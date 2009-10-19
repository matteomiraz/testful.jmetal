/**
 * LocalSearch.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.localSearch;

import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.util.JMException;

/**
 * Abstract class representing a generic local search operator
 */
public abstract class LocalSearchPopulation<T extends Variable> extends LocalSearch<T> { 
  private static final long serialVersionUID = -3243846293089587688L;

	public abstract SolutionSet<T> execute(SolutionSet<T> solution) throws JMException;

}
