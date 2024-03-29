/**
 * HUXCrossover.java
 * @author Juan J. Durillo
 * @version 1.0
 * Class representing a HUX crossover operator
 */
package jmetal.base.operator.crossover;

import jmetal.base.Configuration;
import jmetal.base.Solution;
import jmetal.base.variable.Binary;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class allows to apply a HUX crossover operator using two parent
 * solutions.
 * NOTE: the operator is applied to the first variable of the solutions, and 
 * the type of the solutions must be binary 
 * (e.g., <code>SolutionType_.Binary</code> or 
 * <code>SolutionType_.BinaryReal</code>.
 */
public class HUXCrossover extends Crossover<Binary>{

  private static final long serialVersionUID = -98268788775592756L;


	/**
   * Constructor
   * Create a new intance of the HUX crossover operator.
   */
  public HUXCrossover() {
  } // HUXCrossover


  /**
   * Perform the crossover operation
   * @param probability Crossover probability
   * @param parent1 The first parent
   * @param parent2 The second parent
   * @return An array containig the two offsprings
   * @throws JMException 
   */
  @SuppressWarnings("unchecked")
	public Solution<Binary>[] doCrossover(double   probability, 
                                Solution<Binary> parent1, 
                                Solution<Binary> parent2) throws JMException {
    Solution<Binary> [] offSpring = new Solution[2];
    offSpring[0] = new Solution<Binary>(parent1);
    offSpring[1] = new Solution<Binary>(parent2);
    try {         
      if (PseudoRandom.randDouble() < probability)
      {
        for (int var = 0; var < parent1.getDecisionVariables().variables_.size(); var++) {
          Binary p1 = (Binary)parent1.getDecisionVariables().variables_.get(var);
          Binary p2 = (Binary)parent2.getDecisionVariables().variables_.get(var);

          for (int bit = 0; bit < p1.getNumberOfBits(); bit++) {
            if (p1.bits_.get(bit) != p2.bits_.get(bit)) {
              if (PseudoRandom.randDouble() < 0.5) {
                ((Binary)offSpring[0].getDecisionVariables().variables_.get(var))
                .bits_.set(bit,p2.bits_.get(bit));
                ((Binary)offSpring[1].getDecisionVariables().variables_.get(var))
                .bits_.set(bit,p1.bits_.get(bit));
              }
            }
          }
        }  
        //7. Decode the results
        for (int i = 0; i < offSpring[0].getDecisionVariables().size(); i++)
        {
          ((Binary)offSpring[0].getDecisionVariables().variables_.get(i)).decode();
          ((Binary)offSpring[1].getDecisionVariables().variables_.get(i)).decode();
        }
      }          
    }catch (ClassCastException e1) {
      
      String msg = "HUXCrossover.doCrossover: Cannot perfom SinglePointCrossover ";
			Configuration.logger_.severe(msg) ;
      throw new JMException(msg) ; 
    }        
    return offSpring;                                                                                      
  } // doCrossover

  
  /**
  * Executes the operation
  * @param object An object containing an array of two solutions 
  * @return An object containing the offSprings
  */
  public Solution<Binary>[] execute(Solution<Binary> parent1, Solution<Binary> parent2) throws JMException {
    Solution<Binary>[] offSpring = doCrossover(probability, parent1, parent2);
    
    for (int i = 0; i < offSpring.length; i++)
    {
      offSpring[i].setCrowdingDistance(0.0);
      offSpring[i].setRank(0);
    } 
    return offSpring;
    
  } // execute
} // HUXCrossover
