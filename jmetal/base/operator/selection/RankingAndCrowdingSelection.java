/**
 * RankingAndCrowdingSelection.java
 * @author Juan J. Durillo
 */

package jmetal.base.operator.selection;

import java.util.Comparator;

import jmetal.base.Configuration;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.operator.comparator.CrowdingComparator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;

/** 
 * This class implements a selection for selecting a number of solutions from
 * a solutionSet. The solutions are taken by mean of its ranking and 
 * crowding ditance values.
 * NOTE: if you use the default constructor, the problem has to be passed as
 * a parameter before invoking the execute() method -- see lines 67 - 74
 */
public class RankingAndCrowdingSelection<T extends Variable> extends Selection<T, SolutionSet<T>> {

  private static final long serialVersionUID = -5632920415904561097L;

	/**
   * stores the problem to solve 
   */
  private Problem<T> problem_;
  
  /**
   * stores a <code>Comparator</code> for crowding comparator checking.
   */
  private final Comparator<Solution<T>> crowdingComparator_ = 
                                  new CrowdingComparator<T>();

  
  int populationSize;
  
  /**
   * Constructor
   */
  public RankingAndCrowdingSelection() {
    problem_ = null ;
  } // RankingAndCrowdingSelection
  
  /**
   * Constructor
   * @param problem Problem to be solved
   */
  public RankingAndCrowdingSelection(Problem<T> problem) {
    problem_ = problem;
  } // RankingAndCrowdingSelection

  
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
	
	public void setProblem(Problem<T> problem) {
		problem_ = problem;
	}
	
  /**
  * Performs the operation
  * @param object Object representing a SolutionSet.
  * @return an object representing a <code>SolutionSet<code> with the selected parents
   * @throws JMException 
  */
  public SolutionSet<T> execute(SolutionSet<T> population) throws JMException {
    SolutionSet<T> result     = new SolutionSet<T>(populationSize);

    if (problem_ == null) {
    	String msg = "RankingAndCrowdingSelection.execute: problem not specified";
    	Configuration.logger_.severe(msg) ;
    	throw new JMException(msg) ; 
    } // if
    
    //->Ranking the union
    Ranking<T> ranking = new Ranking<T>(population);                        

    int remain = populationSize;
    int index  = 0;
    SolutionSet<T> front = null;
    population.clear();

    //-> Obtain the next front
    front = ranking.getSubfront(index);

    while ((remain > 0) && (remain >= front.size())){                
      //Asign crowding distance to individuals
      Distance.crowdingDistanceAssignment(front,problem_.getNumberOfObjectives());                
      //Add the individuals of this front
      for(Solution<T> s : front) 
      	result.add(s);

      //Decrement remaint
      remain = remain - front.size();

      //Obtain the next front
      index++;
      if (remain > 0) {
        front = ranking.getSubfront(index);
      } // if        
    } // while

    //-> remain is less than front(index).size, insert only the best one
    if (remain > 0) {  // front containt individuals to insert                        
      Distance.crowdingDistanceAssignment(front,problem_.getNumberOfObjectives());
      front.sort(crowdingComparator_);
      for (int k = 0; k < remain; k++) {
        result.add(front.get(k));
      } // for

      remain = 0; 
    } // if

    return result;
  } // execute    
} // RankingAndCrowding
