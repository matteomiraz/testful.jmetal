/**
 * PAES_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * PAES_Settings class of algorithm PAES
 */
package jmetal.experiments.settings;

import java.util.Properties;

import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.mutation.PolynomialMutation;
import jmetal.experiments.Settings;
import jmetal.metaheuristics.paes.PAES;
import jmetal.util.JMException;

/**
 *
 * @author Antonio
 */
@SuppressWarnings("unchecked")
public class PAES_Settings extends Settings{
  
  // Default settings
  int populationSize_ = 100   ;
  int maxEvaluations_ = 25000 ;
  int archiveSize_    = 100   ;
  int biSections_     = 5     ;
 
  double mutationProbability_  = 1.0/problem_.getNumberOfVariables() ;
  
  double  distributionIndexForMutation_ = 20    ;
  
  String paretoFrontFile_ = "" ;
  
  /**
   * Constructor
   */
  public PAES_Settings(Problem problem) {
    super(problem) ;
  } // PAES_Settings
  
  /**
   * Configure the MOCell algorithm with default parameter settings
   * @return an algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    PAES algorithm ;
    PolynomialMutation  mutation  ;
    
    // Creating the problem
    algorithm = new PAES(problem_) ;

    // Algorithm parameters
    algorithm.setMaxEvaluations(maxEvaluations_);
    algorithm.setBiSections(biSections_);
    algorithm.setArchiveSize(archiveSize_ );
    
    // Mutation for Real codification 
    mutation = (PolynomialMutation) MutationFactory.getMutationOperator("PolynomialMutation");                    
    mutation.setProbability(mutationProbability_);
    mutation.setDistributionIndex(distributionIndexForMutation_);    
        
    // Add the operators to the algorithm
    algorithm.setMutation(mutation);
    
//   // Creating the indicator object
//   if (! paretoFrontFile_.equals("")) {
//      indicators = new QualityIndicator(problem_, paretoFrontFile_);
//      algorithm.setIndicators(indicators) ;  
//   } // if
    return algorithm ;
  }
  
  /**
   * Configure an algorithm with user-defined parameter settings
   * @param settings
   * @return An algorithm
   * @throws jmetal.util.JMException
   */
  public Algorithm configure(Properties settings) throws JMException {
    if (settings != null) {
      biSections_  = Integer.parseInt(settings.getProperty("BISECTIONS", ""+biSections_)) ;
      maxEvaluations_  = Integer.parseInt(settings.getProperty("MAX_EVALUATIONS", ""+maxEvaluations_)) ;
      archiveSize_     = Integer.parseInt(settings.getProperty("ARCHIVE_SIZE", ""+archiveSize_)) ;
      mutationProbability_ = Double.parseDouble(settings.getProperty("MUTATION_PROBABILITY", 
                                                    ""+mutationProbability_)) ;
      distributionIndexForMutation_ = 
            Double.parseDouble(settings.getProperty("DISTRIBUTION_INDEX_FOR_MUTATION", 
                                                    ""+distributionIndexForMutation_)) ;
      paretoFrontFile_ = settings.getProperty("PARETO_FRONT_FILE", "") ;
    }
    
    return configure() ;
  }
} // PAES_Settings
