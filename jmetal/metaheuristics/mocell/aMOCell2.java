/**
 * aMOCell2.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.mocell;

import java.util.Comparator;

import jmetal.base.Algorithm;
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
 * This class representing an asychronous version of MOCell algorithm in 
 * wich feedback take places through parent selection from the archive
 */
public class aMOCell2<V extends Variable>
extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {

  private static final long serialVersionUID = 1680926060959015487L;
	/**
   * Stores the problem to solve
   */
  private Problem<V> problem_;

  /** 
   * Constructor
   * @param problem Problem to solve
   */
  public aMOCell2(Problem<V> problem){
    problem_ = problem;
  } // aMOCell2

  private int archiveSize;
  
	public void setArchiveSize(int archiveSize) {
		this.archiveSize = archiveSize;
	}
	
  /**   
   * Runs of the aMOCell2 algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution  
   * @throws JMException 
   */    
  @SuppressWarnings("unchecked")
	public SolutionSet<V> execute() throws JMException {
    //Init the param
    int populationSize, maxEvaluations, evaluations;
    SolutionSet<V> currentSolutionSet;
    CrowdingArchive<V> archive;
    SolutionSet<V> [] neighbors;    
    Neighborhood<V> neighborhood;
    Comparator<Solution<V>> dominance = new DominanceComparator<V>(),
    crowding  = new CrowdingComparator<V>();  

    //Read the params
    populationSize    = getPopulationSize();
    maxEvaluations    = getMaxEvaluations();                                

    //Initialize the variables
    currentSolutionSet  = new SolutionSet<V>(populationSize);        
    archive            = new CrowdingArchive<V>(archiveSize,problem_.getNumberOfObjectives());                
    evaluations        = 0;                        
    neighborhood       = new Neighborhood<V>(populationSize);
    neighbors          = new SolutionSet[populationSize];


    //Create the initial population
    for (int i = 0; i < populationSize; i++){
      Solution<V> solution = new Solution<V>(problem_);
      problem_.evaluate(solution);           
      problem_.evaluateConstraints(solution);
      currentSolutionSet.add(solution);
      solution.setLocation(i);
      evaluations++;
    }


    while (evaluations < maxEvaluations){                                 
      for (int ind = 0; ind < currentSolutionSet.size(); ind++){
        Solution<V> individual = new Solution<V>(currentSolutionSet.get(ind));

        Solution<V> [] offSpring;

        //neighbors[ind] = neighborhood.getFourNeighbors(currentSolutionSet,ind);
        neighbors[ind] = neighborhood.getEightNeighbors(currentSolutionSet,ind);                                                           
        neighbors[ind].add(individual);

        //parents
        Solution<V> parent1 = selectionOperator.execute(neighbors[ind]);
        Solution<V> parent2;
				if (archive.size() > 0) {
          parent2 = selectionOperator.execute(archive);
        } else {
          parent2 = selectionOperator.execute(neighbors[ind]);
        }

        //Create a new solution, using genetic operators mutation and crossover
        offSpring = crossoverOperator.execute(parent1, parent2);               
        mutationOperator.execute(offSpring[0]);

        //->Evaluate solution and constraints
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);
        evaluations++;

        int flag = dominance.compare(individual,offSpring[0]);

        if (flag == 1){ // OffSpring[0] dominates
          offSpring[0].setLocation(individual.getLocation());                                      
          currentSolutionSet.replace(offSpring[0].getLocation(),offSpring[0]);
          archive.add(new Solution<V>(offSpring[0]));                   
        } else if (flag == 0) { //Both two are non-dominated               
          neighbors[ind].add(offSpring[0]);
          //(new Spea2Fitness(neighbors[ind])).fitnessAssign();                   
          //neighbors[ind].sort(new FitnessAndCrowdingDistanceComparator()); //Create a new comparator;
          Ranking<V> rank = new Ranking<V>(neighbors[ind]);
          for (int j = 0; j < rank.getNumberOfSubfronts(); j++){
            Distance.crowdingDistanceAssignment(rank.getSubfront(j),problem_.getNumberOfObjectives());
          }

          boolean deleteMutant = true;


          int compareResult = crowding.compare(individual,offSpring[0]);
          if (compareResult == 1) { //The offSpring[0] is better
            deleteMutant = false;
          }

          if (!deleteMutant)  {
            offSpring[0].setLocation(individual.getLocation());
            currentSolutionSet.replace(offSpring[0].getLocation(),offSpring[0]);
            archive.add(new Solution<V>(offSpring[0]));
          } else {
            archive.add(new Solution<V>(offSpring[0]));    
          }
        }                              
      }                                                       
    }
    return archive;
  } // execute     
} // aMOCell2

