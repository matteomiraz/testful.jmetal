/**
 * Configuration.java
 * @author Juan J. Durillo
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.base;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * This class contain types and constant definitions
 */
public class Configuration implements Serializable {
  
 private static final long serialVersionUID = -3978998639401276254L;

 /**
  * Defines the default number of bits used for binary coded variables.
  */
  public static final int DEFAULT_PRECISION = 30;   
  
  /** 
   * Logger object 
   */
  public static Logger logger_ = Logger.getLogger("jMetal");
    
} // Configuration
