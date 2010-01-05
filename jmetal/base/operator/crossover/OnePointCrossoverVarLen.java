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

	/** Maximum length of an individual */
	private int maxLen = 10000;

	/**
	 * Constructor Creates a new intance of the two point crossover operator
	 */
	public OnePointCrossoverVarLen() {} // TwoPointsCrossover
	
	public int getMaxLen() {
		return maxLen;
	}
	
	public void setMaxLen(int maxLen) {
		this.maxLen = maxLen;
	}
	
	private Solution<T> cross(int c1, Solution<T> p1, int c2, Solution<T> p2) {
		Solution<T> son = new Solution<T>(p1);
		
		List<T> r1 = p1.getDecisionVariables().variables_;
		List<T> r2 = p2.getDecisionVariables().variables_;
		
		int len = c1 + r2.size() - c2;
		if(len > maxLen) len = maxLen;

		List<T> rep = new ArrayList<T>(len);
		for(int j = 0; j < c1; j++)
			rep.add(r1.get(j));
		for(int j = c2; j < r2.size() && rep.size() < maxLen; j++)
			rep.add(r2.get(j));

		
		son.getDecisionVariables().variables_ = rep;
		
		// inherit all paramemets
		
		final float perc1 = (1.0f * c1) / len;
		final float perc2 = 1-perc1;
		
		son.setOverallConstraintViolation(perc1 * p1.getOverallConstraintViolation() + perc2 * p2.getOverallConstraintViolation());
		son.setNumberOfViolatedConstraint((int)(perc1 * p1.getNumberOfViolatedConstraint() + perc2 * p2.getNumberOfViolatedConstraint()));
		son.setDistanceToSolutionSet(perc1 * p1.getDistanceToSolutionSet() + perc2 * p2.getDistanceToSolutionSet());
		son.setCrowdingDistance(perc1*p1.getCrowdingDistance() + perc2*p2.getCrowdingDistance());
		son.setKDistance(perc1*p1.getCrowdingDistance() + perc2*p2.getCrowdingDistance());                
		son.setFitness(perc1*p1.getFitness() + perc2*p2.getFitness());
		
		for(int i = 0; i < son.numberOfObjectives(); i++)
			son.setObjective(i, perc1*p1.getObjective(i) + perc2*p2.getObjective(i));
		
		return son;
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
		
		if(!p1.getDecisionVariables().variables_.isEmpty() && 
				!p2.getDecisionVariables().variables_.isEmpty()
				&& PseudoRandom.getMersenneTwisterFast().nextBoolean(probability)) {
			
			int cx1 = PseudoRandom.getMersenneTwisterFast().nextInt(p1.getDecisionVariables().variables_.size());
			int cx2 = PseudoRandom.getMersenneTwisterFast().nextInt(p2.getDecisionVariables().variables_.size());

			return new Solution[] {
					cross(cx1, p1, cx2, p2),
					cross(cx2, p2, cx1, p1)
			};
		}

		return new Solution[] { new Solution<T>(p1), new Solution<T>(p2) };

	} // execute
} // TwoPointsCrossover