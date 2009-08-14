/**
 * CrossoverFactory.java
 *
 * @author Juanjo Durillo
 * @version 1.1
 */

package jmetal.base.operator.mutation;

import jmetal.base.Configuration;
import jmetal.util.JMException;

/**
 * Class implementing a mutation factory.
 */
public class MutationFactory {
  
  /**
   * Gets a crossover operator through its name.
   * @param name of the operator
   * @return the operator
   * @throws JMException 
   */
  @SuppressWarnings("unchecked")
	public static Mutation<?> getMutationOperator(String name) throws JMException{
  
    if (name.equalsIgnoreCase("PolynomialMutation"))
      return new PolynomialMutation(20);
    else if (name.equalsIgnoreCase("BitFlipMutation"))
      return new BitFlipMutationInt();
    else if (name.equalsIgnoreCase("SwapMutation"))
      return new SwapMutation();
    else
    {
      String msg = "Operator '" + name + "' not found ";
			Configuration.logger_.severe(msg);
      throw new JMException(msg) ;
    }        
  } // getMutationOperator
} // MutationFactory
