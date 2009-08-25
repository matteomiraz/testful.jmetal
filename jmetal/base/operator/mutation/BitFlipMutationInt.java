/**
 * BitFlipMutation.java
 * @author Juan J. Durillo
 * @author Antonio J. Nebro
 * @version 1.1
 */
package jmetal.base.operator.mutation;

import jmetal.base.Solution;
import jmetal.base.variable.Int;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class implements a bit flip mutation operator.
 * NOTE: the operator is applied to binary or integer solutions, considering the
 * whole solution as a single variable.
 */
public class BitFlipMutationInt extends Mutation<Int> {

  private static final long serialVersionUID = 7338668714477076348L;

	/**
   * Constructor
   * Creates a new instance of the Bit Flip mutation operator
   */
  public BitFlipMutationInt() {
  } // BitFlipMutation

  /**
   * Perform the mutation operation
   * @param probability Mutation probability
   * @param solution The solution to mutate
   * @throws JMException
   */
  public void doMutation(double probability, Solution<Int> solution) throws JMException {
    try {
         for (int i = 0; i < solution.getDecisionVariables().size(); i++)
            if (PseudoRandom.randDouble() < probability) {
              int value = (int) (PseudoRandom.randInt(
                       (int)solution.getDecisionVariables().variables_.get(i).getLowerBound(),
                       (int)solution.getDecisionVariables().variables_.get(i).getUpperBound()));
              solution.getDecisionVariables().variables_.get(i).setValue(value);
            } // if
    } catch (ClassCastException e1) {
      String msg = "BitFlipMutation.doMutation: ClassCastException error" + e1.getMessage();
      throw new JMException(msg);
    }
  } // doMutation

  /**
   * Executes the operation
   * @param object An object containing a solution to mutate
   * @return An object containing the mutated solution
   * @throws JMException 
   */
  public void execute(Solution<Int> solution) throws JMException {

    doMutation(probability, solution);

  } // execute
} // BitFlipMutation
