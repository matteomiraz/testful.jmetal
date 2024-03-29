/**
 * ssNSGAII.java
 * @author Antonio J. Nebro
 * @version 1.0  
 */
package jmetal.metaheuristics.nsgaII;

import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.Selection;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;

/**
 * This class implements a steady-state version of NSGA-II.
 */
public class ssNSGAII<V extends Variable>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {

  private static final long serialVersionUID = 4074688177180204337L;
	/**
   * stores the problem  to solve
   */
  private Problem<V> problem_;

  /**
   * Constructor
   * @param problem Problem to solve
   */
  public ssNSGAII(Problem<V> problem) {
    this.problem_ = problem;
  } // NSGAII

  private QualityIndicator<V> indicators; // QualityIndicator object
  
	public void setIndicators(QualityIndicator<V> indicators) {
		this.indicators = indicators;
	}

  /**   
   * Runs the ssNSGA-II algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution
   * @throws JMException 
   */
	public SolutionSet<V> execute() throws JMException {
    int populationSize;
    int maxEvaluations;
    int evaluations;

    int requiredEvaluations; // Use in the example of use of the
    // indicators object (see below)

    SolutionSet<V> population;
    SolutionSet<V> offspringPopulation;
    SolutionSet<V> union;

    //Read the parameters
    populationSize = getPopulationSize();
    maxEvaluations = getMaxEvaluations();

    //Initialize the variables
    population = new SolutionSet<V>(populationSize);
    evaluations = 0;

    requiredEvaluations = 0;

    // Create the initial solutionSet
    Solution<V> newSolution;
    for (int i = 0; i < populationSize; i++) {
      newSolution = new Solution<V>(problem_);
      problem_.evaluate(newSolution);
      problem_.evaluateConstraints(newSolution);
      evaluations++;
      population.add(newSolution);
    } //for       

    // Generations ...
    while (evaluations < maxEvaluations) {

      // Create the offSpring solutionSet      
      offspringPopulation = new SolutionSet<V>(populationSize);

      //obtain parents
      Solution<V> parent1 = (Solution<V>) selectionOperator.execute(population);
      Solution<V> parent2 = (Solution<V>) selectionOperator.execute(population);
      
      // crossover
      Solution<V>[] offSpring = (Solution<V>[]) crossoverOperator.execute(parent1, parent2);

      // mutation
      mutationOperator.execute(offSpring[0]);

      // evaluation
      problem_.evaluate(offSpring[0]);
      problem_.evaluateConstraints(offSpring[0]);

      // insert child into the offspring population
      offspringPopulation.add(offSpring[0]);

      evaluations ++;

      // Create the solutionSet union of solutionSet and offSpring
      union = ((SolutionSet<V>) population).union(offspringPopulation);

      // Ranking the union
      Ranking<V> ranking = new Ranking<V>(union);

      int remain = populationSize;
      int index = 0;
      SolutionSet<V> front = null;
      population.clear();

      // Obtain the next front
      front = ranking.getSubfront(index);

      while ((remain > 0) && (remain >= front.size())) {
        //Assign crowding distance to individuals
        Distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
        //Add the individuals of this front
        for(Solution<V> s : front) {
          population.add(s);
        } // for

        //Decrement remain
        remain = remain - front.size();

        //Obtain the next front
        index++;
        if (remain > 0) {
          front = ranking.getSubfront(index);
        } // if        
      } // while

      // Remain is less than front(index).size, insert only the best one
      if (remain > 0) {  // front contains individuals to insert                        
        Distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
        front.sort(new jmetal.base.operator.comparator.CrowdingComparator<V>());
        for (int k = 0; k < remain; k++) {
          population.add(front.get(k));
        } // for

        remain = 0;
      } // if                               

      // This piece of code shows how to use the indicator object into the code
      // of NSGA-II. In particular, it finds the number of evaluations required
      // by the algorithm to obtain a Pareto front with a hypervolume higher
      // than the hypervolume of the true Pareto front.
      if ((indicators != null) &&
        (requiredEvaluations == 0)) {
        double HV = indicators.getHypervolume(population);
        if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
          requiredEvaluations = evaluations;
        } // if
      } // if
    } // while

    // Return as output parameter the required evaluations
    setEvaluations(requiredEvaluations);

    // Return the first non-dominated front
    Ranking<V> ranking = new Ranking<V>(population);
    return ranking.getSubfront(0);
  } // execute
} // NSGA-II
