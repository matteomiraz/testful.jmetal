/**
 * Griewank.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.problems.singleObjective;

import java.util.List;

import jmetal.base.DecisionVariables;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

public class Griewank<T extends IReal> extends Problem<T> {
  private static final long serialVersionUID = -7795959989040731929L;

  private final Class<T> solutionType_;
  
	/** 
   * Constructor
   * Creates a default instance of the Griewank problem
   * @param numberOfVariables Number of variables of the problem 
   * @param solutionType The solution type must "Real" or "BinaryReal". 
   */
  public Griewank(Integer numberOfVariables, Class<T> solutionType) {
    numberOfVariables_   = numberOfVariables;
    numberOfObjectives_  = 1;
    numberOfConstraints_ = 0;
    problemName_         = "Sphere";
        
    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] = -600.0;
      upperLimit_[var] = 600.0;
    } // for

    solutionType_ = solutionType; 
  } // Griewank
    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */        
  public void evaluate(Solution<T> solution) throws JMException {
    DecisionVariables<T> decisionVariables  = solution.getDecisionVariables();

    double sum  = 0.0    ;
    double mult = 0.0    ;
    double d    = 4000.0 ;
    for (int var = 0; var < numberOfVariables_; var++) {
      sum += decisionVariables.variables_.get(var).getValue() * 
             decisionVariables.variables_.get(var).getValue() / d ;    
      mult *= Math.cos(decisionVariables.variables_.get(var).getValue()/Math.sqrt(var)) ;    
    }        


    solution.setObjective(0, 1.0 + sum - mult) ;
  } // evaluate

  @Override
  public List<T> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} // Griewank

