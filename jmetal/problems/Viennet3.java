/**
 * Viennet3.java
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.problems;

import java.util.List;

import jmetal.base.ProblemValue;
import jmetal.base.Solution;
import jmetal.base.VariableValue;
import jmetal.util.JMException;

/**
 * Class representing problem Viennet3
 */
public class Viennet3<T extends VariableValue> extends ProblemValue<T> {           
  
 private static final long serialVersionUID = 4453468598271961362L;
 
 private final Class<T> solutionType_;

/** 
  * Constructor.
  * Creates a default instance of the Viennet3 problem.
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public Viennet3(Class<T> solutionType) {
    numberOfVariables_   = 2 ;
    numberOfObjectives_  = 3 ;
    numberOfConstraints_ = 0;
    problemName_         = "Viennet3";
        
    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] =  -3.0;
      upperLimit_[var] =   3.0;
    } // for
        
    solutionType_ = solutionType; 
  } //Viennet3
      

  /**
   * Evaluates a solution.
   * @param solution The solution to evaluate.
   * @throws JMException 
   */
  public void evaluate(Solution<T> solution) throws JMException {                
    double [] x = new double[numberOfVariables_];
    double [] f = new double[numberOfObjectives_];
        
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = solution.getDecisionVariables().variables_.get(i).getValue();
                 
    f[0] = 0.5 * (x[0]*x[0] + x[1]*x[1]) + Math.sin(x[0]*x[0] + x[1]*x[1]) ;

    // Second function
    double value1 = 3.0 * x[0] - 2.0 * x[1] + 4.0 ;
    double value2 = x[0] - x[1] + 1.0 ;
    f[1] = (value1 * value1)/8.0 + (value2 * value2)/27.0 + 15.0 ;

    // Third function
    f[2] = 1.0 / (x[0]*x[0] + x[1]*x[1]+1) - 1.1 *
          Math.exp(-(x[0]*x[0])-(x[1]*x[1])) ;

        
    for (int i = 0; i < numberOfObjectives_; i++)
      solution.setObjective(i,f[i]);        
  } // evaluate

  @Override
  public List<T> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} // Viennet3


