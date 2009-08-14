/**
 * LZ06_F1.java
*
* @author Antonio J. Nebro
* @author Juan J. Durillo
* @version 1.0
*/

package jmetal.problems.LZ06;

import jmetal.base.DecisionVariables;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.Configuration.SolutionType_;
import jmetal.base.Configuration.VariableType_;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

/** 
* Class representing problem LZ06_F2
*/
public class LZ06_F2<V extends IReal>  extends Problem<V> {
  
 private static final long serialVersionUID = 5825380420784925762L;

/** 
  * Constructor
  * Creates a default instance of the LZ06_F2 problem
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
 public LZ06_F2(String solutionType) {
   this(30, solutionType); // 30 variables by default
 }
 /** 
  * Constructor
  * Creates a default instance of the ZZJ07_F1 problem
  * @param numberOfVariables Number of variables.
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
 public LZ06_F2(Integer numberOfVariables, String solutionType) {
   numberOfVariables_   = numberOfVariables.intValue() ;
   numberOfObjectives_  = 2;
   numberOfConstraints_ = 0;
   problemName_         = "LZ06_F2";
       
   upperLimit_ = new double[numberOfVariables_];
   lowerLimit_ = new double[numberOfVariables_];
   for (int var = 0; var < numberOfVariables_; var++){
     lowerLimit_[var] = 0.0;
     upperLimit_[var] = 1.0;
   } // for
       
   solutionType_ = Enum.valueOf(SolutionType_.class, solutionType) ; 
   
   // All the variables are of the same type, so the solutionType name is the
   // same than the variableType name
   variableType_ = new VariableType_[numberOfVariables_];
   for (int var = 0; var < numberOfVariables_; var++){
     variableType_[var] = Enum.valueOf(VariableType_.class, solutionType) ;    
   } // for
 } //LZ06_F2
   
 /** 
 * Evaluates a solution 
 * @param solution The solution to evaluate
  * @throws JMException 
 */        
 public void evaluate(Solution<V> solution) throws JMException {
   DecisionVariables<V> decisionVariables  = solution.getDecisionVariables();
   
   double [] x  = new double[numberOfVariables_] ; 
   double [] fx = new double[numberOfVariables_] ; 
   double g   ;
   double h   ;
   double sum ;
   for (int i = 0; i < numberOfVariables_; i++)
     x[i] = decisionVariables.variables_.get(i).getValue() ;
   
   fx[0] = Math.sqrt(x[0]) ;

   sum = 0.0 ;
   for (int i = 1; i < numberOfVariables_; i++)
  	 sum += Math.abs(x[0]-Math.sin(0.5*x[i]*Math.PI));

   g = 1.0 + 1.0*sum/(numberOfVariables_-1.0);
   h = 1.0 - Math.pow(x[0]/g,2.0) ;
  
   fx[1] = h ;
   
   solution.setObjective(0,fx[0]);
   solution.setObjective(1,fx[1]);
 } // evaluate
} // LZ06_F2
