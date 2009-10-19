/**
 * Algorithm.java
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.base ;

import java.io.Serializable;

import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.Selection;
import jmetal.util.JMException;

/** This class implements a generic template for the algorithms developed in
 *  jMetal. Every algorithm must have a mapping between the parameters and 
 *  and their names, and another mapping between the operators and their names. 
 *  The class declares an abstract method called <code>execute</code>, which 
 *  defines the behavior of the algorithm.
 *  @param <V> the type of the variable
 *  @param <C> the type of the crossover operator
 *  @param <M> the type of the mutation operator
 *  @param <S> the type of the selection operator
 *  @param <I> the type of the improvement operator
 */ 
public abstract class Algorithm<V extends Variable, C extends Crossover<V>, M extends Mutation<V>, S extends Selection<V,?>, I extends LocalSearch<V>> implements Serializable {

	private static final long serialVersionUID = 170011594278842840L;

	// input parameters
	private int populationSize;
	private int maxEvaluations;

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	public void setMaxEvaluations(int maxEvaluations) {
		this.maxEvaluations = maxEvaluations;
	}

	// output parameters
	private int evaluations = -1;

	public void setEvaluations(int evaluations) {
		this.evaluations = evaluations;
	}

	public int getEvaluations() {
		return evaluations;
	}

	// configuration
	protected C crossoverOperator;
	protected M mutationOperator;
	protected S selectionOperator;
	protected I improvement;

	/**   
	 * Launches the execution of an specific algorithm.
	 * @return a <code>SolutionSet</code> that is a set of non dominated solutions
	 * as a result of the algorithm execution  
	 */
	public abstract SolutionSet<V> execute() throws JMException ;   

	public void setCrossover(C crossover) {
		this.crossoverOperator = crossover;
	}

	public void setMutation(M mutation) {
		this.mutationOperator = mutation;
	}

	public void setSelection(S selection) {
		this.selectionOperator = selection;
	}

	public void setImprovement(I improvement) {
		this.improvement = improvement;
	}
} // Algorithm
