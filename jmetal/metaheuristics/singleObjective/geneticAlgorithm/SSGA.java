/**
 * SSGA.java
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.metaheuristics.singleObjective.geneticAlgorithm;

import java.util.Comparator;

import jmetal.base.Algorithm;
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
 * Class implementing a steady state genetic algorithm
 */
public class SSGA<V extends Variable>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {
  private static final long serialVersionUID = -1246866956324724184L;
	private Problem<V>           problem_;        
  
 /**
  *
  * Constructor
  * Create a new SSGA instance.
  * @param problem Problem to solve
  *
  */
  public SSGA(Problem<V> problem){
    this.problem_ = problem;                        
  } // SSGA
  
 /**
  * Execute the SSGA algorithm
 * @throws JMException 
  */
  public SolutionSet<V> execute() throws JMException {
    int populationSize ;
    int maxEvaluations ;
    int evaluations    ;

    SolutionSet<V> population        ;
    
    Comparator<Solution<V>>  comparator        ;
    
    comparator = new ObjectiveComparator<V>(0) ; // Single objective comparator
    
    // Read the params
    populationSize = ((Integer)this.getInputParameter("populationSize")).intValue();
    maxEvaluations = ((Integer)this.getInputParameter("maxEvaluations")).intValue();                
   
    // Initialize the variables
    population   = new SolutionSet<V>(populationSize);        
    evaluations  = 0;                

    // Create the initial population
    Solution<V> newIndividual;
    for (int i = 0; i < populationSize; i++) {
      newIndividual = new Solution<V>(problem_);                    
      problem_.evaluate(newIndividual);            
      evaluations++;
      population.add(newIndividual);
    } //for       

    while (evaluations < maxEvaluations) {
    //while (population.get(0).getObjective(0) > 0.0049) {
      if ((evaluations % 10000) == 0) {
        System.out.println(evaluations + ": " + population.get(0).getObjective(0)) ;
      } //
      
      // Selection
      Solution<V> parent1 = (Solution<V>)selectionOperator.execute(population);
      Solution<V> parent2 = (Solution<V>)selectionOperator.execute(population);
 
      // Crossover
      Solution<V> [] offspring = (Solution<V> []) crossoverOperator.execute(parent1, parent2);  

      // Mutation
      mutationOperator.execute(offspring[0]);

      // Evaluation of the new individual
      problem_.evaluate(offspring[0]);            
          
      evaluations ++;
    
      // Replacement: replace the last individual is the new one is better
      population.sort(comparator) ;

      Solution<V> lastIndividual = population.get(populationSize - 1) ;
      
      if (lastIndividual.getObjective(0) > offspring[0].getObjective(0)) {
        population.remove(populationSize -1) ;
        population.add(offspring[0]);
      } // if
    } // while
    
    // Return a population with the best individual
    population.sort(comparator) ;

    SolutionSet<V> resultPopulation = new SolutionSet<V>(1) ;
    resultPopulation.add(population.get(0)) ;
    
    System.out.println("Evaluations: " + evaluations ) ;
    
    return resultPopulation ;
  } // execute
} // SSGA
