/**
 * NSGAIIBinary_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * Example of using a binary representation in NSGAII
 */
package jmetal.experiments.settings;

import java.util.Properties;

import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.Selection;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.experiments.Settings;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

/**
 *
 * @author Antonio J. Nebro
 */
@SuppressWarnings("unchecked")
public class NSGAIIBinary_Settings extends Settings{
  
  // Default settings
  int populationSize_ = 100   ;
  int maxEvaluations_ = 25000 ;

  // To obtain the default mutation probability, we need to know the lenght of
  // the binary string. One way to do it is by using the getNumberOfBits() method
  // applied to a solution
  Solution dummy = new Solution(problem_) ;
  
  double mutationProbability_  = 1.0/Solution.getNumberOfBits(dummy) ;
  double crossoverProbability_ = 0.9 ;
  
  String paretoFrontFile_ = "" ;
  
  /**
   * Constructor
   */
  public NSGAIIBinary_Settings(Problem problem) {
    super(problem) ;
  } // NSGAIIBinary_Settings
  
  /**
   * Configure NSGAII with user-defined parameter settings
   * @return A NSGAII algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    NSGAII algorithm ;
    Selection  selection ;
    Crossover  crossover ;
    Mutation mutation  ;
    
    QualityIndicator indicators ;
    
    // Creating the problem
    algorithm = new NSGAII(problem_) ;
    
    // Algorithm parameters
    algorithm.setPopulationSize(populationSize_);
    algorithm.setMaxEvaluations(maxEvaluations_);

    
    // Mutation and Crossover Binary codification
    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover");                   
    crossover.setProbability(0.9);                   
    mutation = MutationFactory.getMutationOperator("BitFlipMutation");     
    Solution dummy = new Solution(problem_) ;
    mutation.setProbability(1.0/Solution.getNumberOfBits(dummy));
    
    // Selection Operator 
    selection = SelectionFactory.getSelectionOperator("BinaryTournament2") ;   
    
    // Add the operators to the algorithm
    algorithm.setCrossover(crossover);
    algorithm.setMutation(mutation);
    algorithm.setSelection(selection);
    
   // Creating the indicator object
   if (! paretoFrontFile_.equals("")) {
      indicators = new QualityIndicator(problem_, paretoFrontFile_);
      algorithm.setIndicators(indicators) ;  
   } // if
    return algorithm ;
  } // configure
  
  /**
   * Configure NSGAII with user-defined parameter settings
   * @param settings
   * @return A NSGAII algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure(Properties settings) throws JMException {
    if (settings != null) {
      populationSize_  = Integer.parseInt(settings.getProperty("POPULATION_SIZE", ""+populationSize_)) ;
      maxEvaluations_  = Integer.parseInt(settings.getProperty("MAX_EVALUATIONS", ""+maxEvaluations_)) ;
      crossoverProbability_ = Double.parseDouble(settings.getProperty("CROSSOVER_PROBABILITY", 
                                                    ""+crossoverProbability_)) ;     
      mutationProbability_ = Double.parseDouble(settings.getProperty("MUTATION_PROBABILITY", 
                                                    ""+mutationProbability_)) ;
      paretoFrontFile_ = settings.getProperty("PARETO_FRONT_FILE", "") ;
    }
    
    return configure() ;
  } // configure
} // NSGAIIBinary_Settings
