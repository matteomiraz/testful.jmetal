/**
 * ZDT1.java
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.problems.ZDT;

import java.util.List;

import jmetal.base.DecisionVariables;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

/**
 * Class representing problem ZDT1
 */
public class ZDT1<T extends IReal> extends Problem<T> {
    
 private static final long serialVersionUID = 6680347915034255781L;

 private final Class<T> solutionType_;

/** 
  * Constructor.
  * Creates a default instance of problem ZDT1 (30 decision variables)
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public ZDT1(Class<T> solutionType) {
    this(30, solutionType); // 30 variables by default
  } // ZDT1
  
 /**
  * Creates a new instance of problem ZDT1.
  * @param numberOfVariables Number of variables.
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public ZDT1(Integer numberOfVariables, Class<T> solutionType) {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ =  2;
    numberOfConstraints_=  0;
    problemName_        = "ZDT1";

    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];

    // Stablishes upper and lower limits for the variables
    for (int var = 0; var < numberOfVariables_; var++)
    {
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    } // for

    solutionType_ = solutionType; 
  } // ZDT1
    
  /** 
   * Evaluates a solution.
   * @param solution The solution to evaluate.
   * @throws JMException 
   */
  public void evaluate(Solution<T> solution) throws JMException {
    DecisionVariables<T> decisionVariables  = solution.getDecisionVariables();
    
    double [] f = new double[numberOfObjectives_]  ;
    f[0]        = decisionVariables.variables_.get(0).getValue()     ;
    double g    = this.evalG(decisionVariables)                 ;
    double h    = this.evalH(f[0],g)              ;
    f[1]        = h * g                           ;
    
    solution.setObjective(0,f[0]);
    solution.setObjective(1,f[1]);
  } // evaluate
    
  /**
   * Returns the value of the ZDT1 function G.
   * @param decisionVariables The decision variables of the solution to 
   * evaluate.
   * @throws JMException 
   */
  private double evalG(DecisionVariables<T> decisionVariables) throws JMException {
    double g = 0.0;        
    for (int i = 1; i < decisionVariables.size();i++)
      g += decisionVariables.variables_.get(i).getValue();
    double constante = (9.0 / (numberOfVariables_-1));
    g = constante * g;
    g = g + 1.0;
    return g;
  } // evalG
    
  /**
   * Returns the value of the ZDT1 function H.
   * @param f First argument of the function H.
   * @param g Second argument of the function H.
   */
  public double evalH(double f, double g) {
    double h = 0.0;
    h = 1.0 - java.lang.Math.sqrt(f/g);
    return h;        
  } // evalH

  @Override
  public List<T> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} // ZDT1
