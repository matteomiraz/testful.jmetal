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
import jmetal.base.variable.Binary;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class allows to apply a Single Point crossover operator using two parent
 * solutions.
 * NOTE: the operator is applied to binary or integer solutions, considering the
 * whole solution as a single variable.
 */
public class SinglePointCrossoverBinary extends Crossover<Binary> {

  private static final long serialVersionUID = 7967959079668529640L;

	/**
   * Constructor
   * Creates a new instance of the single point crossover operator
   */
  public SinglePointCrossoverBinary() {
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
	public Solution<Binary>[] doCrossover(double probability,
          Solution<Binary> parent1,
          Solution<Binary> parent2) throws JMException {
    Solution<Binary>[] offSpring = new Solution[2];
    offSpring[0] = new Solution<Binary>(parent1);
    offSpring[1] = new Solution<Binary>(parent2);

    try {
      if (PseudoRandom.randDouble() < probability) {
          //1. Compute the total number of bits
          int totalNumberOfBits = 0;
          for (int i = 0; i < parent1.getDecisionVariables().size(); i++) {
            totalNumberOfBits +=
                    ((Binary) parent1.getDecisionVariables().variables_.get(i)).getNumberOfBits();
          }

          //2. Calcule the point to make the crossover
          int crossoverPoint = PseudoRandom.randInt(0, totalNumberOfBits - 1);

          //3. Compute the variable that containt the crossoverPoint bit
          int variable = 0;
          int acountBits =
                  ((Binary) parent1.getDecisionVariables().variables_.get(variable)).getNumberOfBits();

          while (acountBits < (crossoverPoint + 1)) {
            variable++;
            acountBits +=
                    ((Binary) parent1.getDecisionVariables().variables_.get(variable)).getNumberOfBits();
          }

          //4. Compute the bit into the variable selected
          int diff = acountBits - crossoverPoint;
          int intoVariableCrossoverPoint =
                  ((Binary) parent1.getDecisionVariables().variables_.get(variable)).getNumberOfBits() - diff;

          //5. Make the crossover into the the gene;
          Binary offSpring1, offSpring2;
          offSpring1 =
                  (Binary) parent1.getDecisionVariables().variables_.get(variable).clone();
          offSpring2 =
                  (Binary) parent2.getDecisionVariables().variables_.get(variable).clone();

          for (int i = intoVariableCrossoverPoint;
                  i < offSpring1.getNumberOfBits();
                  i++) {
            boolean swap = offSpring1.bits_.get(i);
            offSpring1.bits_.set(i, offSpring2.bits_.get(i));
            offSpring2.bits_.set(i, swap);
          }

          offSpring[0].getDecisionVariables().variables_.set(variable, offSpring1);
          offSpring[1].getDecisionVariables().variables_.set(variable, offSpring2);

          //6. Apply the crossover to the other variables
          for (int i = 0; i < variable; i++) {
            offSpring[0].getDecisionVariables().variables_.set(variable,
                    (Binary) parent2.getDecisionVariables().variables_.get(variable).clone());

            offSpring[1].getDecisionVariables().variables_.set(variable, 
                    (Binary) parent1.getDecisionVariables().variables_.get(variable).clone());

          }

          //7. Decode the results
          for (int i = 0; i < offSpring[0].getDecisionVariables().size(); i++) {
            ((Binary) offSpring[0].getDecisionVariables().variables_.get(variable)).decode();
            ((Binary) offSpring[1].getDecisionVariables().variables_.get(variable)).decode();
          }
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
  public Solution<Binary>[] execute(Solution<Binary> parent1, Solution<Binary> parent2) throws JMException {
    Solution<Binary>[] offSpring = doCrossover(probability, parent1, parent2);

    //-> Update the offSpring solutions
    for (int i = 0; i < offSpring.length; i++) {
      offSpring[i].setCrowdingDistance(0.0);
      offSpring[i].setRank(0);
    }
    return offSpring;//*/
  } // execute
} // SinglePointCrossover
