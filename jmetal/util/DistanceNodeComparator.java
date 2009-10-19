/**
 * DistanceNodeComparator.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.util;

import java.util.Comparator;

/**
 * This class implements a <code>Comparator</code> to compare instances of
 * <code>DistanceNode</code>.
 */
public class DistanceNodeComparator implements Comparator<DistanceNode>{
    
  /**
   * Compares two <code>DistanceNode</code>.
   * @param o1 Object representing a DistanceNode
   * @param o2 Object representing a DistanceNode
   * @return -1 if the distance of o1 is smaller than the distance of o2,
   *          0 if the distance of both are equals, and
   *          1 if the distance of o1 is bigger than the distance of o2
   */
  public int compare(DistanceNode node1, DistanceNode node2){
    double distance1 = node1.getDistance();
    double distance2 = node2.getDistance();
        
    if (distance1 < distance2)
      return -1;
    else if (distance1 > distance2)
      return 1;
    else
      return 0;
  } // DistanceNodeComparator 
} // DistanceNodeComparator
