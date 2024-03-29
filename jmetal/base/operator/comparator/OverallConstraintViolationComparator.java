/**
 * OverallConstraintViolationComparator.java
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
 * <code>Solution</code> objects) based on the overall constraint violation of
 * the solucions, as in NSGA-II.
 */
public class OverallConstraintViolationComparator<T extends Variable> implements Comparator<Solution<T>> {
   
 /** 
  * Compares two solutions.
  * @param o1 Object representing the first <code>Solution</code>.
  * @param o2 Object representing the second <code>Solution</code>.
  * @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
  * respectively.
  */
  public int compare(Solution<T> o1, Solution<T> o2) {    
    double overall1 = o1.getOverallConstraintViolation();
    double overall2 = o2.getOverallConstraintViolation();
        
    if ((overall1 < 0) && (overall2 < 0)) {
      if (overall1 > overall2){
        return -1;
      } else if (overall2 > overall1){
        return 1;
      } else {
        return 0;
      }
    } else if ((overall1 == 0) && (overall2 < 0)) {
      return -1;
    } else if ((overall1 < 0) && (overall2 == 0)) {        
      return 1;
    } else {
      return 0;        
    }
  } // compare    
} // ConstraintComparator
