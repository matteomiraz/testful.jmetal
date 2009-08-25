/**
 * GA_main.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */

package jmetal.metaheuristics.singleObjective.geneticAlgorithm;

import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.crossover.SBXCrossover;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.mutation.PolynomialMutation;
import jmetal.base.operator.selection.BinaryTournament;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.base.variable.Real;
import jmetal.problems.singleObjective.Griewank;
import jmetal.util.JMException;

/**
 * This class runs a single-objective genetic algorithm (GA). The GA can be 
 * a steady-state GA (class SSGA) or a generational GA (class GGA). The OneMax
 * problem is used to test the algorithms.
 */
public class GA_main {

  @SuppressWarnings("unchecked")
	public static void main(String [] args) throws JMException {
    Problem<Real>   problem   ;         // The problem to solve
    GGA<Real> algorithm ;         // The algorithm to use
    SBXCrossover  crossover ;         // Crossover operator
    PolynomialMutation<Real>  mutation  ;         // Mutation operator
    BinaryTournament<Real>  selection ;         // Selection operator
            
    //problem = new OneMax(bits);
    //problem = new Sphere(20, "Real") ;
    //problem = new Easom("Real") ;
    problem = new Griewank(20, Real.class) ;
    
    // algorithm = new SSGA(problem);
    algorithm = new GGA<Real>(problem) ;
    
    /* Algorithm parameters*/
    algorithm.setInputParameter("populationSize",100);
    algorithm.setInputParameter("maxEvaluations",100000);
    
    // Mutation and Crossover for Real codification 
    crossover = (SBXCrossover) CrossoverFactory.getCrossoverOperator("SBXCrossover");                   
    crossover.setProbability(1.0);                   
    crossover.setDistributionIndex(10.0);

    mutation = (PolynomialMutation<Real>) MutationFactory.getMutationOperator("PolynomialMutation");                    
    mutation.setProbability(1.0/problem.getNumberOfVariables());
    mutation.setDistributionIndex(10.0);    
    
    /**
    // Mutation and Crossover for Binary codification 
    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover");                   
    crossover.setParameter("probability",0.95);                   
    mutation = MutationFactory.getMutationOperator("BitFlipMutation");                    
    mutation.setParameter("probability",1.0/bits); 
    */
    
    /* Selection Operator */
    selection = (BinaryTournament<Real>) SelectionFactory.getSelectionOperator("BinaryTournament") ;                            
    
    /* Add the operators to the algorithm*/
    algorithm.setCrossover(crossover);
    algorithm.setMutation(mutation);
    algorithm.setSelection(selection);
 
    /* Execute the Algorithm */
    long initTime = System.currentTimeMillis();
    SolutionSet<Real> population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    System.out.println("Total execution time: " + estimatedTime);

    /* Log messages */
    System.out.println("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    System.out.println("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");          
  }//main

} // GA_main
