/**
 * TwoPointsCrossover.java Class representing a two points crossover operator
 * 
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.base.operator.crossover;

import java.util.ArrayList;
import java.util.List;

import jmetal.base.Solution;
import jmetal.base.Variable;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * Single point crossover with variable-lenght individuals
 */
public class OnePointCrossoverVarLen<T extends Variable> extends Crossover<T> {

	private static final long serialVersionUID = -1053495991518184005L;
	public static int MAX_LEN = 10000;

	/**
	 * Constructor Creates a new intance of the two point crossover operator
	 */
	public OnePointCrossoverVarLen() {} // TwoPointsCrossover

	public List<T> cross(int c1, List<T> p1, int c2, List<T> p2) {
		int len = c1 + p2.size() - c2;
		if(len > MAX_LEN) len = MAX_LEN;

		List<T> ret = new ArrayList<T>(len);

		for(int j = 0; j < c1; j++)
			ret.add(p1.get(j));

		for(int j = c2; j < p2.size() && ret.size() < MAX_LEN; j++)
			ret.add(p2.get(j));

		return ret;
	}

	/**
	 * Executes the operation
	 * 
	 * @param object An object containing an array of two solutions
	 * @return An object containing an array with the offSprings
	 * @throws JMException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Solution<T>[] execute(Solution<T> p1, Solution<T> p2) throws JMException {
		Solution<T> of1 = new Solution<T>(p1);
		Solution<T> of2 = new Solution<T>(p2);

		List<T> r1 = of1.getDecisionVariables().variables_;
		List<T> r2 = of2.getDecisionVariables().variables_;

		if(!r1.isEmpty() && !r2.isEmpty() && PseudoRandom.getMersenneTwisterFast().nextBoolean(probability)) {
			int cx1 = PseudoRandom.getMersenneTwisterFast().nextInt(r1.size());
			int cx2 = PseudoRandom.getMersenneTwisterFast().nextInt(r2.size());

			List<T> one = cross(cx1, r1, cx2, r2);
			List<T> two = cross(cx2, r2, cx1, r1);

			of1.getDecisionVariables().variables_ = one;
			of2.getDecisionVariables().variables_ = two;
		}

		return new Solution[] { of1, of2 };
	} // execute
} // TwoPointsCrossover