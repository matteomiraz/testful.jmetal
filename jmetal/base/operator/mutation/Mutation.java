package jmetal.base.operator.mutation;

import jmetal.base.Solution;
import jmetal.base.Variable;
import jmetal.util.JMException;

public abstract class Mutation<T extends Variable> {

	protected double probability;

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public double getProbability() {
		return probability;
	}

	public abstract void execute(Solution<T> parent) throws JMException;
}
