/**
 * FitnessAndCrowdingComparator.java
 * 
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.comparator;

import java.util.Comparator;

import jmetal.base.Solution;
import jmetal.base.Variable;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on the fitness and crowding distance.
 */
public class FitnessAndCrowdingDistanceComparator<T extends Variable> implements Comparator<Solution<T>>{
  
  /** 
   * stores a comparator for check the fitness value of the solutions
   */
  private final Comparator<Solution<T>> fitnessComparator_ =
                              new FitnessComparator<T>();
  /** 
   * stores a comparator for check the crowding distance
   */
  private final Comparator<Solution<T>> crowdingDistanceComparator_ = 
                              new CrowdingDistanceComparator<T>();
  
 /**
  * Compares two solutions.
  * @param solution1 Object representing the first <code>Solution</code>.
  * @param solution2 Object representing the second <code>Solution</code>.
  * @return -1, or 0, or 1 if solution1 is less than, equal, or greater than 
  * solution2, respectively.
  */
  public int compare(Solution<T> solution1, Solution<T> solution2) {    
    
    int flag = fitnessComparator_.compare(solution1,solution2);
    if (flag != 0) {
      return flag;
    } else {
      return crowdingDistanceComparator_.compare(solution1,solution2);        
    }
  } // compares
} // FitnessAndCrowdingDistanceComparator
