/**
 * EpsilonDominanceComparator.java
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
 * <code>Solution</code> objects) based on epsilon dominance.
 */
public class EpsilonDominanceComparator<T extends Variable> implements Comparator<Solution<T>>{
   
  /**
   * Stores the value of eta, needed for epsilon-dominance.
   */
  private double eta_;
  
  /** 
   * stores a comparator for check the OverallConstraintComparator
   */
	private final Comparator<Solution<T>> overallConstraintViolationComparator_ =
                              new OverallConstraintViolationComparator<T>();
  
  /**
   * Constructor.
  *  @param eta Value for epsilon-dominance.
  */
  public EpsilonDominanceComparator(double eta) {
    eta_ = eta;
  }
  
 /** 
  * Compares two solutions.
  * @param solution1 Object representing the first <code>Solution</code>.
  * @param solution2 Object representing the second <code>Solution</code>.
  * @return -1, or 0, or 1 if solution1 dominates solution2, both are 
  * non-dominated, or solution1 is dominated by solution2, respectively.
  */
  public int compare(Solution<T> solution1, Solution<T> solution2) {
    if (solution1==null)
      return 1;
    else if (solution2 == null)
      return -1;    
        
    boolean dominate1 = false; // dominate1 indicates if some objective of solution1 
                               // dominates the same objective in solution2. dominate2
    boolean dominate2 = false; // is the complementary of dominate1.

    int flag; 
    flag = overallConstraintViolationComparator_.compare(solution1,solution2);
    
    if (flag != 0) {      
      return flag;
    }

    double[] sol1objs = solution1.getObjectives();
    double[] sol2objs = solution2.getObjectives();

    // Idem number of violated constraint. Apply a dominance Test
    for (int i = 0; i < solution1.numberOfObjectives(); i++) {
			double value1 = sol1objs[i];
			double value2 = sol2objs[i];

      //Objetive implements comparable!!! 
      if (value1/(1 + eta_) < value2) dominate1 = true;
      else if (value1/(1 + eta_) > value2) dominate2 = true;
    }
            
    if (dominate1 == dominate2) // No one dominates the other
      return 0; 
    
    if (dominate1) // solution1 dominates 
    	return -1; 
    
    return 1;    // solution2 dominates
  } // compare
} // EpsilonDominanceComparator
