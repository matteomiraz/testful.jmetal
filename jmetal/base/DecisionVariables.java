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

import jmetal.base.Configuration.SolutionType_;
import jmetal.base.Configuration.VariableType_;
import jmetal.base.variable.Binary;
import jmetal.base.variable.BinaryReal;
import jmetal.base.variable.Int;
import jmetal.base.variable.Permutation;
import jmetal.base.variable.Real;

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
  public DecisionVariables(Problem<T> problem, Class<T> varType){
    problem_   = problem;
    variables_ = new ArrayList<T>(problem_.getNumberOfVariables());

    if (varType.equals(Binary.class)) {
      for (int var = 0; var < problem_.getNumberOfVariables(); var++)
        variables_.set(var, new Binary(problem_.getLength(var)));       
    } 
    else if (varType.equals(Real.class)) {
      for (int var = 0; var < problem_.getNumberOfVariables(); var++)
      	variables_.set(var, new Real(problem_.getLowerLimit(var),
                                   problem_.getUpperLimit(var)));               
    }
    else if (problem.solutionType_ == SolutionType_.Int) {
      for (int var = 0; var < problem_.getNumberOfVariables(); var++)
      	variables_.set(var, new Int((int)problem_.getLowerLimit(var),
                                  (int)problem_.getUpperLimit(var)));    
    } 
    else if (problem.solutionType_ == SolutionType_.Permutation) {
      for (int var = 0; var < problem_.getNumberOfVariables(); var++)
      	variables_.set(var, new Permutation(problem_.getLength(var)));   
    } 
    else if (problem.solutionType_ == SolutionType_.BinaryReal) {
      for (int var = 0; var < problem_.getNumberOfVariables(); var++) {
        if (problem.getPrecision() == null) {
          int [] precision = new int[problem.getNumberOfVariables()] ;
          for (int i = 0; i < problem.getNumberOfVariables(); i++)
            precision[i] = jmetal.base.Configuration.DEFAULT_PRECISION ;
          problem.setPrecision(precision) ;
        } // if
        variables_.set(var, new BinaryReal(problem_.getPrecision(var),
                                         problem_.getLowerLimit(var),
                                         problem_.getUpperLimit(var)));   
      } // for 
    } 
    else if (problem.solutionType_ == SolutionType_.IntReal) {
      for (int var = 0; var < problem_.getNumberOfVariables(); var++)
        if (problem.variableType_[var] == VariableType_.Int)
        	variables_.set(var, new Int((int)problem_.getLowerLimit(var),
                                    (int)problem_.getUpperLimit(var))); 
        else if (problem.variableType_[var] == VariableType_.Real)
        	variables_.set(var, new Real(problem_.getLowerLimit(var),
                                     problem_.getUpperLimit(var)));  
        else {
          Configuration.logger_.severe("DecisionVariables.DecisionVariables: " +
              "error creating a Solution of type IntReal") ;
        } // else
    } 
    else {
      Configuration.logger_.severe("DecisionVariables.DecisionVariables: " +
          "the solutio type " + problem.solutionType_ + " is incorrect") ;
      //System.exit(-1) ;
    } // else
  } // DecisionVariable
   
  /**
   * Copy constructor
   * @param decisionVariables The <code>DecisionVariables<code> object to copy.
   */
  public DecisionVariables(DecisionVariables<T> decisionVariables){
    problem_ = decisionVariables.problem_;
    variables_ = new ArrayList<T>(decisionVariables.variables_.size());
    for (int var = 0; var < decisionVariables.variables_.size(); var++) {
      variables_.set(var, decisionVariables.variables_.get(var).clone());
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
