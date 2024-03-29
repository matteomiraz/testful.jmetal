/**
 * PMXCrossover.java
 * Class representing a partially matched (PMX) crossover operator
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.base.operator.crossover;

import jmetal.base.Solution;
import jmetal.base.variable.Permutation;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

 /**
 * This class allows to apply a PMX crossover operator using two parent
 * solutions.
 * NOTE: the operator is applied to the first variable of the solutions, and 
 * the type of those variables must be VariableType_.Permutation.
 */
  public class PMXCrossover extends Crossover<Permutation> {
  private static final long serialVersionUID = 1572296934919082873L;

	/**
   * Constructor
   */
  public PMXCrossover() {
  } // PMXCrossover

  /**
   * Perform the crossover operation
   * @param probability Crossover probability
   * @param parent1 The first parent
   * @param parent2 The second parent
   * @return An array containig the two offsprings
   * @throws JMException 
   */
  @SuppressWarnings("unchecked")
	public Solution<Permutation>[] doCrossover(double   probability, 
                                Solution<Permutation> parent1, 
                                Solution<Permutation> parent2) throws JMException {

    Solution<Permutation> [] offspring = new Solution[2];

    offspring[0] = new Solution<Permutation>(parent1);
    offspring[1] = new Solution<Permutation>(parent2);

      int permutationLength ;

      permutationLength = ((Permutation)parent1.getDecisionVariables().variables_.get(0)).getLength() ;

      int parent1Vector[]    = ((Permutation)parent1.getDecisionVariables().variables_.get(0)).vector_ ;
      int parent2Vector[]    = ((Permutation)parent2.getDecisionVariables().variables_.get(0)).vector_ ;    
      int offspring1Vector[] = ((Permutation)offspring[0].getDecisionVariables().variables_.get(0)).vector_ ;
      int offspring2Vector[] = ((Permutation)offspring[1].getDecisionVariables().variables_.get(0)).vector_ ;

      if (PseudoRandom.randDouble() < probability) {
        int cuttingPoint1 ;
        int cuttingPoint2 ;

//      STEP 1: Get two cutting points
        cuttingPoint1 = PseudoRandom.randInt(0,permutationLength-1) ;
        cuttingPoint2 = PseudoRandom.randInt(0,permutationLength-1) ;
        while (cuttingPoint2 == cuttingPoint1)	
          cuttingPoint2 = PseudoRandom.randInt(0,permutationLength-1) ;

        if (cuttingPoint1 > cuttingPoint2) {
          int swap ;
          swap          = cuttingPoint1 ;
          cuttingPoint1 = cuttingPoint2 ;
          cuttingPoint2 = swap          ;
        } // if
//      STEP 2: Get the subchains to interchange
        int replacement1[] = new int[permutationLength] ;
        int replacement2[] = new int[permutationLength] ;
        for (int i = 0; i < permutationLength; i++)
          replacement1[i] = replacement2[i] = -1;

//      STEP 3: Interchange   	
        for (int i = cuttingPoint1; i <= cuttingPoint2; i++) {
          offspring1Vector[i] = parent2Vector[i] ;
          offspring2Vector[i] = parent1Vector[i] ;

          replacement1[parent2Vector[i]] = parent1Vector[i] ;
          replacement2[parent1Vector[i]] = parent2Vector[i] ;
        } // for

//      STEP 4: Repair offsprings
        for (int i = 0; i < permutationLength; i++) {
          if ((i >= cuttingPoint1) && (i <= cuttingPoint2)) 
            continue ; 

          int n1 = parent1Vector[i] ;
          int m1 = replacement1 [n1] ;

          int n2 = parent2Vector[i] ;
          int m2 = replacement2 [n2] ;

          while (m1 != -1) {
            n1 = m1 ;
            m1 = replacement1[m1] ;
          } // while
          while (m2 != -1) {
            n2 = m2 ;
            m2 = replacement2[m2] ;
          } // while
          offspring1Vector[i] = n1 ;
          offspring2Vector[i] = n2 ;
        } // for
      } // if
    return offspring ;                                                                                      
  } // doCrossover

  /**
   * Executes the operation
   * @param object An object containing an array of two solutions 
   * @throws JMException 
   */
  public Solution<Permutation>[] execute(Solution<Permutation> parent1, Solution<Permutation> parent2) throws JMException {
    Solution<Permutation> [] offspring = doCrossover(probability, parent1, parent2);

    return offspring; 
  } // execute
} // PMXCrossover
