/**
 * ZZJ07_F1.java
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.problems.ZZJ07;

import java.util.List;

import jmetal.base.DecisionVariables;
import jmetal.base.ProblemValue;
import jmetal.base.Solution;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

/** 
 * Class representing problem ZZJ07_F1
 */
public class ZZJ07_F1<V extends IReal>  extends ProblemValue<V> {
   
  private static final long serialVersionUID = 1105894363864045750L;

  private final Class<V> solutionType_;

  /** 
   * Constructor
   * Creates a default instance of the ZZJ07_F1 problem
   * @param solutionType The solution type must "Real" or "BinaryReal".
   */
  public ZZJ07_F1(Class<V> solutionType) {
    this(30, solutionType); // 30 variables by default
  }
  /** 
   * Constructor
   * Creates a default instance of the ZZJ07_F1 problem
   * @param numberOfVariables Number of variables.
   * @param solutionType The solution type must "Real" or "BinaryReal".
   */
  public ZZJ07_F1(Integer numberOfVariables, Class<V> solutionType) {
    numberOfVariables_   = numberOfVariables.intValue() ;
    numberOfObjectives_  = 2;
    numberOfConstraints_ = 0;
    problemName_         = "ZZJ07_F1";
        
    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    } // for
        
    solutionType_ = solutionType; 
  } //ZZJ07_F1
    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */        
  public void evaluate(Solution<V> solution) throws JMException {
    DecisionVariables<V> decisionVariables  = solution.getDecisionVariables();
    
    double [] x  = new double[numberOfVariables_]  ; 

    double g   ;
    double h   ;
    double sum ;
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = decisionVariables.variables_.get(i).getValue() ;
    
    sum = 0.0 ;
    for (int i = 0; i < numberOfVariables_; i++)
      sum += (x[i]-x[0])*(x[i]-x[0]); 
      
    g = 1.0 + 9.0*sum/(numberOfVariables_-1.0);
    h = 1.0 - Math.sqrt(x[0]/g);
    
    solution.setObjective(0,x[0]);
    solution.setObjective(1,g*h);
  } // evaluate

  @Override
  public List<V> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} // ZZJ07_F1
