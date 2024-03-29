/**
 * Golinski.java
 *
 * @author Antonio J. Nebro
 * @author Juanjo Durillo
 * @version 1.0
 */
package jmetal.problems;

import java.util.List;

import jmetal.base.ProblemValue;
import jmetal.base.Solution;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

/** 
 * Class representing problem Golinski.
 */
public class Golinski<T extends IReal> extends ProblemValue<T>{

  private static final long serialVersionUID = 7529477649719646295L;
	// defining lowerLimits and upperLimits for the problem
  public static final double [] LOWERLIMIT = {2.6, 0.7, 17.0, 7.3, 7.3, 2.9, 5.0};
  public static final double [] UPPERLIMIT = {3.6, 0.8, 28.0, 8.3, 8.3, 3.9, 5.5};                          

  private final Class<T> solutionType_;

 /** 
  * Constructor.
  * Creates a defalut instance of the Golinski problem.
  * @param solutionType The solution type must "Real" or "BinaryReal". 
  */
  public Golinski(Class<T> solutionType) {
    numberOfVariables_   = 7 ;
    numberOfObjectives_  = 2 ;
    numberOfConstraints_ = 11;
    problemName_         = "Golinski";
        
    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] =  LOWERLIMIT[var];
      upperLimit_[var] =  UPPERLIMIT[var];
    } // for
        
    solutionType_ = solutionType; 
  } //Golinski
  
  /**
   * Evaluates a solution.
   * @param solution The solution to evaluate.
   * @throws JMException 
   */
  public void evaluate(Solution<T> solution) throws JMException {         
    double x1,x2,x3,x4,x5,x6,x7;
    x1 = solution.getDecisionVariables().variables_.get(0).getValue();
    x2 = solution.getDecisionVariables().variables_.get(1).getValue();
    x3 = solution.getDecisionVariables().variables_.get(2).getValue();
    x4 = solution.getDecisionVariables().variables_.get(3).getValue();
    x5 = solution.getDecisionVariables().variables_.get(4).getValue();
    x6 = solution.getDecisionVariables().variables_.get(5).getValue();
    x7 = solution.getDecisionVariables().variables_.get(6).getValue();
        
    double f1 = 0.7854 * x1 *x2 *x2 * ((10*x3*x3)/3.0 + 14.933*x3 - 43.0934) - 
                1.508*x1*(x6*x6 + x7*x7)+7.477*(x6*x6*x6 + x7*x7*x7) + 
                0.7854*(x4*x6*x6 + x5*x7*x7);        
    
    double aux = 745.0 * x4 / (x2 * x3);
    double f2 = Math.sqrt((aux*aux)+1.69e7) / (0.1*x6*x6*x6);
    
    solution.setObjective(0,f1);    
    solution.setObjective(1,f2);
  } // evaluate
  
  /** 
   * Evaluates the constraint overhead of a solution 
   * @param solution The solution
   * @throws JMException 
   */  
 public void evaluateConstraints(Solution<T> solution) throws JMException {
    double [] constraint = new double[numberOfConstraints_];
    double x1,x2,x3,x4,x5,x6,x7;
        
    x1 = solution.getDecisionVariables().variables_.get(0).getValue();
    x2 = solution.getDecisionVariables().variables_.get(1).getValue();
    x3 = solution.getDecisionVariables().variables_.get(2).getValue();
    x4 = solution.getDecisionVariables().variables_.get(3).getValue();
    x5 = solution.getDecisionVariables().variables_.get(4).getValue();
    x6 = solution.getDecisionVariables().variables_.get(5).getValue();
    x7 = solution.getDecisionVariables().variables_.get(6).getValue();
        
        
    constraint[0] = -((1.0/(x1*x2*x2*x3)) -(1.0/27.0));
    constraint[1] = -((1.0/(x1*x2*x2*x3*x3))-(1.0/397.5));
    constraint[2] = -((x4*x4*x4)/(x2*x3*x3*x6*x6*x6*x6)-(1.0/1.93));
    constraint[3] = -((x5*x5*x5)/(x2*x3*x7*x7*x7*x7)- (1.0/1.93));
    constraint[4] = -(x2*x3 - 40.0);
    constraint[5] = -((x1/x2) - 12.0);
    constraint[6] = -(5.0 - (x1/x2));
    constraint[7] = -(1.9 - x4 + 1.5*x6);
    constraint[8] = -(1.9 - x5 + 1.1*x7);
    
    double aux = 745.0 * x4 / (x2 * x3);
    double f2 = java.lang.Math.sqrt((aux*aux)+1.69e7) / (0.1*x6*x6*x6);
    constraint[9] = -(f2 - 1300);
    double a = 745.0*x5/(x2*x3);
    double b = 1.575e8;
    constraint[10] = -(java.lang.Math.sqrt(a*a+b)/(0.1*x7*x7*x7)-1100.0);

    double total = 0.0;
    int number = 0;
    for (int i = 0; i < numberOfConstraints_; i++) {
      if (constraint[i]<0.0){
        total+=constraint[i];
        number++;
      }
    }
        
    solution.setOverallConstraintViolation(total);    
    solution.setNumberOfViolatedConstraint(number);        
  } // evaluateConstraints

 @Override
 public List<T> generateNewDecisionVariable() {
 	return generate(solutionType_);
 }
} // Golinski

