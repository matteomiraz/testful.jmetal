/**
 * SMPSO_main.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.metaheuristics.smpso;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.base.Configuration;
import jmetal.base.EvaluationTerminationCriterion;
import jmetal.base.IterationTerminationCriterion;
import jmetal.base.ProblemValue;
import jmetal.base.SolutionSet;
import jmetal.base.variable.Real;
import jmetal.problems.Kursawe;
import jmetal.problems.ProblemFactory;
import jmetal.util.JMException;

public class SMPSO_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  /**
   * @param args Command line arguments. The first (optional) argument specifies
   *             the problem to solve.
   * @throws JMException
   */
  @SuppressWarnings("unchecked")
	public static void main(String [] args) throws JMException, IOException {
    ProblemValue<Real>   problem   ;         // The problem to solve
    SMPSO<Real> algorithm ;         // The algorithm to use

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("SMPSO_main.log");
    logger_.addHandler(fileHandler_) ;

    if (args.length == 1) {
      Object [] params = {"Real"};
      problem = (ProblemValue<Real>) ProblemFactory.getProblem(args[0],params);
    } // if
    else if (args.length == 2) {
      Object [] params = {"Real"};
      problem = (ProblemValue<Real>) ProblemFactory.getProblem(args[0],params);
    } // if
    else { // Default problem
      problem = new Kursawe(3, Real.class);
      //problem = new Kursawe(3,"BinaryReal");
      //problem = new Water("Real");
      //problem = new ZDT4("Real");
      //problem = new WFG1("Real");
      //problem = new DTLZ1("Real");
      //problem = new OKA2("Real") ;
    }

    algorithm = new SMPSO<Real>(problem) ;

    // Algorithm parameters
    algorithm.setSwarmSize(100);
    algorithm.setArchiveSize(100);
    algorithm.setTerminationCriterion(new IterationTerminationCriterion(250));
//    algorithm.setPerturbationIndex(0.5);
    algorithm.setMutationDistributionIndex(20.0);

    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet<Real> population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;

    // Result messages
    logger_.info("Total execution time: "+estimatedTime);
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");

  }//main
}
