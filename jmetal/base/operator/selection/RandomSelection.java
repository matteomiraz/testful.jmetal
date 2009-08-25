/**
 * RandomSelection.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.selection;

import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class implements a random selection operator used for selecting two
 * random parents
 */
public class RandomSelection<T extends Variable> extends Selection<T, Solution<T>[]> {
  
  private static final long serialVersionUID = 7446171924839213334L;

	/**
  * Performs the operation
  * @param object Object representing a SolutionSet.
  * @return an object representing an array with the selected parents
  */
  @SuppressWarnings("unchecked")
	@Override
  public Solution<T>[] execute(SolutionSet<T> population) throws JMException {
    int pos1, pos2;
    pos1 = PseudoRandom.randInt(0,population.size()-1);
    pos2 = PseudoRandom.randInt(0,population.size()-1);
    while ((pos1 == pos2) && (population.size()>1)) {
      pos2 = PseudoRandom.randInt(0,population.size()-1);
    }
    
    Solution<T> [] parents = new Solution[2];
    parents[0] = population.get(pos1);
    parents[1] = population.get(pos2);
    
    return parents;
  } // Execute     
} // RandomSelection
