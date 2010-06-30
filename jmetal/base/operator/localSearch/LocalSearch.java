/**
 * LocalSearch.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.localSearch;

import jmetal.base.Solution;
import jmetal.base.TerminationCriterion;
import jmetal.base.Variable;
import jmetal.util.JMException;

/**
 * Abstract class representing a generic local search operator
 */
public abstract class LocalSearch<T extends Variable> {
	private static final long serialVersionUID = -3243846293089587688L;

	/** if true, the termination criterion is absolute; otherwise, it is related to each iteration */
	protected boolean absoluteTerminationCriterion;

	protected TerminationCriterion terminationCriterion;

	public TerminationCriterion getTerminationCriterion() {
		return terminationCriterion;
	}

	public void setTerminationCriterion(TerminationCriterion terminationCriterion) {
		this.terminationCriterion = terminationCriterion;
	}

	public boolean isAbsoluteTerminationCriterion() {
		return absoluteTerminationCriterion;
	}

	public void setAbsoluteTerminationCriterion(boolean absoluteTerminationCriterion) {
		this.absoluteTerminationCriterion = absoluteTerminationCriterion;
	}

	public abstract Solution<T> execute(Solution<T> solution) throws JMException;

} // LocalSearch
