/**
 * UniformMutation.java
 * Class representing a uniform mutation operator
 * @author Antonio J.Nebro
 * @version 1.0
 */
package jmetal.base.operator.mutation;

import jmetal.base.Solution;
import jmetal.base.variable.Real;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class implements a uniform mutation operator.
 * NOTE: the type of the solutions must be <code>SolutionType_.Real</code>
 */
public class UniformMutation extends Mutation<Real> {
    
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
  public void doMutation(double probability, Solution<Real> solution) throws JMException {                        
    for (int var = 0; var < solution.getDecisionVariables().size(); var++)
    {
      if (PseudoRandom.randDouble() < probability)
      {
        double rand = PseudoRandom.randDouble();
        double tmp = (rand - 0.5)*perturbation_.doubleValue();
                                
        tmp += solution.getDecisionVariables().variables_.get(var).getValue();
                
        if (tmp < solution.getDecisionVariables().variables_.get(var).getLowerBound())
            tmp = solution.getDecisionVariables().variables_.get(var).getLowerBound();                    
        else if (tmp > solution.getDecisionVariables().variables_.get(var).getUpperBound())
            tmp = solution.getDecisionVariables().variables_.get(var).getUpperBound();                    
                
        solution.getDecisionVariables().variables_.get(var).setValue(tmp);                             
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
  public void execute(Solution<Real> solution) throws JMException {
    doMutation(probability, solution);
  } // execute                  
} // UniformMutation
