/**
 * Poloni.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.problems;

import java.util.List;

import jmetal.base.DecisionVariables;
import jmetal.base.ProblemValue;
import jmetal.base.Solution;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

/**
 * Class representing problem Poloni. This problem has two objectives to be
 * MAXIMIZED. As jMetal always minimizes, the rule Max(f(x)) = -Min(f(-x)) must
 * be applied.
 */
public class Poloni<T extends IReal> extends ProblemValue<T> {    
    
 private static final long serialVersionUID = -8968373064444128659L;

 private final Class<T> solutionType_;

/**
  * Constructor.
  * Creates a default instance of the Poloni problem
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public Poloni(Class<T> solutionType) {
    numberOfVariables_  = 2;
    numberOfObjectives_ = 2;
    numberOfConstraints_= 0;
    problemName_        = "Poloni";

    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];        
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] = -1* Math.PI;
      upperLimit_[var] =  Math.PI;
    } //for

    solutionType_ = solutionType; 
  } //Poloni
    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */
  public void evaluate(Solution<T> solution) throws JMException {
    final double A1 = 0.5 * Math.sin(1.0) - 2 * Math.cos(1.0) + 
                      Math.sin(2.0) - 1.5 * Math.cos(2.0) ; //!< Constant A1
    final double A2 = 1.5 * Math.sin(1.0) - Math.cos(1.0) + 
                      2 * Math.sin(2.0) - 0.5 * Math.cos(2.0) ; //!< Constant A2
    
    DecisionVariables<T> decisionVariables  = solution.getDecisionVariables();
    
    double [] x = new double[numberOfVariables_] ;
    double [] f = new double[numberOfObjectives_];
    
    x[0] = decisionVariables.variables_.get(0).getValue();
    x[1] = decisionVariables.variables_.get(1).getValue();        
    
    double B1 = 0.5 * Math.sin(x[0]) - 2 * Math.cos(x[0]) + Math.sin(x[1]) - 
                1.5 * Math.cos(x[1]) ;
    double B2 = 1.5 * Math.sin(x[0]) - Math.cos(x[0]) + 2 * Math.sin(x[1]) - 
                0.5 * Math.cos(x[1]) ;
    
    f[0] = - (1 + Math.pow(A1 - B1, 2) + Math.pow(A2 - B2, 2)) ;
    f[1] = -(Math.pow(x[0]+3,2) + Math.pow(x[1]+1,2)) ;
      
    // The two objectives to be minimized. According to Max(f(x)) = -Min(f(-x)), 
    // they must be multiplied by -1. Consequently, the obtained solutions must
    // be also multiplied by -1 
    
    solution.setObjective(0,-1 * f[0]);
    solution.setObjective(1,-1 * f[1]);
  } // evaluate

  @Override
  public List<T> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} // Poloni

