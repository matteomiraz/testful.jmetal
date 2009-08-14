package jmetal.base.operator.selection;

import jmetal.base.SolutionSet;
import jmetal.util.JMException;

public abstract class Selection<T> {

	public abstract T execute(SolutionSet population) throws JMException;
}
