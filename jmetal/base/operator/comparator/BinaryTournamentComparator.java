/** 
 * BinaryTournamentComparator.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.comparator;

import java.util.Comparator;

import jmetal.base.Solution;
import jmetal.base.Variable;

/**
 * This class implements a <code>Comparator</code> for <code>Solution</code>
 */
public class BinaryTournamentComparator<T extends Variable> implements Comparator<Solution<T>> {
  
  /**
   * stores a dominance comparator
   */
  private final Comparator<Solution<T>> dominance_ = new DominanceComparator<T>();
  
  /**
   * Compares two solutions.
   * A <code>Solution</code> a is less than b for this <code>Comparator</code>.
   * if the crowding distance of a if greater than the crowding distance of b.
   * @param o1 Object representing a <code>Solution</code>.
   * @param o2 Object representing a <code>Solution</code>.
   * @return -1, or 0, or 1 if o1 is less than, equals, or greater than o2,
   * respectively.
   */
  public int compare(Solution<T> o1, Solution<T> o2) {
    int flag = dominance_.compare(o1,o2);
    if (flag!=0) {
      return flag;
    }
    
    double crowding1, crowding2;
    crowding1 = o1.getCrowdingDistance();
    crowding2 = o2.getCrowdingDistance();
    
    if (crowding1 > crowding2) {
      return -1;
    } else if (crowding2 > crowding1) {
      return 1;
    } else {
      return 0;
    }
  } // compare
} // BinaryTournamentComparator.
