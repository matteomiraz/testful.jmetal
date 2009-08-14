/**
 * Problem.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * Created on 16 de octubre de 2006, 17:04
 */

package jmetal.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jmetal.base.variable.Binary;
import jmetal.base.variable.BinaryReal;
import jmetal.base.variable.Int;
import jmetal.base.variable.Permutation;
import jmetal.base.variable.Real;
import jmetal.util.JMException;

/**
 * Abstract class representing a multiobjective optimization problem
 */
public abstract class Problem<T extends Variable> implements Serializable {
  
  private static final long serialVersionUID = -6033690904402492022L;

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
  
  /**
   * Stores the lower bound values for each variable (only if needed)
   */
  protected double [] lowerLimit_ ;
  
  /**
   * Stores the upper bound values for each variable (only if needed)
   */
  protected double [] upperLimit_ ;
  
  /**
   * Stores the number of bits used by binary coded variables (e.g., BinaryReal
   * variables). By default, they are initialized to DEFAULT_PRECISION)
   */
  protected int    [] precision_  ;
    
  /**
   * Stores the length of each variable when applicable (e.g., Binary and 
   * Permutation variables)
   */
  protected int    [] length_  ;
  
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
   * Gets the lower bound of the ith variable of the problem.
   * @param i The index of the variable.
   * @return The lower bound.
   */
  public double getLowerLimit(int i) {
    return lowerLimit_[i] ;
  } // getLowerLimit
    
  /** 
   * Gets the upper bound of the ith variable of the problem.
   * @param i The index of the variable.
   * @return The upper bound.
   */
  public double getUpperLimit(int i) {
    return upperLimit_[i] ;
  } // getUpperLimit 
    
  /**
   * Evaluates a <code>Solution</code> object.
   * @param solution The <code>Solution</code> to evaluate.
   */    
  public abstract void evaluate(Solution<T> solution) throws JMException ;    
    
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
   * Returns the number of bits that must be used to encode variable.
   * @return the number of bits.
   */
  public int getPrecision(int var) {
    return precision_[var] ;
  } // getPrecision

  /**
   * Returns array containing the number of bits that must be used to encode 
   * the variables.
   * @return the number of bits.
   */
  public int [] getPrecision() {
    return precision_ ;
  } // getPrecision

  /**
   * Sets the array containing the number of bits that must be used to encode 
   * the variables.
   * @param precision The array
   */
  public void setPrecision(int [] precision) {
    precision_ = precision;
  } // getPrecision

  /**
   * Returns the length of the variable.
   * @return the variable length.
   */
  public int getLength(int var) {
    return length_[var] ;
  } // getLength

  /**
   * Returns the problem name
   * @return The problem name
   */
  public String getName() {
    return problemName_ ;
  }

	public abstract List<T> generateNewDecisionVariable() ;

	@SuppressWarnings("unchecked")
	protected List<T> generate(Class<T> type) {
		if(type == Binary.class) return (List<T>) generateBinary();
		if(type == Int.class) return (List<T>) generateInt();
		if(type == Real.class) return (List<T>) generateReal();
		if(type == Permutation.class) return (List<T>) generatePermutation();
		if(type == BinaryReal.class) return (List<T>) generateBinaryReal();
		
		new ClassCastException("Unknown type: " + type.getCanonicalName()).printStackTrace();
		System.exit(1);
		return null;
	}
	
 	protected List<Binary> generateBinary() {
		List<Binary> variables_ = new ArrayList<Binary>(getNumberOfVariables());
    for (int var = 0; var < getNumberOfVariables(); var++)
      variables_.add(new Binary(getLength(var)));       
		return variables_;
	}

	protected List<Int> generateInt() {
		List<Int> variables_ = new ArrayList<Int>(getNumberOfVariables());
    for (int var = 0; var < getNumberOfVariables(); var++)
    	variables_.add(new Int((int)getLowerLimit(var), (int)getUpperLimit(var)));    
		return variables_;
	}

	protected List<Real> generateReal() {
		List<Real> variables_ = new ArrayList<Real>(getNumberOfVariables());
    for (int var = 0; var < getNumberOfVariables(); var++)
    	variables_.add(new Real(getLowerLimit(var), getUpperLimit(var)));
    return variables_;
	}

	protected List<Permutation> generatePermutation() {
		List<Permutation> variables_ = new ArrayList<Permutation>(getNumberOfVariables());
    for (int var = 0; var < getNumberOfVariables(); var++)
    	variables_.add(new Permutation(getLength(var)));
    return variables_;
	}

	protected List<BinaryReal> generateBinaryReal() {
		int nVars = getNumberOfVariables();
    List<BinaryReal> variables_ = new ArrayList<BinaryReal>(nVars);

    for (int var = 0; var < nVars; var++) {
      if (getPrecision() == null) {
        int [] precision = new int[nVars] ;
        for (int i = 0; i < nVars; i++) precision[i] = jmetal.base.Configuration.DEFAULT_PRECISION ;
        setPrecision(precision) ;
      } // if
      variables_.add(new BinaryReal(getPrecision(var), getLowerLimit(var), getUpperLimit(var)));   
    } // for
    
    return variables_;
	}
	
	public static abstract class ProblemBinary extends Problem<Binary> {
		private static final long serialVersionUID = 5393143054088901698L;

		@Override
		public List<Binary> generateNewDecisionVariable() {
			return this.generateBinary();
		}
	}
	
	public static abstract class ProblemInt extends Problem<Int> {
		private static final long serialVersionUID = 7071547726520686287L;

		@Override
		public List<Int> generateNewDecisionVariable() {
			return generateInt();
		}
	}

	public static abstract class ProblemReal extends Problem<Real> {
		private static final long serialVersionUID = 3724179560263156055L;

		@Override
		public List<Real> generateNewDecisionVariable() {
			return generateReal();
		}
	}

	public static abstract class ProblemPermutation extends Problem<Permutation> {
		private static final long serialVersionUID = -218383516874692966L;

		@Override
		public List<Permutation> generateNewDecisionVariable() {
			return generatePermutation();
		}
	}
	
	public static abstract class ProblemBinaryReal extends Problem<BinaryReal> {
		private static final long serialVersionUID = 741667275879535410L;

		@Override
		public List<BinaryReal> generateNewDecisionVariable() {
	    return generateBinaryReal();
		}
	}

} // Problem
