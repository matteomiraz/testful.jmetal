/**
 * RandomSearch.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.randomSearch;

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
import jmetal.util.NonDominatedSolutionList;

/**
 * This class implements the NSGA-II algorithm.
 */
public class RandomSearch<V extends Variable>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {

  private static final long serialVersionUID = -1156000276724123322L;
	/**
   * stores the problem  to solve
   */
  private Problem<V>  problem_;

  /**
  * Constructor
  * @param problem Problem to solve
  */
  public RandomSearch(Problem<V> problem){
    this.problem_ = problem;
  } // RandomSearch

  /**
  * Runs the RandomSearch algorithm.
  * @return a <code>SolutionSet</code> that is a set of solutions
  * as a result of the algorithm execution
   * @throws JMException
  */
  public SolutionSet<V> execute() throws JMException {

	  NonDominatedSolutionList<V> ndl = new NonDominatedSolutionList<V>();

    // Create the initial solutionSet
    Solution<V> newSolution;
    while(!getTerminationCriterion().isTerminated()) {
      newSolution = new Solution<V>(problem_);
      problem_.evaluate(newSolution);
      problem_.evaluateConstraints(newSolution);
      if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
      	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
      ndl.add(newSolution);
    } //for

    return ndl;
  } // execute
} // RandomSearch
