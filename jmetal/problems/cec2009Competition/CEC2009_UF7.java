/**
 * CEC2009_UF7.java
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
 * Class representing problem CEC2009_UF7
 */
public class CEC2009_UF7<T extends IReal> extends ProblemValue<T> {
    
 private static final long serialVersionUID = -5015954647805240258L;

 private final Class<T> solutionType_;

/** 
  * Constructor.
  * Creates a default instance of problem CEC2009_UF7 (30 decision variables)
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CEC2009_UF7(Class<T> solutionType) {
    this(30, solutionType); // 30 variables by default
  } // CEC2009_UF1
  
 /**
  * Creates a new instance of problem CEC2009_UF7.
  * @param numberOfVariables Number of variables.
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CEC2009_UF7(Integer numberOfVariables, Class<T> solutionType) {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ =  2;
    numberOfConstraints_=  0;
    problemName_        = "CEC2009_UF7";

    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];

    // Establishes upper and lower limits for the variables
    for (int var = 0; var < numberOfVariables_; var++)
    {
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    } // for

    solutionType_ = solutionType; 
  } // CEC2009_UF7
    
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
		double sum1, sum2, yj;
		sum1   = sum2   = 0.0;
		count1 = count2 = 0;
    
    for (int j = 2 ; j <= numberOfVariables_; j++) {
			yj = x[j-1] - Math.sin(6.0*Math.PI*x[0]+j*Math.PI/numberOfVariables_);
			if (j % 2 == 0) {
				sum2  += yj*yj;
				count2++;
			} else {
				sum1  += yj*yj;
				count1++;
			}
    }
    yj = Math.pow(x[0],0.2);
    
    solution.setObjective(0, yj + 2.0*sum1 / (double)count1);
    solution.setObjective(1, 1.0 - yj + 2.0*sum2 / (double)count2);
  } // evaluate

  @Override
  public List<T> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} // CEC2009_UF7
