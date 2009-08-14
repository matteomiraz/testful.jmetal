/**
 * ES_main.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.metaheuristics.singleObjective.evolutionStrategy;

import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.problems.singleObjective.OneMax;
import jmetal.util.JMException;

/**
 * This class runs a single-objective Evolution Strategie (ES). The ES can be 
 * a (mu+lambda) ES (class ElitistES) or a (mu,lambda) ES (class NonElitistGA). 
 * The OneMax problem is used to test the algorithms.
 */
public class ES_main {

  public static void main(String [] args) throws JMException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    Operator  mutation  ;         // Mutation operator
            
    int bits ; // Length of bit string in the OneMax problem
    
    bits = 512 ;
    problem = new OneMax(bits);
    
    int mu     ; 
    int lambda ; 
    
    // Requirement: lambda must be divisible by mu
    mu     = 1  ;
    lambda = 10 ;
    
    algorithm = new ElitistES(problem, mu, lambda);
    //algorithm = new NonElitistES(problem, mu, lambda);
    
    /* Algorithm params*/
    algorithm.setInputParameter("maxEvaluations", 20000);
    
    /* Mutation and Crossover for Real codification */
    mutation = MutationFactory.getMutationOperator("BitFlipMutation");                    
    mutation.setParameter("probability",1.0/bits); 
    
    algorithm.addOperator("mutation",mutation);
 
    /* Execute the Algorithm */
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    System.out.println("Total execution time: "+estimatedTime);

    /* Log messages */
    System.out.println("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    System.out.println("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");          
  }//main

} // SSGA_main
