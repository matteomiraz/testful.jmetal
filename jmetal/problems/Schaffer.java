/**
 * Schaffer.java
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.problems;

import java.util.List;

import jmetal.base.DecisionVariables;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.VariableValue;
import jmetal.util.JMException;

/**
 * Class representing problem Schaffer
 */
public class Schaffer<T extends VariableValue> extends Problem<T> {    

 private static final long serialVersionUID = -8707134037543183769L;

 private final Class<T> solutionType_;

/**
  * Constructor.
  * Creates a default instance of the Schaffer problem
  * @param solutionType The solution type must "Real" or "BinaryReal".s 
  */
  public Schaffer(Class<T> solutionType) {
    numberOfVariables_  = 1;
    numberOfObjectives_ = 2;
    numberOfConstraints_ =0;
    problemName_         = "Schaffer";
        
    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];        
    lowerLimit_[0] = -1000;
    upperLimit_[0] =  1000;
    
    solutionType_ = solutionType; 

  } //Schaffer

    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */
  public void evaluate(Solution<T> solution) throws JMException {
    DecisionVariables<T> decisionVariables  = solution.getDecisionVariables();
    
    double [] f = new double[numberOfObjectives_];
    f[0] = decisionVariables.variables_.get(0).getValue() * 
           decisionVariables.variables_.get(0).getValue();
    
    f[1] = (decisionVariables.variables_.get(0).getValue() - 2.0) * 
           (decisionVariables.variables_.get(0).getValue() - 2.0);
        
    solution.setObjective(0,f[0]);
    solution.setObjective(1,f[1]);
  } //evaluate    

  @Override
  public List<T> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} //Schaffer
