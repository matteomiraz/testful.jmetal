/**
 * AbYSS_main.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 *
 * This class executes the algorithm described in:
 *   A.J. Nebro, F. Luna, E. Alba, B. Dorronsoro, J.J. Durillo, A. Beham
 *   "AbYSS: Adapting Scatter Search to Multiobjective Optimization."
 *   Accepted for publication in IEEE Transactions on Evolutionary Computation.
 *   July 2007
 */
package jmetal.metaheuristics.abyss;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.base.Configuration;
import jmetal.base.EvaluationTerminationCriterion;
import jmetal.base.ProblemValue;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.crossover.SBXCrossover;
import jmetal.base.operator.localSearch.MutationLocalSearch;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.mutation.PolynomialMutation;
import jmetal.base.variable.Real;
import jmetal.problems.Kursawe;
import jmetal.problems.ProblemFactory;
import jmetal.util.JMException;
/**
 * This class is the main program used to configure and run AbYSS, a
 * multiobjective scatter search metaheuristics.
 * Reference: A.J. Nebro, F. Luna, E. Alba, A. Beham, B. Dorronsoro "AbYSS:
 *            Adapting Scatter Search for Multiobjective Optimization".
 *            TechRep. ITI-2006-2, Departamento de Lenguajes y Ciencias de la
 *            Computacion, University of Malaga.
 * Comments: AbYSS is configured to work only with continuous decision
 *           variables.
 */
public class AbYSS_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  /**
   * @param args Command line arguments. The first (optional) argument specifies
   *             the problem to solve.
   * @throws JMException
   */
  @SuppressWarnings("unchecked")
	public static void main(String [] args) throws
                                 JMException, SecurityException, IOException {
    ProblemValue<Real>   problem     ; // The problem to solve
    SBXCrossover crossover   ; // Crossover operator
    PolynomialMutation mutation    ; // Mutation operator
    MutationLocalSearch<Real> improvement ; // Operator for improvement
    AbYSS<Real> algorithm   ; // The algorithm to use

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("AbySS.log");
    logger_.addHandler(fileHandler_) ;

    // STEP 1. Select the multiobjective optimization problem to solve
    if (args.length == 1) {
      Object [] params = {"Real"};
      problem = (ProblemValue<Real>) ProblemFactory.getProblem(args[0], params);
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

    // STEP 2. Select the algorithm (AbYSS)
    algorithm = new AbYSS<Real>(problem) ;

    // STEP 3. Set the input parameters required by the metaheuristic
    algorithm.setPopulationSize(20);
    algorithm.setRefSet1Size(10);
    algorithm.setRefSet2Size(10);
    algorithm.setArchiveSize(100);
    algorithm.setTerminationCriterion(new EvaluationTerminationCriterion(25000));

    // STEP 4. Specify and configure the crossover operator, used in the
    //         solution combination method of the scatter search
    crossover = (SBXCrossover) CrossoverFactory.getCrossoverOperator("SBXCrossover");
    crossover.setProbability(1.0);
    crossover.setDistributionIndex(20.0) ;

    // STEP 5. Specify and configure the improvement method. We use by default
    //         a polynomial mutation in this method.
    mutation = (PolynomialMutation) MutationFactory.getMutationOperator("PolynomialMutation");
    mutation.setProbability(1.0/problem.getNumberOfVariables());

    improvement = new MutationLocalSearch<Real>(problem,mutation);
    improvement.setTerminationCriterion(new EvaluationTerminationCriterion(1));
    improvement.setAbsoluteTerminationCriterion(false);

    // STEP 6. Add the operators to the algorithm
    algorithm.setCrossover(crossover);
    algorithm.setImprovement(improvement);

    long initTime      ;
    long estimatedTime ;
    initTime = System.currentTimeMillis();

    // STEP 7. Run the algorithm
    SolutionSet<Real> population = algorithm.execute();

    estimatedTime = System.currentTimeMillis() - initTime;
    logger_.info("Total execution time: "+ estimatedTime);

    // STEP 8. Print the results
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");
  }//main
}
