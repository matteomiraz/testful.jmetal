/**
a * GDE3.java
 * @author Antonio J. Nebro
 * @version 1.0  
 */
package jmetal.metaheuristics.gde3;

import java.util.Comparator;

import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.crossover.DifferentialCrossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.DifferentialEvolutionSelection;
import jmetal.base.variable.Real;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;

/**
 * This class implements the NSGA-II algorithm. 
 */
public class GDE3<V extends Real>
	extends Algorithm<V, Crossover<V>, Mutation<V>, DifferentialEvolutionSelection<V>, LocalSearch<V>> {
  
  private static final long serialVersionUID = 7776463559880778310L;
	/**
   * stores the problem  to solve
   */
  private Problem<V>  problem_;        
  
  /**
  * Constructor
  * @param problem Problem to solve
  */
  public GDE3(Problem<V> problem){
    this.problem_ = problem;                        
  } // GDE3
  
  /**   
  * Runs of the NSGA-II algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution  
   * @throws JMException 
  */  
  public SolutionSet<V> execute() throws JMException {
    int populationSize ;
    int maxIterations  ;
    int evaluations    ;
    int iterations     ;
    
    SolutionSet<V> population          ;
    SolutionSet<V> offspringPopulation ;
    
    Comparator<Solution<V>> dominance ;
    dominance = new jmetal.base.operator.comparator.DominanceComparator<V>(); 
    
    Solution<V> parent[] ;
    
    //Read the parameters
    populationSize = getPopulationSize();
    maxIterations  = getMaxEvaluations();                             
   
    //Initialize the variables
    population  = new SolutionSet<V>(populationSize);        
    evaluations = 0;                
    iterations  = 0 ;

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
    while (iterations < maxIterations) {
      // Create the offSpring solutionSet      
      offspringPopulation  = new SolutionSet<V>(populationSize * 2);        

      for (int i = 0; i < (populationSize); i++){   
        // Obtain parents. Two parameters are required: the population and the 
        //                 index of the current individual
      	selectionOperator.setIndex(i);
        parent = selectionOperator.execute(population);

        Solution<V> child ;
        // Crossover. Two parameters are required: the current individual and the 
        //            array of parents
        child = ((DifferentialCrossover<V>)crossoverOperator).execute(population.get(i), parent);

        problem_.evaluate(child) ;
        problem_.evaluateConstraints(child);
        evaluations++ ;
        
        // Dominance test
        int result  ;
        result = dominance.compare(population.get(i), child) ;
        if (result == -1) { // Solution i dominates child
          offspringPopulation.add(population.get(i)) ;
        } // if
        else if (result == 1) { // child dominates
          offspringPopulation.add(child) ;
        } // else if
        else { // the two solutions are non-dominated
          offspringPopulation.add(child) ;
          offspringPopulation.add(population.get(i)) ;
        } // else
      } // for           

      // Ranking the offspring population
      Ranking<V> ranking = new Ranking<V>(offspringPopulation);                        

      int remain = populationSize;
      int index  = 0;
      SolutionSet<V> front = null;
      population.clear();

      // Obtain the next front
      front = ranking.getSubfront(index);

      while ((remain > 0) && (remain >= front.size())){                
        //Assign crowding distance to individuals
        Distance.crowdingDistanceAssignment(front,problem_.getNumberOfObjectives());                
        //Add the individuals of this front
        for (int k = 0; k < front.size(); k++ ) {
          population.add(front.get(k));
        } // for

        //Decrement remain
        remain = remain - front.size();

        //Obtain the next front
        index++;
        if (remain > 0) {
          front = ranking.getSubfront(index);
        } // if        
      } // while
      
      // remain is less than front(index).size, insert only the best one
      if (remain > 0) {  // front contains individuals to insert                        
        //distance.crowdingDistanceAssignment(front,problem_.getNumberOfObjectives());
        //front.sort(new jmetal.base.operator.comparator.CrowdingComparator());
        //for (int k = 0; k < remain; k++) {
        //  population.add(front.get(k));
        //} // for
        while (front.size() > remain) {
           Distance.crowdingDistanceAssignment(front,problem_.getNumberOfObjectives());
           front.sort(new jmetal.base.operator.comparator.CrowdingComparator<V>());
           front.remove(front.size()-1);
        }
        for (int k = 0; k < front.size(); k++) {
          population.add(front.get(k));
        }
        
        remain = 0; 
      } // if                   
      
      iterations ++ ;
    } // while
    
    // Return the first non-dominated front
    Ranking<V> ranking = new Ranking<V>(population);        
    return ranking.getSubfront(0);
  } // execute
} // GDE3-II
