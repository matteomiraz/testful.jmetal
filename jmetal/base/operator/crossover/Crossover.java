package jmetal.base.operator.crossover;

import java.io.Serializable;

import jmetal.base.Solution;
import jmetal.base.Variable;
import jmetal.util.JMException;

public abstract class Crossover<T extends Variable> implements Serializable {

	private static final long serialVersionUID = -5454704223436690588L;
	protected double probability;

	public Crossover() {}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public double getProbability() {
		return probability;
	}

	public abstract Solution<T>[] execute(Solution<T> parent1, Solution<T> parent2) throws JMException;

}
