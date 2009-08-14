/**
 * BinaryTournament.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.selection;


import java.util.Comparator;

import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.operator.comparator.BinaryTournamentComparator;
import jmetal.util.PseudoRandom;

/**
 * This class implements an opertor for binary selections
 */
public class BinaryTournament extends Selection<Solution>{

  private static final long serialVersionUID = -6155510776793236308L;
	/**
   * Stores the <code>Comparator</code> used to compare two
   * solutions
   */
  private Comparator<Solution> comparator_;

  /**
   * Constructor
   * Creates a new Binary tournament operator using a BinaryTournamentComparator
   */
  public BinaryTournament(){
    comparator_ = new BinaryTournamentComparator();
  } // BinaryTournament

  
  /**
  * Constructor
  * Creates a new Binary tournament with a specific <code>Comparator</code>
  * @param comparator The comparator
  */
  public BinaryTournament(Comparator<Solution> comparator) {
    comparator_ = comparator;
  } // Constructor

  
  /**
  * Performs the operation
  * @param object Object representing a SolutionSet
  * @return the selected solution
  */
  public Solution execute(SolutionSet population) throws jmetal.util.JMException {
    Solution solution1,solution2;
    solution1 = population.get(PseudoRandom.randInt(0,population.size()-1));
    solution2 = population.get(PseudoRandom.randInt(0,population.size()-1));

    int flag = comparator_.compare(solution1,solution2);
    if (flag == -1)
      return solution1;
    else if (flag == 1)
      return solution2;
    else
      if (PseudoRandom.randDouble()<0.5)
        return solution1;
      else
        return solution2;                       
  } // execute
} // BinaryTournament
