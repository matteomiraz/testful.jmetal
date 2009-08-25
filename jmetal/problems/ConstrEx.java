/**
 * ConstrEx.java
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
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
 * Class representing problem Constr_Ex
 */
public class ConstrEx<T extends IReal> extends ProblemValue<T> {
  private static final long serialVersionUID = 810989642325732952L;

  private final Class<T> solutionType_;

	/**
   * Constructor
   * Creates a default instance of the Constr_Ex problem
   * @param solutionType The solution type must "Real" or "BinaryReal".
   */
  public ConstrEx(Class<T> solutionType) {
    numberOfVariables_  = 2;
    numberOfObjectives_ = 2;
    numberOfConstraints_= 2;
    problemName_        = "Constr_Ex";
        
    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];        
    lowerLimit_[0] = 0.1;
    lowerLimit_[1] = 0.0;        
    upperLimit_[0] = 1.0;
    upperLimit_[1] = 5.0;
        
    solutionType_ = solutionType; 
  } // ConstrEx
     
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */
  public void evaluate(Solution<T> solution) throws JMException {
    DecisionVariables<T> decisionVariables  = solution.getDecisionVariables();
       
    double [] f = new double[numberOfObjectives_];
    f[0] = decisionVariables.variables_.get(0).getValue();        
    f[1] = (1.0 + decisionVariables.variables_.get(1).getValue())/
                  decisionVariables.variables_.get(0).getValue();        
    
    solution.setObjective(0,f[0]);
    solution.setObjective(1,f[1]);
  } // evaluate

 /** 
  * Evaluates the constraint overhead of a solution 
  * @param solution The solution
 * @throws JMException 
  */  
  public void evaluateConstraints(Solution<T> solution) throws JMException {
    double [] constraint = new double[this.getNumberOfConstraints()];

    double x1 = solution.getDecisionVariables().variables_.get(0).getValue();
    double x2 = solution.getDecisionVariables().variables_.get(1).getValue();
        
    constraint[0] =  (x2 + 9*x1 -6.0) ;
    constraint[1] =  (-x2 + 9*x1 -1.0);
        
    double total = 0.0;
    int number = 0;
    for (int i = 0; i < this.getNumberOfConstraints(); i++)
      if (constraint[i]<0.0){
        total+=constraint[i];
        number++;
      }
        
    solution.setOverallConstraintViolation(total);    
    solution.setNumberOfViolatedConstraint(number);         
  } // evaluateConstraints  

  @Override
  public List<T> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} // ConstrEx