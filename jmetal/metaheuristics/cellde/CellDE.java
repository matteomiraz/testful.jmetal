/**
 * CellDE.java
 * @author Juan J. Durillo, Antonio J. Nebro
 * @version 1.0
 */
package jmetal.metaheuristics.cellde;

import java.util.Comparator;

import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.operator.comparator.CrowdingComparator;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.base.operator.crossover.DifferentialCrossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.Selection;
import jmetal.base.variable.Real;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Neighborhood;
import jmetal.util.PseudoRandom;
import jmetal.util.Ranking;

/**
 * This class represents the original asynchronous MOCell algorithm
 * hybridized with Diferential evolutions (GDE3), called CellDE. It uses an 
 * archive based on spea2 fitness to store non-dominated solutions.
 */
public class CellDE<V extends Real, S extends Selection<V, Solution<V>>>
	extends Algorithm<V, DifferentialCrossover<V>, Mutation<V>, S, LocalSearch<V>> {

  private static final long serialVersionUID = 6952485598282407726L;
	/**
   * Stores the problem to solve
   */
  private Problem<V> problem_;

  /** 
   * Constructor
   * @param problem Problem to solve
   */
  public CellDE(Problem<V> problem){
    problem_ = problem;
  } // CellDE

  private int archiveSize;
  
	public void setArchiveSize(int archiveSize) {
		this.archiveSize = archiveSize;
	}
  
	private int feedBack;
	
	public void setFeedBack(int feedBack) {
		this.feedBack = feedBack;
	}

  /**   
   * Runs of the CellDE algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution  
   * @throws JMException 
   */ 
  @SuppressWarnings("unchecked")
	public SolutionSet<V> execute() throws JMException {
    int populationSize, maxEvaluations, evaluations;
    SolutionSet<V> currentSolutionSet;
    SolutionSet<V> archive;
    SolutionSet<V>[] neighbors;    
    Neighborhood<V> neighborhood;
    Comparator<Solution<V>> dominance = new DominanceComparator<V>(),
    crowding  = new CrowdingComparator<V>();  

    //Read the params
    populationSize    = getPopulationSize();
    maxEvaluations    = getMaxEvaluations();                

    //Initialize the variables    
    currentSolutionSet  = new SolutionSet<V>(populationSize);                       
    archive            = new jmetal.base.archive.StrengthRawFitnessArchive<V>(archiveSize);
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

        Solution<V> [] parents = new Solution[3];
        Solution<V> offSpring;

        neighbors[ind] = neighborhood.getEightNeighbors(currentSolutionSet,ind);   
        
        //parents
        parents[0] = selectionOperator.execute(neighbors[ind]);
        parents[1] = selectionOperator.execute(neighbors[ind]);
        parents[2] = individual ;

        //Create a new solution, using genetic operators mutation and crossover
        offSpring = crossoverOperator.execute(individual, parents);               
        
        //->Evaluate offspring and constraints
        problem_.evaluate(offSpring);
        problem_.evaluateConstraints(offSpring);
        evaluations++;

        int flag = dominance.compare(individual,offSpring);

        if (flag == 1){ //The offSpring dominates
          offSpring.setLocation(individual.getLocation());                                      
          //currentSolutionSet.reemplace(offSpring[0].getLocation(),offSpring[0]);
          currentSolutionSet.replace(ind,new Solution<V>(offSpring));
          //newSolutionSet.add(offSpring);
          archive.add(new Solution<V>(offSpring));                   
        } else if (flag == 0) { //Both two are non-dominates
          neighbors[ind].add(offSpring);
          //(new Spea2Fitness(neighbors[ind])).fitnessAssign();                   
          //neighbors[ind].sort(new FitnessAndCrowdingDistanceComparator()); //Create a new comparator;
          Ranking<V> rank = new Ranking<V>(neighbors[ind]);
          for (int j = 0; j < rank.getNumberOfSubfronts(); j++){
            Distance.crowdingDistanceAssignment(rank.getSubfront(j),problem_.getNumberOfObjectives());
          }

          boolean deleteMutant = true;          
          int compareResult = crowding.compare(individual,offSpring);
          if (compareResult == 1) {//The offSpring[0] is better
            deleteMutant = false;
          }

          if (!deleteMutant){
            offSpring.setLocation(individual.getLocation());
            //currentSolutionSet.reemplace(offSpring[0].getLocation(),offSpring[0]);
            //newSolutionSet.add(offSpring);
            currentSolutionSet.replace(offSpring.getLocation(), offSpring);
            archive.add(new Solution<V>(offSpring));
          }else{
            //newSolutionSet.add(new Solution(currentSolutionSet.get(ind)));
            archive.add(new Solution<V>(offSpring));    
          }
        }                              
      }             
      
      //Store a portion of the archive into the population
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
} // CellDE