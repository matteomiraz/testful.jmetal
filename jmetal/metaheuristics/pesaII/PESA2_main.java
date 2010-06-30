/**
 * Main.java
 *
 * @author Juanjo Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.pesaII;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.base.Configuration;
import jmetal.base.EvaluationTerminationCriterion;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.crossover.SBXCrossover;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.mutation.PolynomialMutation;
import jmetal.base.variable.Real;
import jmetal.problems.Kursawe;
import jmetal.problems.ProblemFactory;
import jmetal.util.JMException;

public class PESA2_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  /**
   * @param args Command line arguments. The first (optional) argument specifies
   *             the problem to solve.
   * @throws JMException
   */
  @SuppressWarnings("unchecked")
	public static void main(String [] args) throws JMException, IOException {
    Problem<Real>   problem   ;         // The problem to solve
    PESA2<Real> algorithm ;         // The algorithm to use
    SBXCrossover crossover ;         // Crossover operator
    PolynomialMutation<Real>  mutation  ;         // Mutation operator

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("PESA2_main.log");
    logger_.addHandler(fileHandler_) ;

    if (args.length == 1) {
      Object [] params = {"Real"};
      problem = (Problem<Real>) ProblemFactory.getProblem(args[0],params);
    } // if
    else { // Default problem
      problem = new Kursawe(3, Real.class);
      //problem = new Kursawe(3,"BinaryReal");
      //problem = new Water("Real");
      //problem = new ZDT4("Real");
      //problem = new WFG1("Real");
      //problem = new DTLZ1("Real");
      //problem = new OKA2("Real") ;
    } // else

    algorithm = new PESA2<Real>(problem);

    // Algorithm parameters
    algorithm.setPopulationSize(10);
    algorithm.setArchiveSize(100);
    algorithm.setBiSections(5);
    algorithm.setTerminationCriterion(new EvaluationTerminationCriterion(25000));

    // Mutation and Crossover for Real codification
    crossover = (SBXCrossover) CrossoverFactory.getCrossoverOperator("SBXCrossover");
    crossover.setProbability(0.9);
    crossover.setDistributionIndex(20.0);

    mutation = (PolynomialMutation<Real>) MutationFactory.getMutationOperator("PolynomialMutation");
    mutation.setProbability(1.0/problem.getNumberOfVariables());
    mutation.setDistributionIndex(20.0);

    // Mutation and Crossover Binary codification
    /*
    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover");
    crossover.setParameter("probability",0.9);
    mutation = MutationFactory.getMutationOperator("BitFlipMutation");
    mutation.setParameter("probability",1.0/80);
    */

    // Add the operators to the algorithm
    algorithm.setCrossover(crossover);
    algorithm.setMutation(mutation);


    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet<Real> population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    System.out.println("Total execution time: "+estimatedTime);

    // Result messages
    logger_.info("Total execution time: "+estimatedTime);
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");
  }//main
}
