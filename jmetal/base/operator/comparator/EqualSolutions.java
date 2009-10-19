/**
 * EqualSolutions.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.comparator;

import java.util.Comparator;

import jmetal.base.Solution;
import jmetal.base.Variable;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based whether all the objective values are
 * equal or not. A dominance test is applied to decide about what solution
 * is the best.
 */
public class EqualSolutions<T extends Variable> implements Comparator<Solution<T>>{        
   
  /**
   * Compares two solutions.
   * @param solution1 Object representing the first <code>Solution</code>.
   * @param solution2 Object representing the second <code>Solution</code>.
   * @return -1, or 0, or 1, or 2 if solution1 is dominates solution2, solution1 
   * and solution2 are equals, or solution1 is greater than solution2, 
   * respectively. 
   */
  public int compare(Solution<T> solution1, Solution<T> solution2) {
    if (solution1==null)
      return 1;
    else if (solution2 == null)
      return -1;
        
    boolean dominate1 = false; // dominate1 indicates if some objective of solution1 
                               // dominates the same objective in solution2. dominate2
    boolean dominate2 = false; // is the complementary of dominate1.
    
    double[] sol1obj = solution1.getObjectives();
    double[] sol2objs = solution2.getObjectives();

    for (int i = 0; i < solution1.numberOfObjectives(); i++) {
      if (sol1obj[i] < sol2objs[i]) dominate1 = true;
      else if (sol1obj[i] > sol2objs[i]) dominate2 = true;
    }
            
    if (!(dominate1 || dominate2)) {            
      return 0; //No one dominate the other
    }
    
    if (dominate1) return -1; // solution1 dominate
    if (dominate2) return 1;  // solution2 dominate
    
    return 2;
  } // compare
} // EqualSolutions

