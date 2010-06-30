/*
 * PESA2.java
 * @author Juan J. Durillo
 * @version 1.0
 *
 */
package jmetal.metaheuristics.pesaII;

import jmetal.base.Algorithm;
import jmetal.base.EvaluationTerminationCriterion;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.archive.AdaptiveGridArchive;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.PESA2Selection;
import jmetal.base.operator.selection.Selection;
import jmetal.util.JMException;

/**
 * This class implements the PESA2 algorithm.
 */
public class PESA2<V extends Variable>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {

  private static final long serialVersionUID = -2487602132730970709L;
	/**
   * Stores the problem to solve
   */
  private Problem<V> problem_;

  /**
  * Constructor
  * Creates a new instance of PESA2
  */
  public PESA2(Problem<V> problem) {
    problem_ = problem;
  } // PESA2

  int biSections, archiveSize;

	public void setBiSections(int biSections) {
		this.biSections = biSections;
	}

	public void setArchiveSize(int archiveSize) {
		this.archiveSize = archiveSize;
	}

  /**
  * Runs of the PESA2 algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution
   * @throws JMException
  */
  public SolutionSet<V> execute() throws JMException{
    int populationSize;
    AdaptiveGridArchive<V> archive;
    SolutionSet<V> solutionSet;

    // Read parameters
    populationSize = getPopulationSize();

    // Initialize the variables
    archive = new AdaptiveGridArchive<V>(archiveSize,biSections,
                                        problem_.getNumberOfObjectives());
    solutionSet  = new SolutionSet<V>(populationSize);
    selectionOperator    = new PESA2Selection<V>();

    //-> Create the initial individual and evaluate it and his constraints
    for (int i = 0; i < populationSize; i++){
      Solution<V> solution = new Solution<V>(problem_);
      problem_.evaluate(solution);
      problem_.evaluateConstraints(solution);
      if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
      	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
      solutionSet.add(solution);
    }
    //<-

    // Incorporate non-dominated solution to the archive
    for(Solution<V> sol : solutionSet) {
      archive.add(sol); // Only non dominated are accepted by the archive
    }

    // Clear the init solutionSet
    solutionSet.clear();

    //Iterations....
    do {
      //-> Create the offSpring solutionSet
      while (solutionSet.size() < populationSize){
        Solution<V> parent1 = (Solution<V>) selectionOperator.execute(archive);
        Solution<V> parent2 = (Solution<V>) selectionOperator.execute(archive);

        Solution<V> [] offSpring = (Solution<V> []) crossoverOperator.execute(parent1, parent2);
        mutationOperator.execute(offSpring[0]);
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);
        if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
        	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
        solutionSet.add(offSpring[0]);
      }

      for(Solution<V> sol : solutionSet)
        archive.add(sol);

      // Clear the solutionSet
      solutionSet.clear();

    }while (!getTerminationCriterion().isTerminated());
    //Return the  solutionSet of non-dominated individual
    return archive;
  } // execute
} // PESA2
