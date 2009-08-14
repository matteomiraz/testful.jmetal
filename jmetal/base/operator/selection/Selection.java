package jmetal.base.operator.selection;

import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.util.JMException;

public abstract class Selection<V extends Variable, R> {

	public abstract R execute(SolutionSet<V> population) throws JMException;
}
