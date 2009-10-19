/**
 * LZ07_F4.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * Created on 17 de junio de 2006, 17:30
 */

package jmetal.problems.LZ09;

import java.util.List;
import java.util.Vector;

import jmetal.base.DecisionVariables;
import jmetal.base.ProblemValue;
import jmetal.base.Solution;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

/** 
 * Class representing problem DTLZ1 
 */
public class LZ09_F4<V extends IReal>  extends ProblemValue<V> {   
	private static final long serialVersionUID = -8688542912459476031L;
  private final Class<V> solutionType_;

	LZ09 lz07_ ; 
 /** 
  * Creates a default DTLZ1 problem (7 variables and 3 objectives)
  * @param solutionType The solution type must "Real" or "BinaryReal". 
  */
  public LZ09_F4(Class<V> solutionType){
    this(21, 1, 24, solutionType);
  } // LZ07_F4
  
  /** 
   * Creates a DTLZ1 problem instance
   * @param numberOfVariables Number of variables
   * @param numberOfObjectives Number of objective functions
   * @param solutionType The solution type must "Real" or "BinaryReal". 
   */
   public LZ09_F4(Integer ptype, 
   		            Integer dtype,
   		            Integer ltype,
   		            Class<V> solutionType) {
     numberOfVariables_  = 30;
     numberOfObjectives_ = 2;
     numberOfConstraints_= 0;
     problemName_        = "LZ07_F4";
         
   	 lz07_  = new LZ09(numberOfVariables_, 
   			               numberOfObjectives_, 
   			               ptype, 
   			               dtype, 
   			               ltype) ;

     lowerLimit_ = new double[numberOfVariables_];
     upperLimit_ = new double[numberOfVariables_];      
     lowerLimit_[0] = 0.0 ;
     upperLimit_[0] = 1.0 ;
     for (int var = 1; var < numberOfVariables_; var++){
       lowerLimit_[var] = -1.0;
       upperLimit_[var] = 1.0;
     } //for
         
     solutionType_ = solutionType; 
   } // LZ07_F4
   
   /** 
    * Evaluates a solution 
    * @param solution The solution to evaluate
     * @throws JMException 
    */    
    public void evaluate(Solution<V> solution) throws JMException {
      DecisionVariables<V> gen  = solution.getDecisionVariables();
      
      Vector<Double> x = new Vector<Double>(numberOfVariables_) ;
      Vector<Double> y = new Vector<Double>(numberOfObjectives_);
      @SuppressWarnings("unused")
			int k = numberOfVariables_ - numberOfObjectives_ + 1;
          
      for (int i = 0; i < numberOfVariables_; i++) {
      	x.addElement(gen.variables_.get(i).getValue());
      	y.addElement(0.0) ;
      } // for
        
      lz07_.objective(x, y) ;
      
      for (int i = 0; i < numberOfObjectives_; i++)
        solution.setObjective(i, y.get(i)); 
    } // evaluate

    @Override
    public List<V> generateNewDecisionVariable() {
    	return generate(solutionType_);
    }
} // LZ07_F4
