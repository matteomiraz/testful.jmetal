/**
 * Problem.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * Created on 16 de octubre de 2006, 17:04
 */

package jmetal.base;

import java.io.Serializable;
import java.util.List;

import jmetal.util.JMException;

/**
 * Abstract class representing a multiobjective optimization problem
 */
public abstract class Problem<T extends Variable> implements Serializable {
  
	private static final long serialVersionUID = -3280347869784909859L;

	/**
   * Stores the number of variables of the problem
   */
  protected int numberOfVariables_ ;
  
  /** 
   * Stores the number of objectives of the problem
   */
  protected int numberOfObjectives_ ;
  
  /**
   * Stores the number of constraints of the problem
   */
  protected int numberOfConstraints_      ;
  
  /**
   * Stores the problem name
   */
  protected String problemName_;
  
  /** Stores the current generation */ 
  protected int currentGeneration = -1;
  
  /** 
   * Constructor. 
   */
  public Problem() {
  } // Problem
        
  /** 
   * Gets the number of decision variables of the problem.
   * @return the number of decision variables.
   */
  public int getNumberOfVariables() {
    return numberOfVariables_ ;   
  } // getNumberOfVariables
    
  /** 
   * Gets the the number of objectives of the problem.
   * @return the number of objectives.
   */
  public int getNumberOfObjectives() {
    return numberOfObjectives_ ;
  } // getNumberOfObjectives
    
  /**
   * Evaluates a <code>Solution</code> object.
   * @param solution The <code>Solution</code> to evaluate.
   */    
  public abstract void evaluate(Solution<T> solution) throws JMException ;    

  /**
   * Evaluates a set of <code>Solution</code>s.
   * @param solution The set of <code>Solution</code>s to evaluate.
   * @return the number of evaluations done
   */    
  public int evaluate(final Iterable<Solution<T>> set) throws JMException {
  	int n = 0;
  	
  	for(Solution<T> solution : set) {
  		evaluate(solution);
  		n++;
  	}
  	
  	return n;
  }

  /**
   * Gets the number of side constraints in the problem.
   * @return the number of constraints.
   */
  public int getNumberOfConstraints() {
    return numberOfConstraints_ ;
  } // getNumberOfConstraints
    
  /**
   * Evaluates the overall constraint violation of a <code>Solution</code> 
   * object.
   * @param solution The <code>Solution</code> to evaluate.
   */    
  public void evaluateConstraints(Solution<T> solution) throws JMException {
    // The default behavior is to do nothing. Only constrained problems have to
    // re-define this method
  } // evaluateConstraints

  /**
   * Returns the problem name
   * @return The problem name
   */
  public String getName() {
    return problemName_ ;
  }

	public abstract List<T> generateNewDecisionVariable() ;
	
	public void setCurrentGeneration(int currentGeneration) {
		this.currentGeneration = currentGeneration;
	}
	
	public int getCurrentGeneration() {
		return currentGeneration;
	}
} // Problem
