package jmetal.base.operator.crossover;

import jmetal.base.Solution;
import jmetal.base.Variable;
import jmetal.util.JMException;


public abstract class DifferentialCrossover<T extends Variable> extends Crossover<T> {

	private static final long serialVersionUID = 6089779477596657725L;

	@SuppressWarnings("unchecked")
	@Override
	public Solution<T>[] execute(Solution<T> parent1, Solution<T> parent2) throws JMException {
		return new Solution[] { execute(parent1, new Solution[] { parent2 } ) };
	}

	public abstract Solution<T> execute(Solution<T> current, Solution<T> [] parent) throws JMException;
}
