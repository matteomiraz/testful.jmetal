/**
 * MOEAD_main.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * 
 * This class executes the algorithm described in:
 *   H. Li and Q. Zhang, 
 *   "Multiobjective Optimization Problems with Complicated Pareto Sets,  MOEA/D 
 *   and NSGA-II" 
 *   IEEE Trans on Evolutionary Computation, April/2008. Accepted 
 */
package jmetal.metaheuristics.moead;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.base.Configuration;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.crossover.DifferentialEvolutionCrossover;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.mutation.PolynomialMutation;
import jmetal.base.variable.Real;
import jmetal.problems.Kursawe;
import jmetal.problems.ProblemFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

public class MOEAD_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  /**
   * @param args Command line arguments. The first (optional) argument specifies 
   *      the problem to solve.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   * Usage: three options
   *      - jmetal.metaheuristics.moead.MOEAD_main
   *      - jmetal.metaheuristics.moead.MOEAD_main problemName
   *      - jmetal.metaheuristics.moead.MOEAD_main problemName ParetoFrontFile
 
   */
  @SuppressWarnings("unchecked")
	public static void main(String [] args) throws JMException, SecurityException, IOException {
    Problem<Real>   problem   ;         // The problem to solve
    MOEAD<Real> algorithm ;         // The algorithm to use
    DifferentialEvolutionCrossover  crossover ;         // Crossover operator
    PolynomialMutation  mutation  ;         // Mutation operator
     
    QualityIndicator<Real> indicators ; // Object to get quality indicators

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("MOEAD.log"); 
    logger_.addHandler(fileHandler_) ;
    
    indicators = null ;
    if (args.length == 1) {
      Object [] params = {"Real"};
      problem = (Problem<Real>) ProblemFactory.getProblem(args[0],params);
    } // if
    else if (args.length == 2) {
      Object [] params = {"Real"};
      problem = (Problem<Real>) ProblemFactory.getProblem(args[0],params);
      indicators = new QualityIndicator<Real>(problem, args[1]) ;
    } // if
    else { // Default problem
      problem = new Kursawe(3, Real.class); 
      //problem = new Water("Real");
      //problem = new ZDT4("Real");
      //problem = new ZDT3("Real");
      //problem = new WFG1("Real");
      //problem = new DTLZ1(7,4,"Real");
      //problem = new OKA2("Real") ;
    } // else
    
    algorithm = new MOEAD<Real>(problem);
    
    // Algorithm parameters
    algorithm.setPopulationSize(300);
    algorithm.setMaxEvaluations(150000);
    
    // Crossover operator 
    crossover = (DifferentialEvolutionCrossover) CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover");                   
    crossover.setCR(1.0);                   
    crossover.setF(0.5);
    
    // Mutation operator
    mutation = (PolynomialMutation) MutationFactory.getMutationOperator("PolynomialMutation");                    
    mutation.setProbability(1.0/problem.getNumberOfVariables());
    mutation.setDistributionIndex(20.0);  
    
    algorithm.setCrossover(crossover);
    algorithm.setMutation(mutation);
    
    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet<Real> population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    
    // Result messages 
    logger_.info("Total execution time: "+estimatedTime + "ms");
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");      
    
    if (indicators != null) {
      logger_.info("Quality indicators") ;
      logger_.info("Hypervolume: " + indicators.getHypervolume(population)) ;
      logger_.info("GD         : " + indicators.getGD(population)) ;
      logger_.info("IGD        : " + indicators.getIGD(population)) ;
      logger_.info("Spread     : " + indicators.getSpread(population)) ;
      logger_.info("Epsilon    : " + indicators.getEpsilon(population)) ;
    } // if          
  } //main
} // MOEAD_main
