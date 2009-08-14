/**
 * LZ07_F6.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * Created on 17 de junio de 2006, 17:30
 */

package jmetal.problems.LZ07;

import java.util.List;
import java.util.Vector;

import jmetal.base.DecisionVariables;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

/** 
 * Class representing problem DTLZ1 
 */
public class LZ07_F6<V extends IReal>  extends Problem<V> {   
	private static final long serialVersionUID = 7225210732452769143L;
  private final Class<V> solutionType_;

	LZ07 lz07_ ; 
 /** 
  * Creates a default DTLZ1 problem (7 variables and 3 objectives)
  * @param solutionType The solution type must "Real" or "BinaryReal". 
  */
  public LZ07_F6(Class<V> solutionType){
    this(31, 1, 32, solutionType);
  } // LZ07_F6
  
  /** 
   * Creates a DTLZ1 problem instance
   * @param numberOfVariables Number of variables
   * @param numberOfObjectives Number of objective functions
   * @param solutionType The solution type must "Real" or "BinaryReal". 
   */
   public LZ07_F6(Integer ptype, 
   		            Integer dtype,
   		            Integer ltype,
   		            Class<V> solutionType) {
     numberOfVariables_  = 10;
     numberOfObjectives_ = 3;
     numberOfConstraints_= 0;
     problemName_        = "LZ07_F6";
         
   	 lz07_  = new LZ07(numberOfVariables_, 
   			               numberOfObjectives_, 
   			               ptype, 
   			               dtype, 
   			               ltype) ;

     lowerLimit_ = new double[numberOfVariables_];
     upperLimit_ = new double[numberOfVariables_];      
     lowerLimit_[0] = 0.0 ;
     upperLimit_[0] = 1.0 ;     
     lowerLimit_[1] = 0.0 ;
     upperLimit_[1] = 1.0 ;
     for (int var = 2; var < numberOfVariables_; var++){
       lowerLimit_[var] = -2.0;
       upperLimit_[var] = 2.0;
     } //for
         
     solutionType_ = solutionType; 
   } // LZ07_F6
   
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
} // LZ07_F6
