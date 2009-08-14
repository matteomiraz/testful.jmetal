package jmetal.base.operator.crossover;

import jmetal.base.Solution;
import jmetal.util.JMException;


public abstract class DifferentialCrossover extends Crossover {

	private static final long serialVersionUID = 6089779477596657725L;

	@Override
	public Solution[] execute(Solution parent1, Solution parent2) throws JMException {
		return execute(parent1, new Solution[] { parent2 } );
	}

	public abstract Solution[] execute(Solution current, Solution [] parent) throws JMException;
}
