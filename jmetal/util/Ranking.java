/**
 * Ranking.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.base.operator.comparator.OverallConstraintViolationComparator;

/**
 * This class implements some facilities for ranking solutions.
 * Given a <code>SolutionSet</code> object, their solutions are ranked 
 * according to scheme proposed in NSGA-II; as a result, a set of subsets 
 * are obtained. The subsets are numbered starting from 0 (in NSGA-II, the 
 * numbering starts from 1); thus, subset 0 contains the non-dominated 
 * solutions, subset 1 contains the non-dominated solutions after removing those
 * belonging to subset 0, and so on.
 */
public class Ranking<T extends Variable> {
  
  /**
   * The <code>SolutionSet</code> to rank
   */
  private SolutionSet<T>   solutionSet_ ;
  
  /**
   * An array containing all the fronts found during the search
   */
  private SolutionSet<T>[] ranking_  ;
  
  /**
   * stores a <code>Comparator</code> for dominance checking
   */
  private final Comparator<Solution<T>> dominance_ = new DominanceComparator<T>();
  
  /**
   * stores a <code>Comparator</code> for Overal Constraint Violation Comparator
   * checking
   */
  private final Comparator<Solution<T>> constraint_ = new OverallConstraintViolationComparator<T>();
   
  private static final class Dominium<T extends Variable> {
  	/** number of solutions dominating me */
  	int dominateMe = 0;

    /** list of solutions dominated by me */
    List<Solution<T>> iDominate = new ArrayList<Solution<T>>();
  }
  
  /** 
   * Constructor.
   * @param solutionSet The <code>SolutionSet</code> to be ranked.
   */       
  @SuppressWarnings("unchecked")
	public Ranking(SolutionSet<T> solutionSet) {        
    solutionSet_ = solutionSet ;

    // dominateMe[i] contains the number of solutions dominating i        
    Map<Solution,Dominium<T>> dominate = new HashMap<Solution, Dominium<T>>(solutionSet_.size());

    // front[i] contains the list of individuals belonging to the front i
    List<Solution<T>> [] front = new List[solutionSet_.size()+1];
    for (int i = 0; i < front.length; i++) 
    	front[i] = new ArrayList<Solution<T>>();
        
    //-> Fast non dominated sorting algorithm
    for(Solution<T> p : solutionSet) {
    	// Initialice the list of individuals that i dominate and the number of individuals that dominate me
      Dominium<T> dom = new Dominium<T>();
      dominate.put(p, dom);

      // For all q individuals , calculate if p dominates q or vice versa
      for(Solution<T> q : solutionSet) {
        int flagDominate = constraint_.compare(p,q);
        if (flagDominate == 0) flagDominate =dominance_.compare(p,q);                                

        if (flagDominate == -1) dom.iDominate.add(q);
        else if (flagDominate == 1) dom.dominateMe++;   
      }
            
      // If nobody dominates p, p belongs to the first front
      if (dom.dominateMe == 0) {
        front[0].add(p);
        p.setRank(0);
      }
    }
        
    //Obtain the rest of fronts
    int nFronts = -1;
		while (front[++nFronts].size()!= 0) {
			for(Solution<T> sol1 : front[nFronts]) {
				for(Solution<T> sol2 : dominate.get(sol1).iDominate) {
					Dominium<T> dom = dominate.get(sol2);
          if (--dom.dominateMe==0) {
            front[nFronts+1].add(sol2);
            sol2.setRank(nFronts+1);
          }
        }
      }
    }
    //<-
        
    ranking_ = new SolutionSet[nFronts];
    //0,1,2,....,i-1 are front, then i fronts
    for (int j = 0; j < nFronts; j++) {
      ranking_[j] = new SolutionSet(front[j].size());
      for(Solution<T> s : front[j])
      	ranking_[j].add(s);
    }
    
  } // Ranking

  /**
   * Returns a <code>SolutionSet</code> containing the solutions of a given rank. 
   * @param rank The rank
   * @return Object representing the <code>SolutionSet</code>.
   */
  public SolutionSet<T> getSubfront(int rank) {
    return ranking_[rank];
  } // getSubFront

  /** 
  * Returns the total number of subFronts founds.
  */
  public int getNumberOfSubfronts() {
    return ranking_.length;
  } // getNumberOfSubfronts
} // Ranking
