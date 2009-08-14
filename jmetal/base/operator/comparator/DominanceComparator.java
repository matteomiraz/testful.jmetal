/**
 * DominanceComparator.java
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
 * <code>Solution</code> objects) based on a constraint violation test + 
 * dominance checking, as in NSGA-II.
 */
public class DominanceComparator<T extends Variable> implements Comparator<Solution<T>>{
 
  /** 
   * stores a comparator for check the OverallConstraintComparator
   */
  private final Comparator<Solution<T>> overallConstraintViolationComparator_ =
                              new OverallConstraintViolationComparator<T>();
 /**
  * Compares two solutions.
  * @param solution1 Object representing the first <code>Solution</code>.
  * @param solution2 Object representing the second <code>Solution</code>.
  * @return -1, or 0, or 1 if solution1 dominates solution2, both are 
  * non-dominated, or solution1  is dominated by solution22, respectively.
  */
  public int compare(Solution<T> solution1, Solution<T> solution2) {
    if (solution1==null)
      return 1;
    else if (solution2 == null)
      return -1;
    
    boolean dominate1=false; // dominate1 indicates if some objective of solution1 
                             // dominates the same objective in solution2. dominate2
    boolean dominate2=false; // is the complementary of dominate1.

    if (solution1.getOverallConstraintViolation() != solution2.getOverallConstraintViolation() &&
        (solution1.getOverallConstraintViolation() < 0 || solution2.getOverallConstraintViolation() < 0)) 
      return overallConstraintViolationComparator_.compare(solution1,solution2);

    // Equal number of violated constraint. Apply a dominance Test
    double[] obj1 = solution1.getObjectives();
    double[] obj2 = solution2.getObjectives();
    for (int i = 0; i < obj1.length; i++) {

    	if (obj1[i] < obj2[i])
        dominate1=true;
      else if (obj1[i] > obj2[i])
        dominate2=true;
    }
            
    if (dominate1 == dominate2)
      return 0; //No one dominate the other
    
    if (dominate1)
      return -1; // solution1 dominate

    return 1;    // solution2 dominate   
  } // compare
} // DominanceComparator
