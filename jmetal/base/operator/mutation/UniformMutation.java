/**
 * UniformMutation.java
 * Class representing a uniform mutation operator
 * @author Antonio J.Nebro
 * @version 1.0
 */
package jmetal.base.operator.mutation;

import jmetal.base.Configuration;
import jmetal.base.Solution;
import jmetal.base.Configuration.SolutionType_;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class implements a uniform mutation operator.
 * NOTE: the type of the solutions must be <code>SolutionType_.Real</code>
 */
public class UniformMutation extends Mutation {
    
  private static final long serialVersionUID = -1807873962838274568L;
	/**
   * Stores the value used in a uniform mutation operator
   */
  private Double perturbation_;
    
    
  /** 
   * Constructor
   * Creates a new uniform mutation operator instance
   */
  public UniformMutation()
  {
  } // UniformMutation

  /**
  * Performs the operation
  * @param probability Mutation probability
  * @param solution The solution to mutate
   * @throws JMException 
  */
  public void doMutation(double probability, Solution solution) throws JMException {                        
    for (int var = 0; var < solution.getDecisionVariables().size(); var++)
    {
      if (PseudoRandom.randDouble() < probability)
      {
        double rand = PseudoRandom.randDouble();
        double tmp = (rand - 0.5)*perturbation_.doubleValue();
                                
        tmp += solution.getDecisionVariables().variables_[var].getValue();
                
        if (tmp < solution.getDecisionVariables().variables_[var].getLowerBound())
            tmp = solution.getDecisionVariables().variables_[var].getLowerBound();                    
        else if (tmp > solution.getDecisionVariables().variables_[var].getUpperBound())
            tmp = solution.getDecisionVariables().variables_[var].getUpperBound();                    
                
        solution.getDecisionVariables().variables_[var].setValue(tmp);                             
      }
    }
  } // doMutation
  
  
	public void setPerturbationIndex(double value) {
		perturbation_ = value;
	}
  
  /**
  * Executes the operation
  * @param object An object containing the solution to mutate
   * @throws JMException 
  */
  public Solution execute(Solution solution) throws JMException {
    if (solution.getType() != SolutionType_.Real) {
      String msg = "UniformMutation.execute: the solution " +
          "is not of the right type. The type should be 'Real', but " +
          solution.getType() + " is obtained";
			Configuration.logger_.severe(msg);
      throw new JMException(msg) ;
    } // if 
    
    doMutation(probability, solution);
        
    return solution;
  } // execute                  
} // UniformMutation
