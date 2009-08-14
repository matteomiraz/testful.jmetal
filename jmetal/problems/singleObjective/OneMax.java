/**
 * OneMax.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.problems.singleObjective;

import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.variable.Binary;

/**
 * Class representing problem OneMax. The problem consist of maximizing the
 * number of '1's in a binary string.
 */
public class OneMax extends Problem.ProblemBinary {

  
 private static final long serialVersionUID = 2785137155554439680L;

/**
  * Creates a new OneMax problem instance
  * @param numberOfBits Length of the problem
  */
  public OneMax(Integer numberOfBits) {
    numberOfVariables_  = 1;
    numberOfObjectives_ = 1;
    numberOfConstraints_= 0;
    problemName_        = "ONEMAX";
             
    length_       = new int[numberOfVariables_];
    
    length_      [0] = numberOfBits ;
  } // OneMax
    
 /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
  */      
  public void evaluate(Solution<Binary> solution) {
    Binary variable ;
    int    counter  ;
    
    variable = solution.getDecisionVariables().variables_.get(0);
    
    counter = 0 ;

    for (int i = 0; i < variable.getNumberOfBits() ; i++) 
      if (variable.bits_.get(i) == true)
        counter ++ ;

    // OneMax is a maximization problem: multiply by -1 to minimize
    solution.setObjective(0, -1.0*counter);            
  } // evaluate
} // OneMax
