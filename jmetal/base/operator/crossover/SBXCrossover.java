/**
 * SBXCrossover.java
 * Class representing a simulated binary (SBX) crossover operator
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.base.operator.crossover;

import jmetal.base.Configuration;
import jmetal.base.Solution;
import jmetal.base.Configuration.SolutionType_;
import jmetal.base.variable.Real;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class allows to apply a SBX crossover operator using two parent
 * solutions.
 * NOTE: the operator is applied to Real solutions, so the type of the solutions
 * must be </code>SolutionType_.Real</code>.
 * NOTE: if you use the default constructor, the value of the etc_c parameter is
 * DEFAULT_INDEX_CROSSOVER. You can change it using the parameter 
 * "distributionIndex" before invoking the execute() method -- see lines 196-199
 */
public class SBXCrossover extends Crossover<Real> {
    
  private static final long serialVersionUID = 485761542428572217L;

	/**
   * DEFAULT_INDEX_CROSSOVER defines a default index crossover
   */
  public static final double DEFAULT_INDEX_CROSSOVER = 20.0; 
  
  /**
   * EPS defines the minimum difference allowed between real values
   */
  private static final double EPS= 1.0e-14;
                                                                                      
    
  /**
   * eta_c stores the index for crossover to use
   */
  private double eta_c;
  //<-

  /** 
   * Constructor
   * Create a new SBX crossover operator whit a default
   * index given by <code>DEFAULT_INDEX_CROSSOVER</code>
   */
  public SBXCrossover() {
    eta_c = DEFAULT_INDEX_CROSSOVER;
  } // SBXCrossover
    
  /**
   * Constructor.
   * Create a new SBX crossover specifying a index crossover value
   * @param indexCrossover The index crossover
   */
  public SBXCrossover(double indexCrossover){
    eta_c = indexCrossover;
  }  // SBXCrossover  

  /**
   * Perform the crossover operation. 
   * @param probability Crossover probability
   * @param parent1 The first parent
   * @param parent2 The second parent
   * @return An array containing the two offsprings
   */
  @SuppressWarnings("unchecked")
	public Solution<Real>[] doCrossover(double probability, 
                                Solution<Real> parent1, 
                                Solution<Real> parent2) throws JMException {
    
    Solution<Real> [] offSpring = new Solution[2];

    offSpring[0] = new Solution<Real>(parent1);
    offSpring[1] = new Solution<Real>(parent2);
                    
    int i;
    double rand;
    double y1, y2, yL, yu;
    double c1, c2;
    double alpha, beta, betaq;
    double valueX1,valueX2;
    if (PseudoRandom.randDouble() <= probability){
      for (i=0; i<parent1.getDecisionVariables().size(); i++){
        valueX1 = parent1.getDecisionVariables().variables_.get(i).getValue();
        valueX2 = parent2.getDecisionVariables().variables_.get(i).getValue();
        if (PseudoRandom.randDouble()<=0.5 ){
          
          if (java.lang.Math.abs(valueX1- valueX2) > EPS){
            
            if (valueX1 < valueX2){
              y1 = valueX1;
              y2 = valueX2;
            } else {
              y1 = valueX2;
              y2 = valueX1;
            } // if                       
            
            yL = parent1.getDecisionVariables().variables_.get(i).getLowerBound();
            yu = parent1.getDecisionVariables().variables_.get(i).getUpperBound();
            rand = PseudoRandom.randDouble();
            beta = 1.0 + (2.0*(y1-yL)/(y2-y1));
            alpha = 2.0 - java.lang.Math.pow(beta,-(eta_c+1.0));
            
            if (rand <= (1.0/alpha)){
              betaq = java.lang.Math.pow ((rand*alpha),(1.0/(eta_c+1.0)));
            } else {
              betaq = java.lang.Math.pow ((1.0/(2.0 - rand*alpha)),(1.0/(eta_c+1.0)));
            } // if
            
            c1 = 0.5*((y1+y2)-betaq*(y2-y1));
            beta = 1.0 + (2.0*(yu-y2)/(y2-y1));
            alpha = 2.0 - java.lang.Math.pow(beta,-(eta_c+1.0));
            
            if (rand <= (1.0/alpha)){
              betaq = java.lang.Math.pow ((rand*alpha),(1.0/(eta_c+1.0)));
            } else {
              betaq = java.lang.Math.pow ((1.0/(2.0 - rand*alpha)),(1.0/(eta_c+1.0)));
            } // if
              
            c2 = 0.5*((y1+y2)+betaq*(y2-y1));
            
            if (c1<yL)
              c1=yL;
            
            if (c2<yL)
              c2=yL;
            
            if (c1>yu)
              c1=yu;
            
            if (c2>yu)
              c2=yu;                        
              
            if (PseudoRandom.randDouble()<=0.5) {
              offSpring[0].getDecisionVariables().variables_.get(i).setValue(c2);
              offSpring[1].getDecisionVariables().variables_.get(i).setValue(c1);
            } else {
              offSpring[0].getDecisionVariables().variables_.get(i).setValue(c1);
              offSpring[1].getDecisionVariables().variables_.get(i).setValue(c2);                            
            } // if
          } else {
            offSpring[0].getDecisionVariables().variables_.get(i).setValue(valueX1);
            offSpring[1].getDecisionVariables().variables_.get(i).setValue(valueX2);                        
          } // if
        } else {
          offSpring[0].getDecisionVariables().variables_.get(i).setValue(valueX2);
          offSpring[1].getDecisionVariables().variables_.get(i).setValue(valueX1);                    
        } // if
      } // if
    } // if
                                    
     return offSpring;                                                                                      
  } // doCrossover
  
  public void setDistributionIndex(double value) {
  	eta_c = value;
  }
  
  /**
  * Executes the operation
  * @param object An object containing an array of two parents
  * @return An object containing the offSprings
  */
  public Solution<Real>[] execute(Solution<Real> parent1, Solution<Real> parent2) throws JMException {
    if ((parent1.getType() != SolutionType_.Real) ||
        (parent2.getType() != SolutionType_.Real)) {

      String msg = "SBXCrossover.execute: the solutions " +
					    "are not of the right type. The type should be 'Real', but " +
					    parent1.getType() + " and " + 
					    parent2.getType() + " are obtained";
			Configuration.logger_.severe(msg);
      throw new JMException(msg) ; 
    } // if 
    
    Solution<Real> [] offSpring = doCrossover(probability, parent1, parent2);
        
    for (int i = 0; i < offSpring.length; i++)
    {
      offSpring[i].setCrowdingDistance(0.0);
      offSpring[i].setRank(0);
    } 
    return offSpring;//*/
  } // execute 
} // SBXCrossover
