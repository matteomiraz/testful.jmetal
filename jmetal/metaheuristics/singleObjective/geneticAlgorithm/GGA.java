/**
 * GGA.java
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.metaheuristics.singleObjective.geneticAlgorithm;

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
 * Class implementing a generational genetic algorithm
 */
public class GGA<V extends Variable>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {
  private static final long serialVersionUID = -6298837211531862606L;
	private Problem<V>           problem_;

 /**
  *
  * Constructor
  * Create a new GGA instance.
  * @param problem Problem to solve.
  */
  public GGA(Problem<V> problem){
    this.problem_ = problem;
  } // GGA

 /**
  * Execute the GGA algorithm
 * @throws JMException
  */
  public SolutionSet<V> execute() throws JMException {
    int populationSize ;

    SolutionSet<V> population          ;
    SolutionSet<V> offspringPopulation ;

    Comparator<Solution<V>>  comparator        ;

    comparator = new ObjectiveComparator<V>(0) ; // Single objective comparator

    // Read the params
    populationSize = getPopulationSize();

    // Initialize the variables
    population          = new SolutionSet<V>(populationSize) ;
    offspringPopulation = new SolutionSet<V>(populationSize) ;

    // Create the initial population
    Solution<V> newIndividual;
    for (int i = 0; i < populationSize; i++) {
      newIndividual = new Solution<V>(problem_);
      problem_.evaluate(newIndividual);
      if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
      	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(1);
      population.add(newIndividual);
    } //for

    // Sort population
    population.sort(comparator) ;
    while (!getTerminationCriterion().isTerminated()) {
    //while (population.get(0).getObjective(0) > 0.0049) {
      //if ((evaluations % 10) == 0) {
      //  System.out.print("Evaluation: " + evaluations + " Fitness: " +
      //                     population.get(0).getObjective(0)) ;
      //  System.out.println("   Cob: " + population.get(0).convergenceRate_) ;
      //  System.out.println("   Ant: " + population.get(0).usedTranceivers_) ;
      //}

      // Copy the best two individuals to the offspring population
      offspringPopulation.add(new Solution<V>(population.get(0))) ;
      offspringPopulation.add(new Solution<V>(population.get(1))) ;

      // Reproductive cycle
      for (int i = 0 ; i < (populationSize / 2 - 1) ; i ++) {
        // Selection

        Solution<V> parent1 = (Solution<V>)selectionOperator.execute(population);
        Solution<V> parent2 = (Solution<V>)selectionOperator.execute(population);

        // Crossover
        Solution<V> [] offspring = (Solution<V> []) crossoverOperator.execute(parent1, parent2);

        // Mutation
        mutationOperator.execute(offspring[0]);
        mutationOperator.execute(offspring[1]);

        // Evaluation of the new individual
        problem_.evaluate(offspring[0]);
        problem_.evaluate(offspring[1]);

        if(getTerminationCriterion() instanceof EvaluationTerminationCriterion)
        	((EvaluationTerminationCriterion)getTerminationCriterion()).addEvaluations(2);

        // Replacement: the two new individuals are inserted in the offspring
        //                population
        offspringPopulation.add(offspring[0]) ;
        offspringPopulation.add(offspring[1]) ;
      } // for

      // The offspring population becomes the new current population
      population.clear();
      for (int i = 0; i < populationSize; i++) {
        population.add(offspringPopulation.get(i)) ;
      }
      offspringPopulation.clear();
      population.sort(comparator) ;
    } // while

    // Return a population with the best individual
    SolutionSet<V> resultPopulation = new SolutionSet<V>(1) ;
    resultPopulation.add(population.get(0)) ;

    System.out.println(getTerminationCriterion()) ;
    return resultPopulation ;
  } // execute
} // SSGA