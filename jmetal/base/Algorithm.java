/**
 * Algorithm.java
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.base ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
 */ 
public abstract class Algorithm implements Serializable {
   
 private static final long serialVersionUID = 170011594278842840L;

 protected Crossover crossoverOperator;
 protected Mutation mutationOperator;
 protected Selection<?> selectionOperator;
 protected LocalSearch improvement;

 // output parameters
 private int evaluations = -1;
 
 
 public void setEvaluations(int evaluations) {
	 this.evaluations = evaluations;
 }


 public int getEvaluations() {
	 return evaluations;
 }
 
 /** 
  * Stores algorithm specific parameters. For example, in NSGA-II these
  * parameters include the population size and the maximum number of function
  * evaluations.
  */
  protected Map<String,Object> inputParameters_ = null;  
  
 /**   
  * Launches the execution of an specific algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution  
  */
  public abstract SolutionSet execute() throws JMException ;   
  
  
 /**
  * Sets an input parameter to an algorithm. Typically,
  * the method is invoked by a Main object before running an algorithm. 
  * The parameters have to been inserted using their name to access them through 
  * the <code>getInputParameter</code> method.
  * @param name The parameter name
  * @param object Object that represent a parameter for the
  * algorithm.
  */
  public void setInputParameter(String name, Object object){
    if (inputParameters_ == null) {
      inputParameters_ = new HashMap<String,Object>();
    }        
    inputParameters_.put(name,object);
  } // setInputParameter  
  
 /**
  * Gets an input parameter through its name. Typically,
  * the method is invoked by an object representing an algorithm
  * @param name The parameter name
  * @return Object representing the parameter or null if the parameter doesn't
  * exist or the name is wrong
  */
  public Object getInputParameter(String name){
    return inputParameters_.get(name);
  } // getInputParameter
  
  public void setCrossover(Crossover crossover) {
  	this.crossoverOperator = crossover;
  }
  
	public void setMutation(Mutation mutation) {
		this.mutationOperator = mutation;
	}

	public void setSelection(Selection<?> selection) {
		this.selectionOperator = selection;
	}
	
	public void setImprovement(LocalSearch improvement) {
		this.improvement = improvement;
	}
} // Algorithm
