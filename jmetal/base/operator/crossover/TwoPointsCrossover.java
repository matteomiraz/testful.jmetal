/**
 * TwoPointsCrossover.java
 * Class representing a two points crossover operator
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.base.operator.crossover;

import jmetal.base.Configuration;
import jmetal.base.Solution;
import jmetal.base.Configuration.SolutionType_;
import jmetal.base.Configuration.VariableType_;
import jmetal.base.variable.Permutation;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

 /**
 * This class allows to apply a two points crossover operator using two parent
 * solutions.
 * NOTE: the operator is applied to the first variable of the solutions, and 
 * the type of the solutions must be <code>SolutionType_.Permutation</code>.
 */
  public class TwoPointsCrossover extends Crossover {
    
    
  private static final long serialVersionUID = 8484533354272297259L;

	/**
   * Constructor
   * Creates a new intance of the two point crossover operator
   */
  public TwoPointsCrossover() {
  } // TwoPointsCrossover

  /**
  * Perform the crossover operation
  * @param probability Crossover probability
  * @param parent1 The first parent
  * @param parent2 The second parent
  * @return Two offspring solutions
   * @throws JMException 
  */
  public Solution[] doCrossover(double   probability, 
                                Solution parent1, 
                                Solution parent2) throws JMException {

    Solution [] offspring = new Solution[2];

    offspring[0] = new Solution(parent1);
    offspring[1] = new Solution(parent2);

    if (parent1.getDecisionVariables().variables_.get(0).getVariableType() ==
                                                          VariableType_.Permutation) {
      if (PseudoRandom.randDouble() < probability) {
        int crosspoint1        ;
        int crosspoint2        ;
        int permutationLength  ;
        int parent1Vector[]    ;
        int parent2Vector[]    ;
        int offspring1Vector[] ;
        int offspring2Vector[] ;

        permutationLength = ((Permutation)parent1.getDecisionVariables().variables_.get(0)).getLength() ;
        parent1Vector     = ((Permutation)parent1.getDecisionVariables().variables_.get(0)).vector_ ;
        parent2Vector    = ((Permutation)parent2.getDecisionVariables().variables_.get(0)).vector_ ;    
        offspring1Vector = ((Permutation)offspring[0].getDecisionVariables().variables_.get(0)).vector_ ;
        offspring2Vector = ((Permutation)offspring[1].getDecisionVariables().variables_.get(0)).vector_ ;

        // STEP 1: Get two cutting points
        crosspoint1 = PseudoRandom.randInt(0,permutationLength-1) ;
        crosspoint2 = PseudoRandom.randInt(0,permutationLength-1) ;
        
        while (crosspoint2 == crosspoint1)  
          crosspoint2 = PseudoRandom.randInt(0,permutationLength-1) ;

        if (crosspoint1 > crosspoint2) {
          int swap ;
          swap        = crosspoint1 ;
          crosspoint1 = crosspoint2 ;
          crosspoint2 = swap          ;
        } // if
        
        // STEP 2: Obtain the first child
        int m = 0;
        for(int j = 0; j < permutationLength; j++) {
          boolean exist = false;
          int temp = parent2Vector[j];
          for(int k = crosspoint1; k <= crosspoint2; k++) {
            if (temp == offspring1Vector[k]) {
              exist = true;
              break;
            } // if
          } // for
          if (!exist) {
            if (m == crosspoint1)
              m = crosspoint2 + 1;
            offspring1Vector[m++] = temp;
          } // if
        } // for

        // STEP 3: Obtain the second child
        m = 0;
        for(int j = 0; j < permutationLength; j++) {
          boolean exist = false;
          int temp = parent1Vector[j];
          for(int k = crosspoint1; k <= crosspoint2; k++) {
            if (temp == offspring2Vector[k]) {
              exist = true;
              break;
            } // if
          } // for
          if(!exist) {
            if (m == crosspoint1)
              m = crosspoint2 + 1;
            offspring2Vector[m++] = temp;
          } // if
        } // for
      } // if 
    } // if
    else
    {
      String msg = "TwoPointsCrossover.doCrossover: invalid type" + 
          parent1.getDecisionVariables().variables_.get(0).getVariableType();
			Configuration.logger_.severe(msg);
      throw new JMException(msg) ; 
    } // else

    return offspring;                                                                                      
  } // makeCrossover

 /**
  * Executes the operation
  * @param object An object containing an array of two solutions 
  * @return An object containing an array with the offSprings
 * @throws JMException 
  */
  @Override
  public Solution[] execute(Solution parent1, Solution parent2) throws JMException {
    if ((parent1.getType() != SolutionType_.Permutation) ||
        (parent2.getType() != SolutionType_.Permutation)) {
      
      Configuration.logger_.severe("TwoPointsCrossover.execute: the solutions " +
          "are not of the right type. The type should be 'Permutation', but " +
          parent1.getType() + " and " + 
          parent2.getType() + " are obtained");
    } // if 
    	
    Solution [] offspring = doCrossover(probability,
                                        parent1,
                                        parent2);

    return offspring; 
  } // execute
  
} // TwoPointsCrossover
