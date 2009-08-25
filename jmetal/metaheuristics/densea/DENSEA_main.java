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

import jmetal.base.Configuration;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.crossover.SinglePointCrossoverBinary;
import jmetal.base.operator.mutation.BitFlipMutationBinary;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.BinaryTournament;
import jmetal.base.variable.Binary;
import jmetal.problems.RadioNetworkDesign;
import jmetal.util.JMException;

public class DENSEA_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object
  
  public static void main(String [] args) throws JMException, IOException {
    Problem<Binary>  problem   ;         // The problem to solve
    DENSEA<Binary> algorithm ;         // The algorithm to use
    SinglePointCrossoverBinary  crossover ;         // Crossover operator
    BitFlipMutationBinary  mutation  ;         // Mutation operator
    BinaryTournament<Binary>  selection ;         // Selection operator
      
    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("Densea.log"); 
    logger_.addHandler(fileHandler_) ;
    
    problem = new RadioNetworkDesign(149);
    
    algorithm = new DENSEA<Binary>(problem);
    
    // Algorithm parameters
    algorithm.setInputParameter("populationSize",100);
    algorithm.setInputParameter("maxEvaluations",25000);
    
    // Mutation and Crossover Binary codification 
    crossover = (SinglePointCrossoverBinary) CrossoverFactory.getCrossoverOperator("SinglePointCrossoverBinary");                   
    crossover.setProbability(0.9);                   
    mutation = (BitFlipMutationBinary) MutationFactory.getMutationOperator("BitFlipMutationBinary");                    
    mutation.setProbability(1.0/149);
    
    // Selection Operator 
    selection = new BinaryTournament<Binary>();                            
    
    // Add the operators to the algorithm
    algorithm.setCrossover(crossover);
    algorithm.setMutation(mutation);
    algorithm.setSelection(selection);
    
    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet<Binary> population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    System.out.println("Total time of execution: "+estimatedTime);

    // Log messages 
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");           
  }//main
}

