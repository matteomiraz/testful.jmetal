/**
 * RandomSearch_main.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 *   A simple algorithm that perform a random search.
 */
package jmetal.metaheuristics.randomSearch;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.variable.Real;
import jmetal.problems.Kursawe;
import jmetal.problems.ProblemFactory;
import jmetal.util.JMException;

public class RandomSearch_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  /**
   * @param args Command line arguments.
   * @throws JMException
   * @throws IOException
   * @throws SecurityException
   * Usage: three options
   *      - jmetal.metaheuristics.randomSearch.RandomSearch_main
   *      - jmetal.metaheuristics.randomSearch.RandomSearch_main problemName
   */
  @SuppressWarnings("unchecked")
	public static void main(String [] args) throws
                                  JMException, SecurityException, IOException {
    Problem<Real>   problem   ;         // The problem to solve
    RandomSearch<Real> algorithm ;         // The algorithm to use

    if (args.length == 1) {
      Object [] params = {"Real"};
      problem = (Problem<Real>) ProblemFactory.getProblem(args[0],params);
    } // if
    else { // Default problem
      problem = new Kursawe(3, Real.class);
      //problem = new Kursawe(3,"BinaryReal");
      //problem = new Water("Real");
      //problem = new ZDT4(10, "Real");
      //problem = new WFG1("Real");
      //problem = new DTLZ1("Real");
      //problem = new OKA2("Real") ;
    } // else

    algorithm = new RandomSearch<Real>(problem);

    // Algorithm parameters
    algorithm.setInputParameter("maxEvaluations",25000);

    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet<Real> population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;

    // Result messages
    System.out.println(estimatedTime);
    //logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");
    //logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");

  } //main
} // Randomsearch_main
