/**
 * DTLZ6.java
 *
 * @author Antonio J. Nebro
 * @author Juanjo Durillo
 * @version 1.0
 * Created on 16 de octubre de 2006, 17:30
 */
package jmetal.problems.DTLZ;

import java.util.List;

import jmetal.base.DecisionVariables;
import jmetal.base.ProblemValue;
import jmetal.base.Solution;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

/**
 * Class representing problem DTLZ6
 */
 public class DTLZ6<V extends IReal>  extends ProblemValue<V>{
  
 private static final long serialVersionUID = 5723311960534086239L;

 private final Class<V> solutionType_;

/**
  * Creates a default DTLZ6 problem instance (12 variables and 3 objectives)
  * @param solutionType The solution type must "Real" or "BinaryReal". 
  */
  public DTLZ6(Class<V> solutionType){
    this(12,3,solutionType);
  } // DTLZ6

 /**
  * Creates a new DTLZ6 problem instance
  * @param numberOfVariables Number of variables
  * @param numberOfObjectives Number of objective functions
  * @param solutionType The solution type must "Real" or "BinaryReal". 
  */
  public DTLZ6(Integer numberOfVariables, 
  		         Integer numberOfObjectives, 
  		         Class<V>  solutionType) {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ = numberOfObjectives.intValue();                              
    numberOfConstraints_= 0;
    problemName_        = "DTLZ6";
        
    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    } //for
        
    solutionType_ = solutionType; 
  } // DTLZ6
    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */      
  public void evaluate(Solution<V> solution) throws JMException {
    DecisionVariables<V> gen  = solution.getDecisionVariables();

    double [] x = new double[numberOfVariables_];
    double [] f = new double[numberOfObjectives_];
    double [] theta = new double[numberOfObjectives_-1];
    int k = numberOfVariables_ - numberOfObjectives_ + 1;
        
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = gen.variables_.get(i).getValue();
        
    double g = 0.0;
    for (int i = numberOfVariables_ - k; i < numberOfVariables_; i++)
      g += java.lang.Math.pow(x[i],0.1);
        
    double t = java.lang.Math.PI  / (4.0 * (1.0 + g));
    theta[0] = x[0] * java.lang.Math.PI / 2;  
    for (int i = 1; i < (numberOfObjectives_-1); i++) 
      theta[i] = t * (1.0 + 2.0 * g * x[i]);			
        
    for (int i = 0; i < numberOfObjectives_; i++)
      f[i] = 1.0 + g;
        
    for (int i = 0; i < numberOfObjectives_; i++){
      for (int j = 0; j < numberOfObjectives_ - (i + 1); j++)            
        f[i] *= java.lang.Math.cos(theta[j]);                
        if (i != 0){
          int aux = numberOfObjectives_ - (i + 1);
          f[i] *= java.lang.Math.sin(theta[aux]);
        } //if
    } // for

    for (int i = 0; i < numberOfObjectives_; i++)
      solution.setObjective(i,f[i]);        
  } // evaluate

  @Override
  public List<V> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
}

