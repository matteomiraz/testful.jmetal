/**
 * PESA2Selection.java
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.base.operator.selection;

import jmetal.base.Configuration;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.archive.AdaptiveGridArchive;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/** 
 * This class implements a selection operator as the used in PESA-II 
 * algorithm
 */
public class PESA2Selection<T extends Variable> extends Selection<T, Solution<T>> {      
        
  private static final long serialVersionUID = 345701401946064303L;

	/**
  * Performs the operation
  * @param object Object representing a SolutionSet. This solution set
  * must be an instancen <code>AdaptiveGridArchive</code>
  * @return the selected solution
   * @throws JMException 
  */
  @Override
  public Solution<T> execute(SolutionSet<T> set) throws JMException {
    try {
      AdaptiveGridArchive<T> archive = (AdaptiveGridArchive<T>)set;
      int selected;        
      int hypercube1 = archive.getGrid().randomOccupiedHypercube();
      int hypercube2 = archive.getGrid().randomOccupiedHypercube();                                        
        
      if (hypercube1 != hypercube2){
        if (archive.getGrid().getLocationDensity(hypercube1) < 
            archive.getGrid().getLocationDensity(hypercube2)) {
        
          selected = hypercube1;
        
        } else if (archive.getGrid().getLocationDensity(hypercube2) <
                   archive.getGrid().getLocationDensity(hypercube1)) {
        
          selected = hypercube2;
        } else {
          if (PseudoRandom.randDouble() < 0.5) {
            selected = hypercube2;
          } else {
            selected = hypercube1;
          }
        }
      } else { 
        selected = hypercube1;
      }
      int base = PseudoRandom.randInt(0,archive.size()-1);
      int cnt = 0;
      while (cnt < archive.size()){   
        Solution<T> individual = archive.get((base + cnt)% archive.size());        
        if (archive.getGrid().location(individual) != selected){
          cnt++;                
        } else {
          return individual;
        }
      }        
      return archive.get((base + cnt) % archive.size());
    } catch (ClassCastException e) {
      String msg = "PESA2Selection.execute: ClassCastException. Found" + set.getClass().getCanonicalName() + "Expected: AdaptativeGridArchive";
			Configuration.logger_.severe(msg) ;
      throw new JMException(msg) ; 
    }
  } //execute
} // PESA2Selection
