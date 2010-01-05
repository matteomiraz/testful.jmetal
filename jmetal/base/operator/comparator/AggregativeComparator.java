/**
 * AggregativeComparator.java
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
 * <code>Solution</code> objects) based on the aggregative sum of the objective
 * values.
 */
public class AggregativeComparator<T extends Variable> implements Comparator<Solution<T>> {            
   
 /**
  * Compares two solutions.
  * @param solution1 Object representing the first <code>Solution</code>.
  * @param solution2 Object representing the second <code>Solution</code>.
  * @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
  * respectively.
  */
  public int compare(Solution<T> solution1, Solution<T> solution2) {
    if (solution1==null) {
      return 1;
    } else if (solution2 == null) {
      return -1;
    }
    
    double value1, value2;
    value1 = solution1.getAggregativeValue();
    value2 = solution2.getAggregativeValue();        
    if (value1 < value2)
      return -1;
    else if (value2 < value1)
      return 1;
    else 
      return 0;
  } // compare
} // AgregativeComparator
