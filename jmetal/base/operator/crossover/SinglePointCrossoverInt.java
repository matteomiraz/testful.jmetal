/**
 * SinglePointCrossover.java
 * Class representing a single point crossover operator
 * @author Juan J. Durillo
 * @author Antonio J. Nebro
 * @version 1.1
 */
package jmetal.base.operator.crossover;

import jmetal.base.Configuration;
import jmetal.base.Solution;
import jmetal.base.variable.Int;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class allows to apply a Single Point crossover operator using two parent
 * solutions.
 * NOTE: the operator is applied to binary or integer solutions, considering the
 * whole solution as a single variable.
 */
public class SinglePointCrossoverInt extends Crossover<Int> {

  private static final long serialVersionUID = 7967959079668529640L;

	/**
   * Constructor
   * Creates a new instance of the single point crossover operator
   */
  public SinglePointCrossoverInt() {
  } // SinglePointCrossover

  /**
   * Perform the crossover operation.
   * @param probability Crossover probability
   * @param parent1 The first parent
   * @param parent2 The second parent
   * @return An array containig the two offsprings
   * @throws JMException
   */
  @SuppressWarnings("unchecked")
	public Solution<Int>[] doCrossover(double probability,
          Solution<Int> parent1,
          Solution<Int> parent2) throws JMException {
    Solution<Int>[] offSpring = new Solution[2];
    offSpring[0] = new Solution<Int>(parent1);
    offSpring[1] = new Solution<Int>(parent2);

    try {
      if (PseudoRandom.randDouble() < probability) {
          int crossoverPoint = PseudoRandom.randInt(0, parent1.numberOfVariables() - 1);
          int valueX1;
          int valueX2;
          for (int i = crossoverPoint; i < parent1.numberOfVariables(); i++) {
            valueX1 = (int) parent1.getDecisionVariables().variables_.get(i).getValue();
            valueX2 = (int) parent2.getDecisionVariables().variables_.get(i).getValue();
            offSpring[0].getDecisionVariables().variables_.get(i).setValue(valueX2);
            offSpring[1].getDecisionVariables().variables_.get(i).setValue(valueX1);
          } // for
      }
    } catch (ClassCastException e1) {
      String msg = "SinglePointCrossover.doCrossover: Cannot perfom SinglePointCrossover";
			Configuration.logger_.severe(msg);
      throw new JMException(msg) ; 
    }
    return offSpring;
  } // doCrossover

  /**
   * Executes the operation
   * @param object An object containing an array of two solutions
   * @return An object containing an array with the offSprings
   * @throws JMException
   */
  @Override
  public Solution<Int>[] execute(Solution<Int> parent1, Solution<Int> parent2) throws JMException {
    Solution<Int>[] offSpring = doCrossover(probability, parent1, parent2);

    //-> Update the offSpring solutions
    for (int i = 0; i < offSpring.length; i++) {
      offSpring[i].setCrowdingDistance(0.0);
      offSpring[i].setRank(0);
    }
    return offSpring;//*/
  } // execute
} // SinglePointCrossover
