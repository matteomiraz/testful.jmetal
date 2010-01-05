/**
 * SolutionComparator.java
 * 
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.comparator;

import java.util.Comparator;
import java.util.Iterator;

import jmetal.base.Configuration;
import jmetal.base.Solution;
import jmetal.base.Variable;
import jmetal.base.VariableValue;
import jmetal.util.Distance;
import jmetal.util.JMException;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on the values of the variables.
 */
public class SolutionComparator<T extends Variable> implements Comparator<Solution<T>> {
   
  /**
   * Establishes a value of allowed dissimilarity
   */
  private static final double EPSILON  = 1e-10;    
        
  /**
   * Compares two solutions.
   * @param o1 Object representing the first <code>Solution</code>. 
   * @param o2 Object representing the second <code>Solution</code>.
   * @return 0, if both solutions are equals with a certain dissimilarity, -1
   * otherwise.
   */
  @SuppressWarnings("unchecked")
	public int compare(Solution<T> solution1, Solution<T> solution2) {
    if (solution1.numberOfVariables() != solution2.numberOfVariables())
      return -1;

    if(solution1.getDecisionVariables().variables_.get(0) instanceof VariableValue) {
    	try {
    		if (Distance.distanceBetweenSolutions((Solution<VariableValue>)solution1,(Solution<VariableValue>)solution2) < EPSILON)
    			return 0;
    	} catch (JMException e) {
    		Configuration.logger_.severe("SolutionComparator.compare: JMException ") ; 
    	}
    } else {
    	// For standard variables, check if variables are the same
    	Iterator<T> it1 = solution1.getDecisionVariables().variables_.iterator();
    	Iterator<T> it2 = solution2.getDecisionVariables().variables_.iterator();
    	
    	while(it1.hasNext() && it2.hasNext()) {
    		T v1 = it1.next();
    		T v2 = it2.next();
    		
    		if(v1 == null && v2 == null) continue;
    		if(v1 == null ^ v2 == null) return -1;
    		if(!v1.equals(v2)) return -1;
    	}
    	return 0;
    }
                
    return -1;
  } // compare
} // SolutionComparator
