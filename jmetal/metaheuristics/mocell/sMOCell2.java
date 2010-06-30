/**
 * sMOCell2.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.mocell;

import java.util.Comparator;

import jmetal.base.Algorithm;
import jmetal.base.EvaluationTerminationCriterion;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.archive.CrowdingArchive;
import jmetal.base.operator.comparator.CrowdingComparator;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.Selection;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Neighborhood;
import jmetal.util.Ranking;

/**
 * This class representing a sychronous version of MOCell algorithm
 * In this version the feedback take places through parents selections
 * from the archive
 *
 */
public class sMOCell2<V extends Variable>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {

  private static final long serialVersionUID = 5155649352719225604L;
	/**
   * Stores the problem to solve
   */
  private Problem<V> problem_;

  /**
   * Constructor
   * @param problem Problem to solve
   */
  public sMOCell2(Problem<V> problem){
    problem_= problem;
  } //sMOCell2

  private int archiveSize;

	public void setArchiveSize(int archiveSize) {
		this.archiveSize = archiveSize;
	}

  /**
   * Runs of the sMOCell2 algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution
   * @throws JMException
   */
  @SuppressWarnings("unchecked")
	public SolutionSet<V> execute() throws JMException {
    int populationSize;
    SolutionSet<V> currentSolutionSet, newSolutionSet;
    CrowdingArchive<V> archive;
    SolutionSet<V> [] neighbors;
    Neighborhood<V> neighborhood;
    Comparator<Solution<V>> dominance = new DominanceComparator<V>(),
    crowding  = new CrowdingComparator<V>();

    //Read the params
    populationSize    = getPopulationSize();

    //Initialize the variables
    currentSolutionSet  = new SolutionSet<V>(populationSize);
    newSolutionSet     = new SolutionSet<V>(populationSize);
    archive            = new CrowdingArchive<V>(archiveSize,problem_.getNumberOfObjectives());
    neighborhood       = new Neighborhood<V>(populationSize);
    neighbors          = new SolutionSet[populationSize];

    //Create the initial population
    for (int i = 0; i < populationSize; i++){
      Solution<V> solution = new Solution<V>(problem_);
      problem_.evaluate(solution);
      problem_.evaluateConstraints(solution);
      currentSolutionSet.add(solution);
      solution.setLocation(i);
      if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
      	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
    }

    while (!getTerminationCriterion().isTerminated()){
      newSolutionSet = new SolutionSet<V>(populationSize);
      for (int ind = 0; ind < currentSolutionSet.size(); ind++){
        Solution<V> individual = new Solution<V>(currentSolutionSet.get(ind));

        Solution<V> [] offSpring;

        //neighbors[ind] = neighborhood.getFourNeighbors(currentSolutionSet,ind);
        neighbors[ind] = neighborhood.getEightNeighbors(currentSolutionSet,ind);
        neighbors[ind].add(individual);

        //parents
        Solution<V> parent1 = selectionOperator.execute(neighbors[ind]);
        Solution<V> parent2;
				if (archive.size()>0) {
          parent2 = selectionOperator.execute(archive);
        } else {
          parent2 = selectionOperator.execute(neighbors[ind]);
        }

        //Create a new solution, using genetic operators mutation and crossover
        offSpring = crossoverOperator.execute(parent1, parent2);
        mutationOperator.execute(offSpring[0]);

        //->Evaluate solution an his constraints
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);
        if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
        	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
        //<-Individual evaluated

        int flag = dominance.compare(individual,offSpring[0]);

        if (flag == -1) {
          newSolutionSet.add(new Solution<V>(currentSolutionSet.get(ind)));
        }

        if (flag == 1) {//The new indivudlas dominate
          offSpring[0].setLocation(individual.getLocation());
          //currentSolutionSet.reemplace(offSpring[0].getLocation(),offSpring[0]);
          newSolutionSet.add(offSpring[0]);
          archive.add(new Solution<V>(offSpring[0]));
        } else if (flag == 0) { //The individuals are non-dominates
          neighbors[ind].add(offSpring[0]);
          //(new Spea2Fitness(neighbors[ind])).fitnessAssign();
          //neighbors[ind].sort(new FitnessAndCrowdingDistanceComparator()); //Create a new comparator;
          Ranking<V> rank = new Ranking<V>(neighbors[ind]);
          for (int j = 0; j < rank.getNumberOfSubfronts(); j++){
            Distance.crowdingDistanceAssignment(rank.getSubfront(j),problem_.getNumberOfObjectives());
          }
          boolean deleteMutant = true;

          int compareResult = crowding.compare(individual,offSpring[0]);
          if (compareResult == 1){ //The offSpring[0] is better
            deleteMutant = false;
          }

          if (!deleteMutant){
            offSpring[0].setLocation(individual.getLocation());
            //currentSolutionSet.reemplace(offSpring[0].getLocation(),offSpring[0]);
            newSolutionSet.add(offSpring[0]);
            archive.add(new Solution<V>(offSpring[0]));
          }else{
            newSolutionSet.add(new Solution<V>(currentSolutionSet.get(ind)));
            archive.add(new Solution<V>(offSpring[0]));
          }
        }
      }


      currentSolutionSet = newSolutionSet;
    }
    return archive;
  } // execute
} // sMOCell2

