/*
 * Main.java
 *
 * @author Juanjo Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.paes;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.base.Algorithm;
import jmetal.base.Configuration;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.mutation.PolynomialMutation;
import jmetal.problems.Kursawe;
import jmetal.problems.ProblemFactory;
import jmetal.util.JMException;

public class PAES_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  /**
   * @param args Command line arguments. The first (optional) argument specifies 
   *             the problem to solve.
   * @throws JMException 
   */
  public static void main(String [] args) throws JMException, IOException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    PolynomialMutation  mutation  ;         // Mutation operator
    
    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("PAES_main.log");
    logger_.addHandler(fileHandler_) ;
    
    if (args.length == 1) {
      Object [] params = {"Real"};
      problem = (new ProblemFactory()).getProblem(args[0],params);
    } // if
    else if (args.length == 2) {
      Object [] params = {"Real"};
      problem = (new ProblemFactory()).getProblem(args[0],params);
    } // if
    else { // Default problem
      problem = new Kursawe(3, "Real"); 
      //problem = new Kursawe(3,"BinaryReal");
      //problem = new Water("Real");
      //problem = new ZDT4("Real");
      //problem = new WFG1("Real");
      //problem = new DTLZ1("Real");
      //problem = new OKA2("Real") ;
    } // else
    
    algorithm = new PAES(problem);
    
    // Algorithm parameters
    algorithm.setInputParameter("archiveSize",100);
    algorithm.setInputParameter("biSections",5);
    algorithm.setInputParameter("maxEvaluations",25000);
      
    // Mutation (Real variables)
    mutation = (PolynomialMutation) MutationFactory.getMutationOperator("PolynomialMutation");                    
    mutation.setProbability(1.0/problem.getNumberOfVariables());
    mutation.setDistributionIndex(20.0);
    
    // Mutation (BinaryReal variables)
    //mutation = MutationFactory.getMutationOperator("BitFlipMutation");                    
    //mutation.setParameter("probability",1.0/80);
    
    // Add the operators to the algorithm
    algorithm.setMutation(mutation);
    
    // Execute the Algorithm 
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    
    // Result messages 
    logger_.info("Total execution time: "+estimatedTime);
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");            
  }//main
}
