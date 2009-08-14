/**
 * Kursawe.java
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.problems;

import java.util.List;

import jmetal.base.DecisionVariables;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.variable.IReal;
import jmetal.util.JMException;

/**
 * Class representing problem Kursawe
 */
public class Kursawe<T extends IReal> extends Problem<T> {  
    
  private static final long serialVersionUID = 6446864010464428543L;
  
	private final Class<T> solutionType;

	/** 
   * Constructor.
   * Creates a default instance of the Kursawe problem.
   * @param solutionType The solution type must "Real" or "BinaryReal". 
   */
  public Kursawe(Class<T> solutionType) {
    this(3, solutionType);
  } // Kursawe
  
  /** 
   * Constructor.
   * Creates a new instance of the Kursawe problem.
   * @param numberOfVariables Number of variables of the problem 
   * @param solutionType The solution type must "Real" or "BinaryReal". 
   */
  public Kursawe(Integer numberOfVariables, Class<T> solutionType) {
  	this.solutionType = solutionType;
  	
  	numberOfVariables_   = numberOfVariables.intValue() ;
    numberOfObjectives_  = 2                            ;
    numberOfConstraints_ = 0                            ;
    problemName_         = "Kursawe"                    ;
        
    upperLimit_ = new double[numberOfVariables_] ;
    lowerLimit_ = new double[numberOfVariables_] ;
       
    for (int i = 0; i < numberOfVariables_; i++) {
      lowerLimit_[i] = -5.0 ;
      upperLimit_[i] = 5.0  ;
    } // for
        
  } // Kursawe
    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */
  public void evaluate(Solution<T> solution) throws JMException {
  	DecisionVariables<T> decisionVariables  = solution.getDecisionVariables();

  	double aux, xi, xj           ; // auxiliar variables
  	double [] fx = new double[2] ; // function values     

  	fx[0] = 0.0 ; 
  	for (int var = 0; var < numberOfVariables_ - 1; var++) {        
  		xi = decisionVariables.variables_.get(var).getValue()   * 
  		decisionVariables.variables_.get(var).getValue();
  		xj = decisionVariables.variables_.get(var+1).getValue() * 
  		decisionVariables.variables_.get(var+1).getValue();
  		aux = (-0.2) * Math.sqrt(xi + xj);
  		fx[0] += (-10.0) * Math.exp(aux);
  	} // for

  	fx[1] = 0.0;

  	for (int var = 0; var < numberOfVariables_ ; var++) {
  		fx[1] += Math.pow(Math.abs(decisionVariables.variables_.get(var).getValue()),
  				0.8) + 
  				5.0 * Math.sin(Math.pow(decisionVariables.variables_.get(var).getValue(),
  						3.0));
  	} // for

  	solution.setObjective(0, fx[0]);
  	solution.setObjective(1, fx[1]);
  } // evaluate

  @Override
  public List<T> generateNewDecisionVariable()  {
  	return generate(solutionType);
  }
} // Kursawe
