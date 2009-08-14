/*
 * Main.java
 *
 * @author Juanjo Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.densea;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.base.Algorithm;
import jmetal.base.Configuration;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.BinaryTournament;
import jmetal.base.operator.selection.Selection;
import jmetal.problems.RadioNetworkDesign;
import jmetal.util.JMException;

public class DENSEA_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object
  
  public static void main(String [] args) throws JMException, IOException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    Crossover  crossover ;         // Crossover operator
    Mutation  mutation  ;         // Mutation operator
    Selection<?>  selection ;         // Selection operator
      
    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("Densea.log"); 
    logger_.addHandler(fileHandler_) ;
    
    problem = new RadioNetworkDesign(149);
    
    algorithm = new DENSEA(problem);
    
    // Algorithm parameters
    algorithm.setInputParameter("populationSize",100);
    algorithm.setInputParameter("maxEvaluations",25000);
    
    // Mutation and Crossover Binary codification 
    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover");                   
    crossover.setProbability(0.9);                   
    mutation = MutationFactory.getMutationOperator("BitFlipMutation");                    
    mutation.setProbability(1.0/149);
    
    // Selection Operator 
    selection = new BinaryTournament();                            
    
    // Add the operators to the algorithm
    algorithm.setCrossover(crossover);
    algorithm.setMutation(mutation);
    algorithm.setSelection(selection);
    
    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    System.out.println("Total time of execution: "+estimatedTime);

    // Log messages 
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");           
  }//main
}

