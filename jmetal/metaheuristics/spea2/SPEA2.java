/**
 * SPEA2.java
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.metaheuristics.spea2;

import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.Spea2Fitness;

/** 
 * This class representing the SPEA2 algorithm
 */
public class SPEA2 extends Algorithm{
          
  private static final long serialVersionUID = 749176697788085191L;

	/**
   * Defines the number of tournaments for creating the mating pool
   */
  public static final int TOURNAMENTS_ROUNDS = 1;

  /**
   * Stores the problem to solve
   */
  private Problem problem_;    

  /**
  * Constructor.
  * Create a new SPEA2 instance
  * @param problem Problem to solve
  */
  public SPEA2(Problem problem) {                
    this.problem_ = problem;        
  } // Spea2
   
  /**   
  * Runs of the Spea2 algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution  
   * @throws JMException 
  */  
  public SolutionSet execute() throws JMException{   
    int populationSize, archiveSize, maxEvaluations, evaluations;
    SolutionSet solutionSet, archive, offSpringSolutionSet;    
    
    //Read the params
    populationSize = ((Integer)getInputParameter("populationSize")).intValue();
    archiveSize    = ((Integer)getInputParameter("archiveSize")).intValue();
    maxEvaluations = ((Integer)getInputParameter("maxEvaluations")).intValue();
        
    //Initialize the variables
    solutionSet  = new SolutionSet(populationSize);
    archive     = new SolutionSet(archiveSize);
    evaluations = 0;
        
    //-> Create the initial solutionSet
    Solution newSolution;
    for (int i = 0; i < populationSize; i++) {
      newSolution = new Solution(problem_);
      problem_.evaluate(newSolution);            
      problem_.evaluateConstraints(newSolution);
      evaluations++;
      solutionSet.add(newSolution);
    }                        
        
    while (evaluations < maxEvaluations){               
      SolutionSet union = ((SolutionSet)solutionSet).union(archive);
      Spea2Fitness spea = new Spea2Fitness(union);
      spea.fitnessAssign();
      archive = spea.environmentalSelection(archiveSize);                       
      // Create a new offspringPopulation
      offSpringSolutionSet= new SolutionSet(populationSize);    
      while (offSpringSolutionSet.size() < populationSize){           
        Solution  parent1, parent2;
        int j = 0;
        do{
          j++;                
          parent1 = (Solution)selectionOperator.execute(archive);
        } while (j < SPEA2.TOURNAMENTS_ROUNDS); // do-while                    
        int k = 0;
        do{
          k++;                
          parent2 = (Solution)selectionOperator.execute(archive);
        } while (k < SPEA2.TOURNAMENTS_ROUNDS); // do-while
            
        //make the crossover 
        Solution [] offSpring = crossoverOperator.execute(parent1, parent2);            
        mutationOperator.execute(offSpring[0]);            
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);            
        offSpringSolutionSet.add(offSpring[0]);
        evaluations++;
      } // while
      // End Create a offSpring solutionSet
      solutionSet = offSpringSolutionSet;                   
    } // while
        
    Ranking ranking = new Ranking(archive);
    return ranking.getSubfront(0);
  } // execute    
} // Spea2
