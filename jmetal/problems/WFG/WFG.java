/**
 * WFG.java
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.problems.WFG;

import java.util.List;
import java.util.Random;

import jmetal.base.ProblemValue;
import jmetal.base.variable.IReal;

/**
 * Implements a reference abstract class for all WFG test problems
 * Reference: Simon Huband, Luigi Barone, Lyndon While, Phil Hingston
 *            A Scalable Multi-objective Test Problem Toolkit.
 *            Evolutionary Multi-Criterion Optimization: 
 *            Third International Conference, EMO 2005. 
 *            Proceedings, volume 3410 of Lecture Notes in Computer Science
 */
public abstract class WFG<V extends IReal> extends ProblemValue<V> {
  
  private static final long serialVersionUID = -3511776882893579601L;

  private final Class<V> solutionType_;

	/**
   * stores a epsilon default value
   */
  private final float epsilon = (float)1e-7;
    
  protected int k_; //Var for walking fish group
  protected int M_;
  protected int l_;
  protected int [] A_;
  protected int [] S_;
  protected int D_ = 1;
  protected Random random = new Random();            
    
  /** 
  * Constructor
  * Creates a WFG problem
  * @param k position-related parameters
  * @param l distance-related parameters
  * @param M Number of objectives
  * @param solutionType The solution type must "Real" or "BinaryReal". 
  */
  public WFG (Integer k, Integer l, Integer M, Class<V> solutionType){      
    this.k_ = k.intValue();
    this.l_ = l.intValue();
    this.M_ = M.intValue();        
    numberOfVariables_  = this.k_ + this.l_;
    numberOfObjectives_ = this.M_;
    numberOfConstraints_ = 0;
                
    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];
    for (int var = 0; var < numberOfVariables_; var++) {
      lowerLimit_[var] = 0;
      upperLimit_[var] = 2 * (var + 1);
    }
      
    solutionType_ = solutionType; 
  } // WFG
    
  /**
   * Gets the x vector (consulte WFG tooltik reference)
   */
  public float [] calculate_x(float [] t){
    float [] x = new float[M_];
        
    for (int i = 0; i < M_-1; i++){
      x[i] = Math.max(t[M_-1],A_[i]) * (t[i]  - (float)0.5) + (float)0.5;
    }
        
    x[M_-1] = t[M_-1];
        
    return x;
  } // calculate_x
    
  /**
   * Normalizes a vector (consulte WFG toolkit reference)
   */
  public float [] normalise(float [] z){
    float [] result = new float[z.length];
        
    for (int i = 0; i < z.length; i++){
      float bound = (float)2.0 * (i + 1);
      result[i] = z[i] / bound;
      result[i] = correct_to_01(result[i]);
    }
        
    return result;
  } // normalize    
    
   
  /**
   */
  public float correct_to_01(float a){    
    float min = (float)0.0;
    float max = (float)1.0;

    float min_epsilon = min - epsilon;
    float max_epsilon = max + epsilon;

    if (( a <= min && a >= min_epsilon ) || (a >= min && a <= min_epsilon)) {
      return min;        
    } else if (( a >= max && a <= max_epsilon ) || (a <= max && a >= max_epsilon)) {
      return max;        
    } else {
      return a;        
    }
  } // correct_to_01  
   
  /**
  * Gets a subvector of a given vector
  * (Head inclusive and tail inclusive)
  * @param z the vector
  * @return the subvector
  */
  public float [] subVector(float [] z, int head, int tail){
    int size = tail - head + 1;
    float [] result = new float[size];
       
    for (int i = head; i <= tail; i++){
      result[i - head] = z[i];
    }
       
    return result;
  } // subVector

  /** 
  * Evaluates a solution 
  * @param variables The solution to evaluate
  * @return a double [] with the evaluation results
  */  
  abstract public float[] evaluate(float[] variables);
  // evaluate

  @Override
  public List<V> generateNewDecisionVariable() {
  	return generate(solutionType_);
  }
}
