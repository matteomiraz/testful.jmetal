/**
 * CEC2009_UF2.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.problems.cec2009Competition;

import jmetal.base.DecisionVariables;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.Configuration.SolutionType_;
import jmetal.base.Configuration.VariableType_;
import jmetal.util.JMException;

/**
 * Class representing problem CEC2009_UF2
 */
public class CEC2009_UF2 extends Problem {
    
 private static final long serialVersionUID = -5573422843324628445L;

/** 
  * Constructor.
  * Creates a default instance of problem CEC2009_UF2 (30 decision variables)
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CEC2009_UF2(String solutionType) {
    this(30, solutionType); // 30 variables by default
  } // CEC2009_UF1
  
 /**
  * Creates a new instance of problem CEC2009_UF2.
  * @param numberOfVariables Number of variables.
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CEC2009_UF2(Integer numberOfVariables, String solutionType) {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ =  2;
    numberOfConstraints_=  0;
    problemName_        = "CEC2009_UF2";

    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];

    // Establishes upper and lower limits for the variables
    for (int var = 0; var < numberOfVariables_; var++)
    {
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    } // for

    solutionType_ = Enum.valueOf(SolutionType_.class, solutionType) ; 
    
    // All the variables are of the same type, so the solutionType name is the
    // same than the variableType name
    variableType_ = new VariableType_[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++){
      variableType_[var] = Enum.valueOf(VariableType_.class, solutionType) ;    
    } // for
  } // CEC2009_UF2
    
  /** 
   * Evaluates a solution.
   * @param solution The solution to evaluate.
   * @throws JMException 
   */
  public void evaluate(Solution solution) throws JMException {
    DecisionVariables decisionVariables  = solution.getDecisionVariables();
    
    double [] x = new double[numberOfVariables_] ;
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = decisionVariables.variables_.get(i).getValue() ;

  	int count1, count2;
		double sum1, sum2, yj;
		sum1   = sum2   = 0.0;
		count1 = count2 = 0;
    
    for (int j = 2 ; j <= numberOfVariables_; j++) {
			if(j % 2 == 0) {
				yj = x[j-1]-0.3*x[0]*
             (x[0]*Math.cos(24.0*Math.PI*x[0]+4.0*j*Math.PI/numberOfVariables_)+2.0)*
             Math.sin(6.0*Math.PI*x[0]+j*Math.PI/numberOfVariables_);
				sum2 += yj*yj;
				count2++;
			} else {
				yj = x[j-1]-0.3*x[0]*
             (x[0]*Math.cos(24.0*Math.PI*x[0]+4.0*j*Math.PI/numberOfVariables_)+2.0)*
             Math.cos(6.0*Math.PI*x[0]+j*Math.PI/numberOfVariables_);
				sum1 += yj*yj;
				count1++;
			} 
    }
    
    solution.setObjective(0, x[0] + 2.0 * sum1 / (double)count1);
    solution.setObjective(1, 1.0 - Math.sqrt(x[0]) + 2.0 * sum2 / (double)count2);
  } // evaluate
} // CEC2009_UF2
