/**
 * Srinivas.java
 *
 * @author Antonio J. Nebro
 * @author Juanjo Durillo
 * @version 1.0
 */
package jmetal.problems;

import java.util.List;

import jmetal.base.DecisionVariables;
import jmetal.base.ProblemValue;
import jmetal.base.Solution;
import jmetal.base.VariableValue;
import jmetal.util.JMException;

/**
 * Class representing problem Srinivas
 */
public class Srinivas<T extends VariableValue> extends ProblemValue<T> {    
    
 private static final long serialVersionUID = 2570357706582532681L;

 private final Class<T> solutionType_;

 /**
  * Constructor.
  * Creates a default instance of the Srinivas problem
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public Srinivas(Class<T> solutionType) {
    numberOfVariables_  = 2;
    numberOfObjectives_ = 2;
    numberOfConstraints_= 2;
    problemName_        = "Srinivas";

    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];        
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] = -20.0;
      upperLimit_[var] =  20.0;
    } //for
        
    solutionType_ = solutionType; 
  } //Srinivas
    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */
  public void evaluate(Solution<T> solution) throws JMException {
    DecisionVariables<T> decisionVariables  = solution.getDecisionVariables();
    
    double [] f = new double[numberOfObjectives_];
    
    double x1 = decisionVariables.variables_.get(0).getValue();
    double x2 = decisionVariables.variables_.get(1).getValue();        
    f[0] = 2.0 + (x1-2.0)*(x1-2.0) + (x2-1.0)*(x2-1.0);                        
    f[1] = 9.0 * x1 - (x2-1.0)*(x2-1.0);        
        
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
        
    constraint[0] = 1.0 - (x1*x1 + x2*x2)/225.0;
    constraint[1] = (3.0*x2 - x1)/10.0 - 1.0;
        
    double total = 0.0;
    int number = 0;
    for (int i = 0; i < this.getNumberOfConstraints(); i++)
      if (constraint[i]<0.0){
        number++;
        total+=constraint[i];
      }
        
    solution.setOverallConstraintViolation(total);    
    solution.setNumberOfViolatedConstraint(number);
  } // evaluateConstraints

  @Override
  public List<T> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} // Srinivas
