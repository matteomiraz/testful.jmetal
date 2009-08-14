/**
 * Sphere.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.problems.singleObjective;

import java.util.List;

import jmetal.base.DecisionVariables;
import jmetal.base.ProblemValue;
import jmetal.base.Solution;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

public class Sphere<V extends IReal>  extends ProblemValue<V> {
  private static final long serialVersionUID = -5021029656925462284L;

  private final Class<V> solutionType_;

	/** 
   * Constructor
   * Creates a default instance of the Sphere problem
   * @param numberOfVariables Number of variables of the problem 
   * @param solutionType The solution type must "Real" or "BinaryReal". 
   */
  public Sphere(Integer numberOfVariables, Class<V> solutionType) {
    numberOfVariables_   = numberOfVariables ;
    numberOfObjectives_  = 1;
    numberOfConstraints_ = 0;
    problemName_         = "Sphere";
        
    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] = -5.12;
      upperLimit_[var] = 5.12;
    } // for
        
    solutionType_ = solutionType; 
  } // Sphere
    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */        
  public void evaluate(Solution<V> solution) throws JMException {
    DecisionVariables<V> decisionVariables  = solution.getDecisionVariables();

    double sum = 0.0;
    for (int var = 0; var < numberOfVariables_; var++) {
      sum += StrictMath.pow(decisionVariables.variables_.get(var).getValue(), 2.0);      
    }        
    solution.setObjective(0, sum);
  } // evaluate

  @Override
  public List<V> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} // Sphere

