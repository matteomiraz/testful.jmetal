/**
 * CrowdingArchive.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.archive;

import java.util.Comparator;

import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.operator.comparator.CrowdingDistanceComparator;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.base.operator.comparator.EqualSolutions;
import jmetal.util.Distance;

/**
 * This class implements a bounded archive based on crowding distances (as
 * defined in NSGA-II).
 */
public class CrowdingArchive<T extends Variable> extends SolutionSet<T> {    
  
  private static final long serialVersionUID = -4121763871161850908L;

	/** 
   * Stores the maximum size of the archive.
   */
  private int maxSize_;
  
  /**
   * stores the number of the objectives.
   */
  private int objectives_;    
  
  /**
   * Stores a <code>Comparator</code> for dominance checking.
   */
  private Comparator<Solution<T>> dominance_;
  
  /**
   * Stores a <code>Comparator</code> for equality checking (in the objective
   * space).
   */
  private Comparator<Solution<T>> equals_; 

  /**
   * Stores a <code>Comparator</code> for checking crowding distances.
   */
  private Comparator<Solution<T>> crowdingDistance_; 
  
  /**
   * Constructor. 
   * @param maxSize The maximum size of the archive.
   * @param numberOfObjectives The number of objectives.
   */
  public CrowdingArchive(int maxSize, int numberOfObjectives) {
    super(maxSize);
    maxSize_          = maxSize;
    objectives_       = numberOfObjectives;        
    dominance_        = new DominanceComparator<T>();
    equals_           = new EqualSolutions<T>();
    crowdingDistance_ = new CrowdingDistanceComparator<T>();
  } // CrowdingArchive
  
  /**
   * Adds a <code>Solution</code> to the archive. If the <code>Solution</code>
   * is dominated by any member of the archive, then it is discarded. If the 
   * <code>Solution</code> dominates some members of the archive, these are
   * removed. If the archive is full and the <code>Solution</code> has to be
   * inserted, the solutions are sorted by crowding distance and the one having
   * the minimum crowding distance value.
   * @param solution The <code>Solution</code>
   * @return true if the <code>Solution</code> has been inserted, false 
   * otherwise.
   */
  public boolean add(Solution<T> solution){
    int flag = 0;
    int i = 0;
    Solution<T> aux; //Store an solution temporally
    while (i < solutionsList_.size()){
      aux = solutionsList_.get(i);            
            
      flag = dominance_.compare(solution,aux);
      if (flag == 1) {               // The solution to add is dominated
        return false;                // Discard the new solution
      } else if (flag == -1) {       // A solution in the archive is dominated
        solutionsList_.remove(i);    // Remove it from the population            
      } else {
          if (equals_.compare(aux,solution)==0) { // There is an equal solution 
        	                                      // in the population
            return false; // Discard the new solution
          }  // if
          i++;
      }
    }
    // Insert the solution into the archive
    solutionsList_.add(solution);        
    if (size() > maxSize_) { // The archive is full
      Distance.crowdingDistanceAssignment(this,objectives_);
      sort(crowdingDistance_);
      //Remove the last
      remove(maxSize_);
    }        
    return true;
  } // add
} // CrowdingArchive
