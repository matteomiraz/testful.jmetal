/**
 * Permutation.java
 *
 * @author juanjo durillo
 * @version 1.0
 */

package jmetal.base.variable;

import jmetal.base.VariableValue;
import jmetal.util.JMException;

/**
 * Implements a permutation of integer decision variable
 */
public class Permutation implements VariableValue {
  
  private static final long serialVersionUID = 207056540852484629L;

	/**
   * Stores a permutation of <code>int</code> values
   */
  public int [] vector_;
  
  /**
   * Stores the length of the permutation
   */
  public int size_;
  
  /**
   * Constructor
   */
  public Permutation() {
    size_   = 0;
    vector_ = null;

  } //Permutation

  /**
   * Constructor
   * @param size Length of the permutation
   */
  /*
  public Permutation(int size) {
	  setVariableType(VariableType_.Permutation) ;

	  size_   = size;
    vector_ = new int[size_];
    
    int [] randomSequence = new int[size_];
    
    for(int k = 0; k < size_; k++){
      int num           = PseudoRandom.randInt();
      randomSequence[k] = num;
      vector_[k]        = k;
    } 

    // sort value and store index as fragment order
    for(int i = 0; i < size_-1; i++){
      for(int j = i+1; j < size_; j++) {
        if(randomSequence[i] > randomSequence[j]){
          int temp          = randomSequence[i];
          randomSequence[i] = randomSequence[j];
          randomSequence[j] = temp;

          temp       = vector_[i];
          vector_[i] = vector_[j];
          vector_[j] = temp;
        }
      }
    }
  } //Permutation
   * */
  
 /**
  * Constructor
  * @param size Length of the permutation
  * This constructor has been contributed by Madan Sathe
  */
  public Permutation(int size) {
		size_   = size;
		vector_ = new int[size_];

		java.util.ArrayList<Integer> randomSequence = new
                                       java.util.ArrayList<Integer>(size_);

		for(int i = 0; i < size_; i++)
			randomSequence.add(i);

		java.util.Collections.shuffle(randomSequence);

		for(int j = 0; j < randomSequence.size(); j++)
			vector_[j] = randomSequence.get(j);
  } // Constructor
    
  
  /** 
   * Copy Constructor
   * @param permutation The permutation to copy
   */
  public Permutation(Permutation permutation) {
    size_   = permutation.size_;
    vector_ = new int[size_];
    
    for (int i = 0; i < size_; i++) {
      vector_[i] = permutation.vector_[i];
    }
  } //Permutation

  
  /** 
   * Create an exact copy of the <code>Permutation</code> object.
   * @return An exact copy of the object.
   */
  public VariableValue clone() {
    return new Permutation(this);
  } //deepCopy
  
  /**
   * Returns the length of the permutation.
   * @return The length
   */
  public int getLength(){
    return size_;
  } //getNumberOfBits
  
  /**
   * Returns a string representing the object
   * @return The string
   */ 
  public String toString(){
    String string ;
     
    string = "" ;
    for (int i = 0; i < size_ ; i ++)
      string += vector_[i] + " " ;
      
    return string ;
  } // toString  

	public double getLowerBound() throws JMException {
		throw new JMException("Not Implemented");
	}

	public double getUpperBound() throws JMException {
		throw new JMException("Not Implemented");
	}

	public double getValue() throws JMException {
		throw new JMException("Not Implemented");
	}

	public void setLowerBound(double lowerBound) throws JMException {
		throw new JMException("Not Implemented");
	}

	public void setUpperBound(double upperBound) throws JMException {
		throw new JMException("Not Implemented");
	}

	public void setValue(double value) throws JMException {
		throw new JMException("Not Implemented");
	}

}
