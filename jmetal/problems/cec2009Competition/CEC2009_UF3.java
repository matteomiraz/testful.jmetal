/**
 * CEC2009_UF3.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.problems.cec2009Competition;

import java.util.List;

import jmetal.base.DecisionVariables;
import jmetal.base.ProblemValue;
import jmetal.base.Solution;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

/**
 * Class representing problem CEC2009_UF3
 */
public class CEC2009_UF3<T extends IReal> extends ProblemValue<T> {
    
 private static final long serialVersionUID = -2794991064686477992L;

 private final Class<T> solutionType_;

/** 
  * Constructor.
  * Creates a default instance of problem CEC2009_UF3 (30 decision variables)
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CEC2009_UF3(Class<T> solutionType) {
    this(30, solutionType); // 30 variables by default
  } // CEC2009_UF1
  
 /**
  * Creates a new instance of problem CEC2009_UF3.
  * @param numberOfVariables Number of variables.
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CEC2009_UF3(Integer numberOfVariables, Class<T> solutionType) {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ =  2;
    numberOfConstraints_=  0;
    problemName_        = "CEC2009_UF3";

    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];

    // Establishes upper and lower limits for the variables
    for (int var = 0; var < numberOfVariables_; var++)
    {
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    } // for

    solutionType_ = solutionType; 
  } // CEC2009_UF3
    
  /** 
   * Evaluates a solution.
   * @param solution The solution to evaluate.
   * @throws JMException 
   */
  public void evaluate(Solution<T> solution) throws JMException {
    DecisionVariables<T> decisionVariables  = solution.getDecisionVariables();
    
    double [] x = new double[numberOfVariables_] ;
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = decisionVariables.variables_.get(i).getValue() ;

  	int count1, count2;
		double sum1, sum2, prod1, prod2, yj, pj;
		sum1   = sum2   = 0.0;
		count1 = count2 = 0;
 		prod1  = prod2  = 1.0;

    
    for (int j = 2 ; j <= numberOfVariables_; j++) {
			yj = x[j-1]-Math.pow(x[0],0.5*(1.0+3.0*(j-2.0)/(numberOfVariables_-2.0)));
			pj = Math.cos(20.0*yj*Math.PI/Math.sqrt(j));
			if (j % 2 == 0) {
				sum2  += yj*yj;
				prod2 *= pj;
				count2++;
			} else {
				sum1  += yj*yj;
				prod1 *= pj;
				count1++;
			}
    }
    
    solution.setObjective(0,  x[0] + 2.0*(4.0*sum1 - 2.0*prod1 + 2.0) / (double)count1);
    solution.setObjective(1, 1.0 - Math.sqrt(x[0]) + 2.0*(4.0*sum2 - 2.0*prod2 + 2.0) / (double)count2);
  } // evaluate

  @Override
  public List<T> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} // CEC2009_UF3
