/**
 * StrengthRawFitnessArchive.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.base.archive;

import java.util.Comparator;

import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.base.operator.comparator.EqualSolutions;
import jmetal.base.operator.comparator.FitnessComparator;
import jmetal.util.Spea2Fitness;

/**
 * This class implemens a bounded archive based on strength raw fitness (as
 * defined in SPEA2).
 */ 
 public class StrengthRawFitnessArchive<T extends Variable> extends SolutionSet<T> {    
  
  private static final long serialVersionUID = -800403635250139815L;

	/**
   * Stores the maximum size of the archive.
   */
  private int maxSize_;    
  
  /**
   * Stores a <code>Comparator</code> for dominance checking.
   */
  private Comparator<Solution<T>> dominance_;
  
  /**
   * Stores a <code>Comparator</code> for fitness checking.
   */
  private Comparator<Solution<T>> fitnessComparator_;
  
  /**
   * Stores a <code>Comparator</code> for equality checking (in the objective
   * space).
   */
  private Comparator<Solution<T>> equals_;  
    
  /** 
  * Constructor.
  * @param maxSize The maximum size of the archive.
  */
  public StrengthRawFitnessArchive(int maxSize) {
    super(maxSize);
    maxSize_           = maxSize                   ;        
    dominance_         = new DominanceComparator<T>() ;
    equals_            = new EqualSolutions<T>()      ;
    fitnessComparator_ = new FitnessComparator<T>();
  } // StrengthRawFitnessArchive
    
  /**
   * Adds a <code>Solution</code> to the archive. If the <code>Solution</code>
   * is dominated by any member of the archive then it is discarded. If the 
   * <code>Solution</code> dominates some members of the archive, these are
   * removed. If the archive is full and the <code>Solution</code> has to be
   * inserted, all the solutions are ordered by his strengthRawFitness value and
   * the one having the worst value is removed.
   * @param solution The <code>Solution</code>
   * @return true if the <code>Solution</code> has been inserted, false
   * otherwise.
   */
  public boolean add(Solution<T> solution){
    int flag = 0;
    int i = 0;
    Solution<T> aux;
    while (i < solutionsList_.size()){
      aux = solutionsList_.get(i);                        
      flag = dominance_.compare(solution,aux);
      if (flag == 1) {                // The solution to add is dominated
        return false;                 // Discard the new solution
      } else if (flag == -1) {        // A solution in the archive is dominated
        solutionsList_.remove(i);     // Remove the dominated solution            
      } else {
        if (equals_.compare(aux,solution)==0) {
          return false;
        }
        i++;
      }
    }
    // Insert the solution in the archive
    solutionsList_.add(solution);

    if (size() > maxSize_){ // The archive is full           
      (new Spea2Fitness<T>(this)).fitnessAssign();
      sort(fitnessComparator_);            
      //Remove the last
      remove(maxSize_);
    }        
    return true;
  } // add
} //StrengthRawFitnessArchive
