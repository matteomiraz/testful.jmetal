/**
 * DecisionVariables.java
 * 
 * @author Juan J. durillo
 * @version 1.0 
 */
package jmetal.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 
 * This class contains the decision variables of a solution
 */
public class DecisionVariables<T extends Variable> implements Serializable {  
  private static final long serialVersionUID = 4466133386183087847L;

	/**
   * Stores the decision variables of a solution
   */
  public List<T> variables_;
  
  /**
   * The problem to solve
   */
  private Problem<T> problem_;
  
  /**
   * Constructor
   * @param problem The problem to solve
   */
  public DecisionVariables(Problem<T> problem) {
    problem_   = problem;
    variables_ = problem_.generateNewDecisionVariable();
  } // DecisionVariable
   
  /**
   * Copy constructor
   * @param decisionVariables The <code>DecisionVariables<code> object to copy.
   */
  @SuppressWarnings("unchecked")
	public DecisionVariables(DecisionVariables<T> decisionVariables){
    problem_ = decisionVariables.problem_;
    variables_ = new ArrayList<T>(decisionVariables.variables_.size());
    for (int var = 0; var < decisionVariables.variables_.size(); var++) {
      variables_.add((T) decisionVariables.variables_.get(var).clone());
    }
  } // DecisionVariable
    
  /**
   * Returns the number of decision variables.
   * @return The number of decision variables.
   */
  public int size(){
    return problem_.getNumberOfVariables();
  } // size

    
  /** Returns a String that represent the DecisionVariable
   * @return The string.
   */
  public String toString() {
  	StringBuilder sb = new StringBuilder();

  	for(T v : variables_)
  		sb.append(v.toString());

  	return sb.toString();
  } // toString
} // DecisionVariables
