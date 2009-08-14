/**
 * LocalSearch.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.localSearch;

import jmetal.base.Solution;
import jmetal.util.JMException;

/**
 * Abstract class representing a generic local search operator
 */
public abstract class LocalSearch { 
  private static final long serialVersionUID = -3243846293089587688L;

  protected int improvementRounds;
  
  
	public void setImprovementRounds(int improvementRounds) {
		this.improvementRounds = improvementRounds;
	}
  
	/**
   * Returns the number of evaluations made by the local search operator
   */
  public abstract int getEvaluations();

	public abstract Solution execute(Solution solution) throws JMException;
	
} // LocalSearch
