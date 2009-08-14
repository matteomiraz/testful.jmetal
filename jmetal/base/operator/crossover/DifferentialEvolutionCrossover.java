/**
 * DifferentialEvolutionCrossover.java
 * Class representing the crossover operator used in differential evolution
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.base.operator.crossover;

import jmetal.base.Solution;
import jmetal.base.variable.Real;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

public class DifferentialEvolutionCrossover extends DifferentialCrossover<Real> {
  private static final long serialVersionUID = -3210629965576501068L;

	/**
   * DEFAULT_CR defines a default CR (crossover operation control) value
   */
  public static final double DEFAULT_CR = 0.1; 
  
  /**
   * DEFAULT_F defines the default F (Scaling factor for mutation) value
   */
  private static final double DEFAULT_F = 0.5;
  
  private double CR_ ;
  private double F_  ;
  
  /**
   * Constructor
   */
  DifferentialEvolutionCrossover() {
    CR_ = DEFAULT_CR ;
    F_  = DEFAULT_F  ;
  } // Constructor
  
  
	public void setF(double f) {
		F_ = f;
	}
	
	
	public void setCR(double cR) {
		CR_ = cR;
	}
  
  /**
   * Executes the operation
   * @param object An object containing an array of three parents
   * @return An object containing the offSprings
   */
  @Override
  public Solution<Real> execute(Solution<Real> current, Solution<Real> [] parent) throws JMException {
     Solution<Real> child ;
     
     int jrand ;

     int numberOfVariables = parent[0].getDecisionVariables().variables_.size() ;
     jrand = (int)(PseudoRandom.randInt(0, numberOfVariables - 1)) ;
     
     child = new Solution<Real>(current) ;
     for (int j=0; j < numberOfVariables; j++) {
        if (PseudoRandom.randDouble(0, 1) < CR_ || j == jrand) {
          double value ;
          value = parent[2].getDecisionVariables().variables_.get(j).getValue()  +
                  F_ * (parent[0].getDecisionVariables().variables_.get(j).getValue() -
                       parent[1].getDecisionVariables().variables_.get(j).getValue()) ;
          
          if (value < child.getDecisionVariables().variables_.get(j).getLowerBound())
            value =  child.getDecisionVariables().variables_.get(j).getLowerBound() ;
          if (value > child.getDecisionVariables().variables_.get(j).getUpperBound())
            value = child.getDecisionVariables().variables_.get(j).getUpperBound() ;
            
          child.getDecisionVariables().variables_.get(j).setValue(value) ;
        }
        else {
          double value ;
          value = current.getDecisionVariables().variables_.get(j).getValue();
          child.getDecisionVariables().variables_.get(j).setValue(value) ;
        } // else
     }
     
     return child;
   }
}
