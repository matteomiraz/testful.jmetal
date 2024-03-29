/**
 * Variable.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base;

import java.io.Serializable;

import jmetal.util.JMException;

/**
 * This abstract class is the base for defining new types of variables.
 * Many methods of <code>Variable</code> (<code>getValue</code>,
 * <code>setValue</code>,<code>
 * getLowerLimit</code>,<code>setLowerLimit</code>,<code>getUpperLimit</code>,
 * <code>setUpperLimit</code>)
 * are not applicable to all the subclasses of <code>Variable</code>.
 * For this reason, they are defined by default as giving a fatal error.
 */
public interface VariableValue extends Serializable, Variable {
  /** 
   * Creates an exact copy of a <code>Variable</code> object.
   * @return the copy of the object.
   */
  public abstract VariableValue clone();

  /**
   * Gets the double value representign the variable. 
   * It is used in subclasses of <code>Variable</code> (i.e. <code>Real</code> 
   * and <code>Int</code>).
   * As not all objects belonging to a subclass of <code>Variable</code> have a 
   * double value, a call to this method it is considered a fatal error by 
   * default, and the program is terminated. Those classes requiring this method 
   * must redefine it.
   */
  public double getValue() throws JMException;
  
  /**
  * Sets a double value to a variable in subclasses of <code>Variable</code>. 
  * As not all objects belonging to asubclass of <code>Variable</code> have a 
  * double value, a call to this method it is considered a fatal error by 
  * default, and the program is terminated. Those classes requiring this method 
  * must redefine it.
  */
  public void setValue(double value) throws JMException;

  /**
   * Gets the lower bound value of a variable. As not all
   * objects belonging to a subclass of <code>Variable</code> have a lower bound,
   * a call to this method is considered a fatal error by default,
   * and the program is terminated.
   * Those classes requiring this method must redefine it.
   */
  public double getLowerBound() throws JMException;
  
  /**
   * Gets the upper bound value of a variable. As not all
   * objects belonging to a subclass of <code>Variable</code> have an upper 
   * bound, a call to this method is considered a fatal error by default, and the 
   * program is terminated. Those classes requiring this method mustredefine it.
   */
  public double getUpperBound() throws JMException;
  
  /**
   * Sets the lower bound for a variable. As not all objects beloging to a
   * subclass of <code>Variable</code> have a lower bound, a call to this method 
   * is considered a fatal error by defaultm and the program is terminated.
   * Those classes requiring this method must to redefine it.
   */
  public void setLowerBound(double lowerBound) throws JMException;
  
  /**
   * Sets the upper bound for a variable. As not all objects belongig to a 
   * subclass of <code>Variable</code> have an upper bound, a call to this method
   * is considered a fatal error by default, and the program is terminated. 
   * Those classes requiring this method must redefine it.
   */
  public void setUpperBound(double upperBound) throws JMException;
} // Variable
