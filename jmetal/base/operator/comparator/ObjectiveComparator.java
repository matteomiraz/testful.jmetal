/**
 * ObjectiveComparator.java
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
 * <code>Solution</code> objects) based on a objective values.
 */
public class ObjectiveComparator<T extends Variable> implements Comparator<Solution<T>>{
   
  /**
   * Stores the index of the objective to compare
   */
  private int nObj;
    
  /**
   * Constructor.
   * @param nObj The index of the objective to compare
   */
  public ObjectiveComparator(int nObj) {
    this.nObj = nObj;
  } // ObjectiveComparator
  
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
    
    double objetive1 = o1.getObjective(this.nObj);
    double objetive2 = o2.getObjective(this.nObj);        
    if (objetive1 < objetive2) {
      return -1;
    } else if (objetive1 > objetive2) {
      return 1;
    } else {
      return 0;                                                        
    }
  } // compare
} // ObjectiveComparator
