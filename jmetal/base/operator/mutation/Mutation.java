package jmetal.base.operator.mutation;

import jmetal.base.Solution;
import jmetal.util.JMException;

public abstract class Mutation {

	protected double probability;

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public double getProbability() {
		return probability;
	}

	public abstract Solution execute(Solution parent) throws JMException;
}
