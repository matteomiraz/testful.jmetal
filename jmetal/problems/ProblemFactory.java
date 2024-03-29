/**
 * ProblemFactory.java
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.problems;

import java.lang.reflect.Constructor;

import jmetal.base.Configuration;
import jmetal.base.Problem;
import jmetal.util.JMException;

/**
 * This class represents a factory for problems
 */
public class ProblemFactory {
  /**
   * Creates an object representing a problem
   * @param name Name of the problem
   * @param params Parameters characterizing the problem
   * @return The object representing the problem
   * @throws JMException 
   */
  public static Problem<?> getProblem(String name, Object [] params) throws JMException {
    // Params are the arguments
    // The number of argument must correspond with the problem constructor params
    
    String base = "jmetal.problems.";
    if (name.substring(0,name.length()-1).equalsIgnoreCase("DTLZ"))
      base += "DTLZ.";
    else if (name.substring(0,name.length()-1).equalsIgnoreCase("WFG"))
      base += "WFG.";
    else if (name.substring(0,name.length()-1).equalsIgnoreCase("ZDT"))
      base += "ZDT.";    
    else if (name.substring(0,name.length()-3).equalsIgnoreCase("ZZJ07"))
      base += "ZZJ07.";        
    else if (name.substring(0,name.length()-3).equalsIgnoreCase("LZ09"))
      base += "LZ09.";        
    else if (name.substring(0,name.length()-4).equalsIgnoreCase("ZZJ07"))
        base += "ZZJ07.";    
    else if (name.substring(0,name.length()-3).equalsIgnoreCase("LZ06"))
      base += "LZ06.";    
    else if (name.substring(0,name.length()-4).equalsIgnoreCase("CEC2009"))
      base += "cec2009Competition.";    
    else if (name.substring(0,name.length()-5).equalsIgnoreCase("CEC2009"))
      base += "cec2009Competition.";    
    
    try {
      Class<?> problemClass = Class.forName(base+name);
      Constructor<?> [] constructors = problemClass.getConstructors();
      int i = 0;
      //find the constructor
      while ((i < constructors.length) && 
             (constructors[i].getParameterTypes().length!=params.length)) {
        i++;
      }
      // constructors[i] is the selected one constructor
      Problem<?> problem = (Problem<?>)constructors[i].newInstance(params);
      return problem;      
    }// try
    catch(Exception e) {
      Configuration.logger_.severe("ProblemFactory.getProblem: " +
          "Problem '"+ name + "' does not exist. "  +
          "Please, check the problem names in jmetal/problems") ;
      throw new JMException("Exception in " + name + ".getProblem()") ;
    } // catch            
  }    
}
