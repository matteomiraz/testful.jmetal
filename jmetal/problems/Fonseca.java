/**
 * Fonseca.java
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
 * Class representing problem Fonseca
 */
public class Fonseca<T extends IReal> extends ProblemValue<T> {
   
  private static final long serialVersionUID = -574100028358684089L;

  private final Class<T> solutionType_;

	/** 
   * Constructor
   * Creates a default instance of the Fonseca problem
   * @param solutionType The solution type must "Real" or "BinaryReal".
   */
  public Fonseca(Class<T> solutionType) {
    numberOfVariables_   = 3;
    numberOfObjectives_  = 2;
    numberOfConstraints_ = 0;
    problemName_         = "Fonseca";
        
    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] = -4.0;
      upperLimit_[var] = 4.0;
    } // for
        
    solutionType_ = solutionType; 
  } //Fonseca
    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */        
  public void evaluate(Solution<T> solution) throws JMException {
    DecisionVariables<T> decisionVariables  = solution.getDecisionVariables();

    double [] f = new double[numberOfObjectives_];
    double sum1 = 0.0;
    for (int var = 0; var < numberOfVariables_; var++){
      sum1 += StrictMath.pow(decisionVariables.variables_.get(var).getValue() 
              - (1.0/StrictMath.sqrt((double)numberOfVariables_)),2.0);            
    }
    double exp1 = StrictMath.exp((-1.0)*sum1);
    f[0] = 1 - exp1;
        
    double sum2 = 0.0;        
    for (int var = 0; var < numberOfVariables_; var++){
      sum2 += StrictMath.pow(decisionVariables.variables_.get(var).getValue() 
              + (1.0/StrictMath.sqrt((double)numberOfVariables_)),2.0);
    }    
    double exp2 = StrictMath.exp((-1.0)*sum2);
    f[1] = 1 - exp2;
        
    solution.setObjective(0,f[0]);
    solution.setObjective(1,f[1]);
  } // evaluate

  @Override
  public List<T> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} // Fonseca
