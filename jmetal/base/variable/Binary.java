/**
 * Binary.java
 *
 * @author Juanjo Durillo
 * @version 1.0
 *
 */
package jmetal.base.variable;

import java.util.BitSet;

import jmetal.base.VariableValue;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class implements a generic binary string variable.It can be used as
 * a base class other binary string based classes (e.g., binary coded integer
 * or real variables).
 */ 
public class Binary implements VariableValue {
  
  private static final long serialVersionUID = -7777100119877645273L;

	/**
   * Stores the bits constituting the binary string. It is
   * implemented using a BitSet object
   */
  public BitSet bits_;
  
  /**
   * Store the length of the binary string
   */
  protected int numberOfBits_;
        
  /**
   * Default constructor.
   */
  public Binary() {       
  } //Binary

  
  /**
   *  Constructor
   *  @param numberOfBits Length of the bit string
   */
  public Binary(int numberOfBits){
   
    numberOfBits_ = numberOfBits;

    bits_ = new BitSet(numberOfBits_);      
    for (int i = 0; i < numberOfBits_; i++){
    	bits_.set(i, PseudoRandom.randDouble() < 0.5);
    }
  } //Binary
  
  /**
   * Copy constructor.
   * @param variable The Binary variable to copy.
   */
  public Binary(Binary variable){

    numberOfBits_ = variable.numberOfBits_;
        
    bits_ = new BitSet(numberOfBits_);
    for (int i = 0; i < numberOfBits_; i++) {
      bits_.set(i,variable.bits_.get(i));      
    }
  } //Binary

  /**
   * This method is intended to be used in subclass of <code>Binary</code>, 
   * for examples the classes, <code>BinaryReal</code> and <code>BinaryInt<codes>. 
   * In this classes, the method allows to decode the 
   * value enconded in the binary string. As generic variables do not encode any
   * value, this method do noting 
   */
  public void decode() {
    //do nothing
  } //decode
  
  /** 
   * Creates an exact copy of a Binary object
   * @return An exact copy of the object.
   **/
  public VariableValue clone() {
    return new Binary(this);
  } //deepCopy

  /**
   * Returns the length of the binary string.
   * @return The length
   */
  public int getNumberOfBits(){
    return numberOfBits_;
  } //getNumberOfBits
  
  /**
   * Returns the value of the ith bit.
   * @param bit The bit to retrieve
   * @return The ith bit
   */
  public boolean getIth(int bit){
    return bits_.get(bit);
  } //getNumberOfBits

  /**
   * Sets the value of the ith bit.
   * @param bit The bit to set
   */
  public void setIth(int bit, boolean value){
    bits_.set(bit, value) ;
  } //getNumberOfBits

  
 /**
  * Obtain the hamming distance between two binary strings
  * @param other The binary string to compare
  * @return The hamming distance
  */
  public int hammingDistance(Binary other) {
    int distance = 0;
    int i = 0;
    while (i < bits_.size()) {
      if (bits_.get(i) != other.bits_.get(i)) {
        distance++;
      }
      i++;
    }
    return distance;
  } // hammingDistance

 /**
  *  
  */
  public String toString() {
    String result ;
    
    result = "" ;
    for (int i = 0; i < numberOfBits_; i ++)
      if (bits_.get(i))
        result = result + "1" ;
      else
        result = result + "0" ;
        
    return result ;
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

} // Binary
