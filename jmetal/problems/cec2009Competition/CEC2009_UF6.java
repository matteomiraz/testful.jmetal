/**
 * CEC2009_UF6.java
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
 * Class representing problem CEC2009_UF5
 */
public class CEC2009_UF6<T extends IReal> extends ProblemValue<T> {
  private static final long serialVersionUID = 3058107176457728593L;

  private final Class<T> solutionType_;
  
  int    N_       ;
  double epsilon_ ;
 /** 
  * Constructor.
  * Creates a default instance of problem CEC2009_UF6 (30 decision variables)
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CEC2009_UF6(Class<T> solutionType) {
    this(30, 2, 0.1, solutionType); // 30 variables, N =10, epsilon = 0.1
  } // CEC2009_UF1
  
 /**
  * Creates a new instance of problem CEC2009_UF6.
  * @param numberOfVariables Number of variables.
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CEC2009_UF6(Integer numberOfVariables, int N, double epsilon, Class<T> solutionType) {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ =  2;
    numberOfConstraints_=  0;
    problemName_        = "CEC2009_UF6";
    
    N_       = N       ;
    epsilon_ = epsilon ;

    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];

    // Establishes upper and lower limits for the variables
    for (int var = 0; var < numberOfVariables_; var++)
    {
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    } // for

    solutionType_ = solutionType; 
  } // CEC2009_UF6
    
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

  	int count1, count2 ;
    double prod1, prod2 ;
    double sum1, sum2, yj, hj, pj ;
		sum1   = sum2   = 0.0;
		count1 = count2 = 0;
 		prod1  = prod2  = 1.0;
    
    for (int j = 2 ; j <= numberOfVariables_; j++) {
			yj = x[j-1]-Math.sin(6.0*Math.PI*x[0]+j*Math.PI/numberOfVariables_);
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
		hj = 2.0*(0.5/N_ + epsilon_)*Math.sin(2.0*N_*Math.PI*x[0]);
		if (hj < 0.0) 
      hj = 0.0;
    
    solution.setObjective(0, x[0] + hj + 2.0*(4.0*sum1 - 2.0*prod1 + 2.0) / (double)count1);
    solution.setObjective(1, 1.0 - x[0] + hj + 2.0*(4.0*sum2 - 2.0*prod2 + 2.0) / (double)count2);
  } // evaluate

  @Override
  public List<T> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} // CEC2009_UF6
