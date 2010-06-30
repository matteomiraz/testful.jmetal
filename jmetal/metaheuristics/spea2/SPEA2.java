/**
 * SPEA2.java
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.metaheuristics.spea2;

import jmetal.base.Algorithm;
import jmetal.base.EvaluationTerminationCriterion;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.Selection;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.Spea2Fitness;

/**
 * This class representing the SPEA2 algorithm
 */
public class SPEA2<V extends Variable>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {

  private static final long serialVersionUID = 749176697788085191L;

	/**
   * Defines the number of tournaments for creating the mating pool
   */
  public static final int TOURNAMENTS_ROUNDS = 1;

  /**
   * Stores the problem to solve
   */
  private Problem<V> problem_;

  /**
  * Constructor.
  * Create a new SPEA2 instance
  * @param problem Problem to solve
  */
  public SPEA2(Problem<V> problem) {
    this.problem_ = problem;
  } // Spea2

  private int archiveSize;

	public void setArchiveSize(int archiveSize) {
		this.archiveSize = archiveSize;
	}

  /**
  * Runs of the Spea2 algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution
   * @throws JMException
  */
  public SolutionSet<V> execute() throws JMException{
    int populationSize;
    SolutionSet<V> solutionSet, archive, offSpringSolutionSet;

    //Read the params
    populationSize = getPopulationSize();

    //Initialize the variables
    solutionSet  = new SolutionSet<V>(populationSize);
    archive     = new SolutionSet<V>(archiveSize);

    //-> Create the initial solutionSet
    Solution<V> newSolution;
    for (int i = 0; i < populationSize; i++) {
      newSolution = new Solution<V>(problem_);
      problem_.evaluate(newSolution);
      problem_.evaluateConstraints(newSolution);
      if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
      	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
      solutionSet.add(newSolution);
    }

    while (!getTerminationCriterion().isTerminated()){
      SolutionSet<V> union = ((SolutionSet<V>)solutionSet).union(archive);
      Spea2Fitness<V> spea = new Spea2Fitness<V>(union);
      spea.fitnessAssign();
      archive = spea.environmentalSelection(archiveSize);
      // Create a new offspringPopulation
      offSpringSolutionSet= new SolutionSet<V>(populationSize);
      while (offSpringSolutionSet.size() < populationSize){
        Solution<V>  parent1, parent2;
        int j = 0;
        do{
          j++;
          parent1 = (Solution<V>)selectionOperator.execute(archive);
        } while (j < SPEA2.TOURNAMENTS_ROUNDS); // do-while
        int k = 0;
        do{
          k++;
          parent2 = (Solution<V>)selectionOperator.execute(archive);
        } while (k < SPEA2.TOURNAMENTS_ROUNDS); // do-while

        //make the crossover
        Solution<V> [] offSpring = crossoverOperator.execute(parent1, parent2);
        mutationOperator.execute(offSpring[0]);
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);
        offSpringSolutionSet.add(offSpring[0]);
        if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
        	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
      } // while
      // End Create a offSpring solutionSet
      solutionSet = offSpringSolutionSet;
    } // while

    Ranking<V> ranking = new Ranking<V>(archive);
    return ranking.getSubfront(0);
  } // execute
} // Spea2
