/**
 * Main.java
 *
 * @author Juanjo Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.fastPGA;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.base.Algorithm;
import jmetal.base.Configuration;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.operator.comparator.FPGAFitnessComparator;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.crossover.SBXCrossover;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.mutation.PolynomialMutation;
import jmetal.base.operator.selection.BinaryTournament;
import jmetal.base.operator.selection.Selection;
import jmetal.problems.Kursawe;
import jmetal.problems.ProblemFactory;
import jmetal.util.JMException;

public class FastPGA_main {
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
    SBXCrossover  crossover ;         // Crossover operator
    PolynomialMutation  mutation  ;         // Mutation operator
    Selection<?> selection ;         // Selection operator

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("FastPGA_main.log"); 
    logger_.addHandler(fileHandler_) ;
  
    if (args.length == 1) {
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

    algorithm = new FastPGA(problem);

    algorithm.setInputParameter("maxPopSize",100);
    algorithm.setInputParameter("initialPopulationSize",100);
    algorithm.setInputParameter("maxEvaluations",25000);
    algorithm.setInputParameter("a",20.0);
    algorithm.setInputParameter("b",1.0);
    algorithm.setInputParameter("c",20.0);
    algorithm.setInputParameter("d",0.0);

    // Parameter "termination"
    // If the preferred stopping criterium is PPR based, termination must 
    // be set to 0; otherwise, if the algorithm is intended to iterate until 
    // a give number of evaluations is carried out, termination must be set to 
    // that number
    algorithm.setInputParameter("termination",1);

    // Mutation and Crossover for Real codification 
    crossover = (SBXCrossover) CrossoverFactory.getCrossoverOperator("SBXCrossover");                   
    crossover.setProbability(1.0);                   
    crossover.setDistributionIndex(20.0);


    mutation = (PolynomialMutation) MutationFactory.getMutationOperator("PolynomialMutation");                    
    mutation.setProbability(1.0/problem.getNumberOfVariables());
    mutation.setDistributionIndex(20.0);
    
    // Mutation and Crossover for Binary codification
    /*
    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover");                   
    crossover.setParameter("probability",1.0);  
    mutation = MutationFactory.getMutationOperator("BitFlipMutation");                    
    mutation.setParameter("probability",1.0/149.0);    
     */

    selection = new BinaryTournament(new FPGAFitnessComparator());  

    algorithm.setCrossover(crossover);
    algorithm.setMutation(mutation);
    algorithm.setSelection(selection);

    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;

    // Result messsages
    logger_.info("Total execution time: "+estimatedTime);
    logger_.info("Total number of evaluations: " + algorithm.getEvaluations());
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");          
  }//main
}
