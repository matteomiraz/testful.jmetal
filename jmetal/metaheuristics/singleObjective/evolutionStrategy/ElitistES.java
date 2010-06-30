/**
 * Elitist.java
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.metaheuristics.singleObjective.evolutionStrategy;

import java.util.Comparator;

import jmetal.base.Algorithm;
import jmetal.base.EvaluationTerminationCriterion;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.operator.comparator.ObjectiveComparator;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.Selection;
import jmetal.util.JMException;

/**
 * Class implementing a (mu + lambda) ES. Lambda must be divisible by mu
 */
public class ElitistES<V extends Variable>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {
  private static final long serialVersionUID = -4777692864162116020L;
	private Problem<V> problem_;
  private int     mu_     ;
  private int     lambda_ ;

 /**
  * Constructor
  * Create a new ElitistES instance.
  * @param problem Problem to solve.
  * @mu Mu
  * @lambda Lambda
  */
  public ElitistES(Problem<V> problem, int mu, int lambda){
    problem_ = problem;
    mu_      = mu     ;
    lambda_  = lambda ;
  } // ElitistES

 /**
  * Execute the ElitistES algorithm
 * @throws JMException
  */
  public SolutionSet<V> execute() throws JMException {
    SolutionSet<V> population          ;
    SolutionSet<V> offspringPopulation ;

    Comparator<Solution<V>> comparator       ;

    comparator = new ObjectiveComparator<V>(0) ; // Single objective comparator

    // Initialize the variables
    population          = new SolutionSet<V>(mu_) ;
    offspringPopulation = new SolutionSet<V>(mu_ + lambda_) ;

    // Read the operators
    System.out.println("(" + mu_ + " + " + lambda_+")ES") ;

    // Create the parent population of mu solutions
    Solution<V> newIndividual;
    for (int i = 0; i < mu_; i++) {
      newIndividual = new Solution<V>(problem_);
      problem_.evaluate(newIndividual);
      if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
      	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
      population.add(newIndividual);
    } //for

    // Main loop
    int offsprings ;
    offsprings = lambda_ / mu_ ;
    while (!getTerminationCriterion().isTerminated()) {
      // STEP 1. Generate the mu+lambda population
      for (int i = 0; i < mu_; i++) {
        for (int j = 0; j < offsprings; j++) {
          Solution<V> offspring = new Solution<V>(population.get(i)) ;
          mutationOperator.execute(offspring);
          problem_.evaluate(offspring) ;
          offspringPopulation.add(offspring) ;
          if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
          	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
        } // for
      } // for

      // STEP 2. Add the mu individuals to the offspring population
      for (int i = 0 ; i < mu_; i++) {
        offspringPopulation.add(population.get(i)) ;
      } // for
      population.clear() ;

      // STEP 3. Sort the mu+lambda population
      offspringPopulation.sort(comparator) ;

      // STEP 4. Create the new mu population
      for (int i = 0; i < mu_; i++)
        population.add(offspringPopulation.get(i)) ;

      System.out.println(getTerminationCriterion() + " Fitness: " +
          population.get(0).getObjective(0)) ;

      // STEP 6. Delete the mu+lambda population
      offspringPopulation.clear() ;
    } // while

    // Return a population with the best individual
    SolutionSet<V> resultPopulation = new SolutionSet<V>(1) ;
    resultPopulation.add(population.get(0)) ;

    return resultPopulation ;
  } // execute
} // SSGA