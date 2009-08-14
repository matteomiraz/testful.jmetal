/**
 * aMOCell1.java
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
import jmetal.util.PseudoRandom;
import jmetal.util.Ranking;

/**
 * This class representing the asychronous version of MOCell algorithm
 */
public class aMOCell1<V extends Variable>
extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {

  private static final long serialVersionUID = -1168663720157150836L;
	/**
   * Stores the problem to solve
   */
  private Problem<V> problem_;     

  /** 
   * Constructor
   * @param problem Problem to solve
   */
  public aMOCell1(Problem<V> problem){
    problem_ = problem;
  } //aMOCell1       

  /**   
   * Runs of the aMOCell1 algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution  
   * @throws JMException 
   */   
  @SuppressWarnings("unchecked")
	public SolutionSet<V> execute() throws JMException {
    int populationSize, archiveSize, maxEvaluations, evaluations, feedBack;
    SolutionSet<V> currentSolutionSet;
    CrowdingArchive<V> archive;
    SolutionSet<V> [] neighbors;    
    Neighborhood<V> neighborhood;
    Comparator<Solution<V>> dominance = new DominanceComparator<V>(),
    crowding  = new CrowdingComparator<V>();                   

    //Read the params
    populationSize    = ((Integer)getInputParameter("populationSize")).intValue();
    archiveSize       = ((Integer)getInputParameter("archiveSize")).intValue();
    maxEvaluations    = ((Integer)getInputParameter("maxEvaluations")).intValue();                
    feedBack          = ((Integer)getInputParameter("feedBack")).intValue();

    //Init the variables
    //init the population and the archive
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
        Solution<V> parent2 = selectionOperator.execute(neighbors[ind]);

        //Create a new solution, using genetic operators mutation and crossover
        offSpring = crossoverOperator.execute(parent1, parent2);               
        mutationOperator.execute(offSpring[0]);

        //Evaluate solution and constraints
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);
        evaluations++;

        // Check dominance
        int flag = dominance.compare(individual,offSpring[0]);               
        if (flag == 1) { // offSpring[0] dominates
          offSpring[0].setLocation(individual.getLocation());                                      
          currentSolutionSet.replace(offSpring[0].getLocation(),offSpring[0]);
          archive.add(new Solution<V>(offSpring[0]));                   
        } else if (flag == 0) { //Both two are non-dominates               
          neighbors[ind].add(offSpring[0]);
          //(new Spea2Fitness(neighbors[ind])).fitnessAssign();                   
          //neighbors[ind].sort(new FitnessAndCrowdingDistanceComparator());
          Ranking<V> rank = new Ranking<V>(neighbors[ind]);
          for (int j = 0; j < rank.getNumberOfSubfronts(); j++) {
            Distance.crowdingDistanceAssignment(rank.getSubfront(j),
                problem_.getNumberOfObjectives());
          }

          boolean deleteMutant = true;          
          int compareResult = crowding.compare(individual,offSpring[0]);
          if (compareResult == 1) //The offSpring[0] is better
            deleteMutant = false;

          if (!deleteMutant){
            offSpring[0].setLocation(individual.getLocation());
            currentSolutionSet.replace(offSpring[0].getLocation(),offSpring[0]);
            archive.add(new Solution<V>(offSpring[0]));
          } else {
            archive.add(new Solution<V>(offSpring[0]));    
          }
        }                              
      }                     

      //Stores a portion of the archive into the population
      Distance.crowdingDistanceAssignment(archive,problem_.getNumberOfObjectives());                      
      for (int j = 0; j < feedBack; j++){
        if (archive.size() > j){
          int r = PseudoRandom.randInt(0,currentSolutionSet.size()-1);
          if (r < currentSolutionSet.size()){
            Solution<V> individual = archive.get(j);
            individual.setLocation(r);
            currentSolutionSet.replace(r,new Solution<V>(individual));
          }
        }
      }                      
    }
    return archive;
  } // execute        
} // aMOCell1

