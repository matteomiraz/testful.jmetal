/**
* FPGA.java
* @author Juan J. Durillo
* @version 1.0
*/

package jmetal.metaheuristics.fastPGA;

import java.util.Comparator;

import jmetal.base.Algorithm;
import jmetal.base.EvaluationTerminationCriterion;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.operator.comparator.FPGAFitnessComparator;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.Selection;
import jmetal.util.Distance;
import jmetal.util.FPGAFitness;
import jmetal.util.JMException;
import jmetal.util.Ranking;

/*
* This class implements the FPGA (Fast Pareto Genetic Algorithm).
*/
public class FastPGA<V extends Variable>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {

  private static final long serialVersionUID = -1773050806660065098L;
	Problem<V> problem_;

  /**
   * Constructor
   * Creates a new instance of FastPGA
   */
  public FastPGA(Problem<V> problem) {
    problem_ = problem;
  } // FastPGA

  private double a, b, c, d;
  private int initialPopulationSize;
  private int termination;

	public void setA(double a) {
		this.a = a;
	}

	public void setB(double b) {
		this.b = b;
	}

	public void setC(double c) {
		this.c = c;
	}

	public void setD(double d) {
		this.d = d;
	}

	public void setInitialPopulationSize(int initialPopulationSize) {
		this.initialPopulationSize = initialPopulationSize;
	}

	public void setTermination(int termination) {
		this.termination = termination;
	}

  /**
  * Runs of the FastPGA algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution
   * @throws JMException
  */
  public SolutionSet<V> execute() throws JMException {
    int maxPopSize, populationSize,offSpringSize;

    SolutionSet<V> solutionSet, offSpringSolutionSet, candidateSolutionSet = null;
    Comparator<Solution<V>> fpgaFitnessComparator = new FPGAFitnessComparator<V>();

    //Read the parameters
    maxPopSize     = getPopulationSize();

    //Initialize populationSize and offSpringSize
    populationSize = initialPopulationSize;
    offSpringSize  = maxPopSize;

    //Build a solution set randomly
    solutionSet = new SolutionSet<V>(populationSize);
    for (int i = 0; i < populationSize; i++) {
      Solution<V> solution = new Solution<V>(problem_);
      problem_.evaluate(solution);
      problem_.evaluateConstraints(solution);
      if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
      	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
      solutionSet.add(solution);
    }

    //Begin the iterations
    Solution<V> [] offSprings;
    boolean stop = false;
    while (!stop) {

      // Create the candidate solutionSet
      offSpringSolutionSet = new SolutionSet<V>(offSpringSize);
      for (int i = 0; i < offSpringSize/2; i++) {
        Solution<V> parent1 = selectionOperator.execute(solutionSet);
        Solution<V> parent2 = selectionOperator.execute(solutionSet);
        offSprings = crossoverOperator.execute(parent1, parent2);
        mutationOperator.execute(offSprings[0]);
        mutationOperator.execute(offSprings[1]);
        problem_.evaluate(offSprings[0]);
        problem_.evaluateConstraints(offSprings[0]);
        if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
          	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
        problem_.evaluate(offSprings[1]);
        problem_.evaluateConstraints(offSprings[1]);
        if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
          	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
        offSpringSolutionSet.add(offSprings[0]);
        offSpringSolutionSet.add(offSprings[1]);
      }

      // Merge the populations
      candidateSolutionSet = solutionSet.union(offSpringSolutionSet);

      // Rank
      Ranking<V> ranking = new Ranking<V>(candidateSolutionSet);
      Distance.crowdingDistanceAssignment(ranking.getSubfront(0),problem_.getNumberOfObjectives());
      FPGAFitness<V> fitness = new FPGAFitness<V>(candidateSolutionSet,problem_);
      fitness.fitnessAssign();

      // Count the non-dominated solutions in candidateSolutionSet
      int count = ranking.getSubfront(0).size();

      //Regulate
      populationSize = (int)Math.min(a + Math.floor(b * count),maxPopSize);
      offSpringSize  = (int)Math.min(c + Math.floor(d * count),maxPopSize);

      candidateSolutionSet.sort(fpgaFitnessComparator);
      solutionSet = new SolutionSet<V>(populationSize);

      for (int i = 0; i < populationSize; i++) {
        solutionSet.add(candidateSolutionSet.get(i));
      }

      //Termination test
      if (termination == 0) {
        ranking = new Ranking<V>(solutionSet);
        count = ranking.getSubfront(0).size();
        if (count == maxPopSize) {
          if (getTerminationCriterion().isTerminated()) {
            stop = true;
          }
        }
      } else {
        if (getTerminationCriterion().isTerminated()) {
          stop = true;
        }
      }
    }

    Ranking<V> ranking = new Ranking<V>(solutionSet);
    return ranking.getSubfront(0);
  } // execute
} // FastPGA
