/**
 * VariableFactory.java
 *
 * @author Juanjo Durillo
 * @version 1.0
 */
package jmetal.base.variable;

import jmetal.base.Configuration;
import jmetal.base.Variable;
import jmetal.util.JMException;

/**
 * This class is intended to be used as a static Factory to obtains variables. 
 */
public class VariableFactory {
    
  /** 
   * Obtains an instance of a <code>Variable</code> given its name.
   * @param name The name of the class from which we want to obtain an instance
   * object
   * @throws JMException 
   */
  public static Variable getVariable(String name) throws JMException{
    Variable variable   = null;
    String baseLocation = "jmetal.base.variable.";
    try {
      Class<?> c = Class.forName(baseLocation + name);
      variable = (Variable) c.newInstance();
      return variable;
    } catch (ClassNotFoundException e1) {
      Configuration.logger_.severe("VariableFactory.getVariable: ClassNotFoundException ");
      throw new JMException(e1) ;
    } catch (InstantiationException e2) {
      Configuration.logger_.severe("VariableFactory.getVariable: " +
      "InstantiationException ");
      throw new JMException(e2) ;
    } catch (IllegalAccessException e3) {
      Configuration.logger_.severe("VariableFactory.getVariable: " +
      "IllegalAccessException ");
      throw new JMException(e3) ;
    }
  } // getVariable      
} //VariabeFactory
