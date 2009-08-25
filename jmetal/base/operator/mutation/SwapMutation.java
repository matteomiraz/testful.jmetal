/**
 * SwapMutation.java
 * Class representing a swap mutation operator
 * @author Antonio J.Nebro
 * @version 1.0
 */

package jmetal.base.operator.mutation;

import jmetal.base.Solution;
import jmetal.base.variable.Permutation;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class implements a swap mutation.
 * NOTE: the operator is applied to the first variable of the solutions, and 
 * the type of those variables must be <code>VariableType_.Permutation</code>.
 */
public class SwapMutation extends Mutation<Permutation> {
  private static final long serialVersionUID = 7556625122222056318L;

	/** 
   * Constructor
   */
  public SwapMutation() {    
  } // Constructor
  
  /**
   * Performs the operation
   * @param probability Mutation probability
   * @param solution The solution to mutate
   * @throws JMException 
   */
  public void doMutation(double probability, Solution<Permutation> solution) throws JMException {   
    int permutation[] ;
    int permutationLength ;

    	permutationLength = ((Permutation)solution.getDecisionVariables().variables_.get(0)).getLength() ;
      permutation = ((Permutation)solution.getDecisionVariables().variables_.get(0)).vector_ ;

      if (PseudoRandom.randDouble() < probability) {
        int pos1 ;
        int pos2 ;

        pos1 = PseudoRandom.randInt(0,permutationLength-1) ;
        pos2 = PseudoRandom.randInt(0,permutationLength-1) ;

        while (pos1 == pos2) {
          if (pos1 == (permutationLength - 1)) 
            pos2 = PseudoRandom.randInt(0, permutationLength- 2);
          else 
            pos2 = PseudoRandom.randInt(pos1, permutationLength- 1);
        } // while
        // swap
        int temp = permutation[pos1];
        permutation[pos1] = permutation[pos2];
        permutation[pos2] = temp;    
      } // if
  } // doMutation

  /**
   * Executes the operation
   * @param object An object containing the solution to mutate
   * @return an object containing the mutated solution
   * @throws JMException 
   */
  @Override
  public void execute(Solution<Permutation> parent) throws JMException {
    doMutation(probability, parent);
  } // execute  
} // SwapMutation
