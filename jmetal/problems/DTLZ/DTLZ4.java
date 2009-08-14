/**
 * DTLZ4.java
 *
 * @author Antonio J. Nebro
 * @author Juanjo Durillo
 * @version 1.0
 * Created on 16 de octubre de 2006, 17:30
 */
package jmetal.problems.DTLZ;

import jmetal.base.DecisionVariables;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.Configuration.SolutionType_;
import jmetal.base.Configuration.VariableType_;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

/**
 * Class representing problem DTLZ4
 */
public class DTLZ4<V extends IReal>  extends Problem<V>{
   
  
 private static final long serialVersionUID = -4898314946109270420L;

/**
  * Creates a default DTLZ4 problem (12 variables and 3 objectives) 
  * @param solutionType The solution type must "Real" or "BinaryReal". 
  */
  public DTLZ4(String solutionType){
    this(12,3,solutionType);
  }
    
 /**
  * Creates a DTLZ4 problem problem instance 
  * @param numberOfVariables Number of variables
  * @param numberOfObjectives Number of objective functions
  * @param solutionType The solution type must "Real" or "BinaryReal". 
  */
  public DTLZ4(Integer numberOfVariables, 
               Integer numberOfObjectives, 
               String solutionType) {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ = numberOfObjectives.intValue();
    numberOfConstraints_= 0;
    problemName_        = "DTLZ4";
        
    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];        
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    }
        
    solutionType_ = Enum.valueOf(SolutionType_.class, solutionType) ; 
    
    // All the variables are of the same type, so the solutionType name is the
    // same than the variableType name
    variableType_ = new VariableType_[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++){
      variableType_[var] = Enum.valueOf(VariableType_.class, solutionType) ;    
    } // for
  } //DTLZ4         

  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */      
  public void evaluate(Solution<V> solution) throws JMException {
    DecisionVariables<V> gen  = solution.getDecisionVariables();
  
    double [] x = new double[numberOfVariables_];
    double [] f = new double[numberOfObjectives_];
    double alpha = 100.0;
    int k = numberOfVariables_ - numberOfObjectives_ + 1;
  
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = gen.variables_.get(i).getValue();

    double g = 0.0;
    for (int i = numberOfVariables_ - k; i < numberOfVariables_; i++)
      g += (x[i] - 0.5)*(x[i] - 0.5);                
        
    for (int i = 0; i < numberOfObjectives_; i++)
      f[i] = 1.0 + g;
        
    for (int i = 0; i < numberOfObjectives_; i++) {
      for (int j = 0; j < numberOfObjectives_ - (i + 1); j++)            
        f[i] *= java.lang.Math.cos(java.lang.Math.pow(x[j],alpha)*(java.lang.Math.PI/2.0));                
        if (i != 0){
          int aux = numberOfObjectives_ - (i + 1);
          f[i] *= java.lang.Math.sin(java.lang.Math.pow(x[aux],alpha)*(java.lang.Math.PI/2.0));
        } //if
    } // for
        
    for (int i = 0; i < numberOfObjectives_; i++)
      solution.setObjective(i,f[i]);                
  } // evaluate 
}

