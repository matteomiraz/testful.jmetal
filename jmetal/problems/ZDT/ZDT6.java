/**
 * ZDT6.java
 *
 * @author Antonio J. Nebro
 * @author Juanjo Durillo
 * @version 1.0
 */
package jmetal.problems.ZDT;

import java.util.List;

import jmetal.base.DecisionVariables;
import jmetal.base.ProblemValue;
import jmetal.base.Solution;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

/**
 * Class representing problem ZDT6
 */
public class ZDT6<V extends IReal>  extends ProblemValue<V> {
    
 private static final long serialVersionUID = 6380134519371621098L;

 private final Class<V> solutionType_;

 /**
  * Creates a default instance of problem ZDT6 (10 decision variables)
  * @param representation The solution type must "Real" or "BinaryReal".
  */
  public ZDT6(Class<V> representation) {
    this(10,representation); // 10 variables by default
  } // ZDT6
  
 /**
  * Creates a instance of problem ZDT6
  * @param numberOfVariables Number of variables
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public ZDT6(Integer numberOfVariables, Class<V> solutionType) {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ = 2;
    numberOfConstraints_= 0;
    problemName_        = "ZDT6";
        
    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];
        
    for (int var = 0; var < numberOfVariables_; var++) {
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    } //for
        
    solutionType_ = solutionType; 
  } //ZDT6
  
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */    
  public void evaluate(Solution<V> solution) throws JMException {
    DecisionVariables<V> decisionVariables  = solution.getDecisionVariables();
        
    double x1   = decisionVariables.variables_.get(0).getValue()       ;
    double [] f = new double[numberOfObjectives_]   ;
    f[0]        = 1.0 - Math.exp((-4.0)*x1) * Math.pow(Math.sin(6.0*Math.PI*x1),6.0);
    double g    = this.evalG(decisionVariables)                   ;
    double h    = this.evalH(f[0],g)                ;
    f[1]        = h * g                             ;
    
    solution.setObjective(0,f[0]);
    solution.setObjective(1,f[1]);    
  } //evaluate
    
  /**
  * Returns the value of the ZDT6 function G.
  * @param decisionVariables The decision variables of the solution to 
  * evaluate.
   * @throws JMException 
  */
  public double evalG(DecisionVariables<V> decisionVariables) throws JMException{
    double g = 0.0;
    for (int var = 1; var < this.numberOfVariables_; var++)
      g += decisionVariables.variables_.get(var).getValue();
    g = g / (numberOfVariables_ - 1);
    g = java.lang.Math.pow(g,0.25);
    g = 9.0 * g;
    g = 1.0 + g;        
    return g;
  } // evalG
  
  /**
  * Returns the value of the ZDT6 function H.
  * @param f First argument of the function H.
  * @param g Second argument of the function H.
  */
  public double evalH(double f, double g){
    return 1.0 - Math.pow((f/g),2.0);
  } // evalH       

  @Override
  public List<V> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
} //ZDT6
