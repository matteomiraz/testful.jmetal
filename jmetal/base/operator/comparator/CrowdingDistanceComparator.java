/**
 * CrowdingDistanceComparator.java
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
 * <code>Solution</code> objects) based on the crowding distance, as in NSGA-II.
 */
public class CrowdingDistanceComparator<T extends Variable> implements Comparator<Solution<T>>{
   
 /**
  * Compares two solutions.
  * @param o1 Object representing the first <code>Solution</code>.
  * @param o2 Object representing the second <code>Solution</code>.
  * @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
  * respectively.
  */
  public int compare(Solution<T> o1, Solution<T> o2) {
    if (o1==null)
      return 1;
    else if (o2 == null)
      return -1;
    
    double distance1 = o1.getCrowdingDistance();
    double distance2 = o2.getCrowdingDistance();
    if (distance1 >  distance2)
      return -1;
    if (distance1 < distance2)
      return 1;
    return 0;    
  } // compare
} // CrowdingDistanceComparator

