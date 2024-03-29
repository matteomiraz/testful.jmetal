/**
 * CellDE_main.java
 *
 * @author Juan J. Durillo
 * @author Antonio J. Nebro
 * @version 1.0
 * 
 * This algorithm is described in:
 *   J.J. Durillo, A.J. Nebro, F. Luna, E. Alba "Solving Three-Objective 
 *   Optimization Problems Using a new Hybrid Cellular Genetic Algorithm". 
 *   To be presented in: PPSN'08. Dortmund. September 2008. 
 */
package jmetal.metaheuristics.cellde;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.base.Configuration;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.crossover.DifferentialEvolutionCrossover;
import jmetal.base.operator.selection.BinaryTournament;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.base.variable.Real;
import jmetal.problems.Kursawe;
import jmetal.problems.ProblemFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

public class CellDE_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  /**
   * @param args Command line arguments. The first (optional) argument specifies 
   *      the problem to solve.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   */
  @SuppressWarnings("unchecked")
	public static void main(String [] args) throws 
                                 JMException, SecurityException, IOException {
    Problem<Real>  problem   ;         // The problem to solve
    BinaryTournament<Real>  selection ;
    DifferentialEvolutionCrossover  crossover ;
    CellDE<Real, BinaryTournament<Real>> algorithm ;         // The algorithm to use
    
    QualityIndicator<Real> indicators ; // Object to get quality indicators

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("MOCell_main.log");
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
      //problem = new Kursawe(3,"BinaryReal");
      //problem = new Water("Real");
      //problem = new ZDT4("Real");
      //problem = new WFG1("Real");
      //problem = new DTLZ1("Real");
      //problem = new OKA2("Real") ;
    } // else
    
    algorithm = new CellDE<Real, BinaryTournament<Real>>(problem);
    
    // Algorithm parameters
    algorithm.setPopulationSize(100);
    algorithm.setArchiveSize(100);
    algorithm.setMaxEvaluations(25000);
    algorithm.setFeedBack(20);
    
    // Crossover operator 
    crossover = (DifferentialEvolutionCrossover) CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover");                   
    crossover.setCR(0.5);                   
    crossover.setF(0.5);
    
    // Add the operators to the algorithm
    selection = (BinaryTournament<Real>) SelectionFactory.getSelectionOperator("BinaryTournament") ; 

    algorithm.setCrossover(crossover);
    algorithm.setSelection(selection);
    
    // Execute the Algorithm 
    long initTime = System.currentTimeMillis();
    SolutionSet<Real> population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    System.out.println("Total execution time: "+estimatedTime);

    // Log messages 
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
    } // if
  }//main
} // CellDE_main
