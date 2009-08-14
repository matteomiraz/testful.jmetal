/**
 * NSGAII_main.java
 *
 * @author Juan J. Durillo
 * @author Antonio J. Nebro
 * @version 1.0
 *   This implementation of jMetal makes use of a QualityIndicator object
 *   to obtained the convergence speed of the algorithm. This version is used
 *   in the paper:
 *     A.J. Nebro, J.J. Durillo, C.A. Coello Coello, F. Luna, E. Alba 
 *     "A Study of Convergence Speed in Multi-Objective Metaheuristics." 
 *     To be presented in: PPSN'08. Dortmund. September 2008.
 */
package jmetal.metaheuristics.nsgaII;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.base.Configuration;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.crossover.SBXCrossover;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.mutation.PolynomialMutation;
import jmetal.base.operator.selection.BinaryTournament2;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.base.variable.Real;
import jmetal.problems.Kursawe;
import jmetal.problems.ProblemFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

public class NSGAII_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  /**
   * @param args Command line arguments.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   * Usage: three options
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main problemName
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main problemName paretoFrontFile
   */
  @SuppressWarnings("unchecked")
	public static void main(String [] args) throws 
                                  JMException, SecurityException, IOException {
    Problem<Real>   problem   ;         // The problem to solve
    NSGAII<Real> algorithm ;         // The algorithm to use
    SBXCrossover  crossover ;         // Crossover operator
    PolynomialMutation mutation  ;         // Mutation operator
    BinaryTournament2<Real> selection ;         // Selection operator
    
    QualityIndicator<Real> indicators ; // Object to get quality indicators

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("NSGAII_main.log"); 
    logger_.addHandler(fileHandler_) ;
    
    indicators = null ;
    if (args.length == 1) {
      Object [] params = {Real.class};
      problem = (Problem<Real>) ProblemFactory.getProblem(args[0],params);
    } // if
    else if (args.length == 2) {
      Object [] params = {Real.class};
      problem = (Problem<Real>) ProblemFactory.getProblem(args[0],params);
      indicators = new QualityIndicator<Real>(problem, args[1]) ;
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
    
    algorithm = new NSGAII<Real>(problem);

    // Algorithm parameters
    algorithm.setInputParameter("populationSize",100);
    algorithm.setInputParameter("maxEvaluations",25000);

    // Mutation and Crossover for Real codification 
    crossover = (SBXCrossover) CrossoverFactory.getCrossoverOperator("SBXCrossover");                   
    crossover.setProbability(0.9);                   
    crossover.setDistributionIndex(20.0);

    mutation = (PolynomialMutation) MutationFactory.getMutationOperator("PolynomialMutation");                    
    mutation.setProbability(1.0/problem.getNumberOfVariables());
    mutation.setDistributionIndex(20.0);    

    // Selection Operator 
    selection = (BinaryTournament2<Real>) SelectionFactory.getSelectionOperator("BinaryTournament2") ;                           

    // Add the operators to the algorithm
    algorithm.setCrossover(crossover);
    algorithm.setMutation(mutation);
    algorithm.setSelection(selection);
    
    // Add the indicator object to the algorithm
    algorithm.setInputParameter("indicators", indicators) ;
    
    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet<Real> population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    
    // Result messages 
    logger_.info("Total execution time: "+estimatedTime + "ms");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");    
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
  
    if (indicators != null) {
      logger_.info("Quality indicators") ;
      logger_.info("Hypervolume: " + indicators.getHypervolume(population)) ;
      logger_.info("GD         : " + indicators.getGD(population)) ;
      logger_.info("IGD        : " + indicators.getIGD(population)) ;
      logger_.info("Spread     : " + indicators.getSpread(population)) ;
      logger_.info("Epsilon    : " + indicators.getEpsilon(population)) ;  
     
      logger_.info("Speed      : " + algorithm.getEvaluations() + " evaluations") ;      
    } // if
  } //main
} // NSGAII_main
