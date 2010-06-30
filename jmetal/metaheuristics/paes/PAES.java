/*
 * Paes.java
 * @author Juan J. Durillo
 * @version 1.0
 *
 */
package jmetal.metaheuristics.paes;

import java.util.Comparator;

import jmetal.base.Algorithm;
import jmetal.base.EvaluationTerminationCriterion;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.archive.AdaptiveGridArchive;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.Selection;
import jmetal.util.JMException;

/**
 * This class implements the NSGA-II algorithm.
 */
public class PAES<V extends Variable>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {

  private static final long serialVersionUID = 4817205693804417123L;
	/**
   * Stores the problem to solve
   */
  private Problem<V> problem_;

  /**
  * Create a new PAES instance for resolve a problem
  * @param problem Problem to solve
  */
  public PAES(Problem<V> problem) {
    problem_ = problem;
  } // Paes

  /**
   * Tests two solutions to determine which one becomes be the guide of PAES
   * algorithm
   * @param solution The actual guide of PAES
   * @param mutatedSolution A candidate guide
   */
  public Solution<V> test(Solution<V> solution,
                       Solution<V> mutatedSolution,
                       AdaptiveGridArchive<V> archive){

    int originalLocation = archive.getGrid().location(solution);
    int mutatedLocation  = archive.getGrid().location(mutatedSolution);

    if (originalLocation == -1) {
      return new Solution<V>(mutatedSolution);
    }

    if (mutatedLocation == -1) {
      return new Solution<V>(solution);
    }

    if (archive.getGrid().getLocationDensity(mutatedLocation) <
        archive.getGrid().getLocationDensity(originalLocation)) {
      return new Solution<V>(mutatedSolution);
    }

    return new Solution<V>(solution);
  } // test

  int biSections, archiveSize;

	public void setBiSections(int biSections) {
		this.biSections = biSections;
	}

	public void setArchiveSize(int archiveSize) {
		this.archiveSize = archiveSize;
	}

  /**
  * Runs of the Paes algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution
   * @throws JMException
  */
  public SolutionSet<V> execute() throws JMException{
    AdaptiveGridArchive<V> archive;
    Comparator<Solution<V>> dominance;

    //Initialize the variables
    archive     = new AdaptiveGridArchive<V>(archiveSize,biSections,problem_.getNumberOfObjectives());
    dominance = new DominanceComparator<V>();

    //-> Create the initial solution and evaluate it and his constraints
    Solution<V> solution = new Solution<V>(problem_);
    problem_.evaluate(solution);
    problem_.evaluateConstraints(solution);
    if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
    	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);

    // Add it to the archive
    archive.add(new Solution<V>(solution));


    //Iterations....
    do {
      // Create the mutate one
      Solution<V> mutateIndividual = new Solution<V>(solution);
      mutationOperator.execute(mutateIndividual);

      problem_.evaluate(mutateIndividual);
      problem_.evaluateConstraints(mutateIndividual);
      if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
      	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
      //<-

      // Check dominance
      int flag = dominance.compare(solution,mutateIndividual);

      if (flag == 1) { //If mutate solution dominate
        solution = new Solution<V>(mutateIndividual);
        archive.add(mutateIndividual);
      } else if (flag == 0) { //If none dominate the other
        if (archive.add(mutateIndividual)) {
          solution = test(solution,mutateIndividual,archive);
        }
      }

    } while (!getTerminationCriterion().isTerminated());

    //Return the  population of non-dominated solution
    return archive;
  }  // execute
} // Paes
