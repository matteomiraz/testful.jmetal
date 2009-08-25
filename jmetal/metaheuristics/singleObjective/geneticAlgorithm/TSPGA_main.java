/**
 * TSPGA_main.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.metaheuristics.singleObjective.geneticAlgorithm;

import java.io.FileNotFoundException;
import java.io.IOException;

import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.BinaryTournament;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.base.variable.Permutation;
import jmetal.problems.singleObjective.TSP;
import jmetal.util.JMException;

/**
 * This class runs a single-objective genetic algorithm (GA). The GA can be 
 * a steady-state GA (class SSGA) or a generational GA (class GGA). The TSP
 * is used to test the algorithms. The data files accepted as in input are from
 * TSPLIB.
 */
public class TSPGA_main {

  @SuppressWarnings("unchecked")
	public static void main(String [] args)  throws FileNotFoundException, 
                                                  IOException, JMException{
    Problem<Permutation>  problem   ;         // The problem to solve
    GGA<Permutation> algorithm ;         // The algorithm to use
    Crossover<Permutation>  crossover ;         // Crossover operator
    Mutation<Permutation>  mutation  ;         // Mutation operator
    BinaryTournament<Permutation>  selection ;         // Selection operator
            
    String problemName = "eil101.tsp" ;
    
    problem = new TSP(problemName);
    
    //algorithm = new SSGA(problem);
    algorithm = new GGA<Permutation>(problem) ;
    
    // Algorithm params
    algorithm.setInputParameter("populationSize",512);
    algorithm.setInputParameter("maxEvaluations",200000);
    
    // Mutation and Crossover for Real codification */
    crossover = (Crossover<Permutation>) CrossoverFactory.getCrossoverOperator("TwoPointsCrossover");
    //crossover = CrossoverFactory.getCrossoverOperator("PMXCrossover");
    crossover.setProbability(0.95);                   
    mutation = (Mutation<Permutation>) MutationFactory.getMutationOperator("SwapMutation");                    
    mutation.setProbability(0.2); 
  
    /* Selection Operator */
    selection = (BinaryTournament<Permutation>) SelectionFactory.getSelectionOperator("BinaryTournament") ;                            
    
    /* Add the operators to the algorithm*/
    algorithm.setCrossover(crossover);
    algorithm.setMutation(mutation);
    algorithm.setSelection(selection);

    /* Execute the Algorithm */
    long initTime = System.currentTimeMillis();
    SolutionSet<Permutation> population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    System.out.println("Total time of execution: "+estimatedTime);

    /* Log messages */
    System.out.println("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    System.out.println("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");          
  }//main
} // TSPGA_main
