/**
 * GDE3_Settings.java
 *
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * CellDE_Settings class of algorithm GDE3
 */
package jmetal.experiments.settings;

import java.util.Properties;

import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.crossover.DifferentialEvolutionCrossover;
import jmetal.base.operator.selection.Selection;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.experiments.Settings;
import jmetal.metaheuristics.gde3.GDE3;
import jmetal.util.JMException;

/**
 *
 * @author Antonio
 */
@SuppressWarnings("unchecked")
public class GDE3_Settings extends Settings {
  // Default settings
  double CR_ = 0.1;
  double F_ = 0.5;
  int populationSize_ = 100;
  int maxIterations_ = 250;
  String paretoFrontFile_ = "";

  /**
   * Constructor
   */
  public GDE3_Settings(Problem problem) {
    super(problem);
  } // CellDE_Settings

  /**
   * Configure the algorith with the especified parameter settings
   * @return an algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    GDE3 algorithm;
    Selection selection;
    DifferentialEvolutionCrossover crossover;
    //Operator mutation;

    // Creating the problem
    algorithm = new GDE3(problem_);

    // Algorithm parameters
    algorithm.setPopulationSize(populationSize_);
    algorithm.setMaxEvaluations(maxIterations_);

    // Crossover operator 
    crossover = (DifferentialEvolutionCrossover) CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover");
    crossover.setCR(CR_);
    crossover.setF(F_);

    // Add the operators to the algorithm
    selection = SelectionFactory.getSelectionOperator("DifferentialEvolutionSelection");

    algorithm.setCrossover(crossover);
    algorithm.setSelection(selection);

//    // Creating the indicator object
//    if (!paretoFrontFile_.equals("")) {
//      indicators = new QualityIndicator(problem_, paretoFrontFile_);
//      algorithm.setIndicators(indicators);
//    } // if

    return algorithm;
  }

  /**
   * Configure an algorithm with user-defined parameter settings
   * @param settings
   * @return An algorithm
   * @throws jmetal.util.JMException
   */
  public Algorithm configure(Properties settings) throws JMException {
    if (settings != null) {
      CR_ = Double.parseDouble(settings.getProperty("CR", "" + CR_));
      F_ = Double.parseDouble(settings.getProperty("F", "" + F_));
      populationSize_ = Integer.parseInt(settings.getProperty("POPULATION_SIZE", "" + populationSize_));
      maxIterations_ = Integer.parseInt(settings.getProperty("MAX_ITERATIONS", "" + maxIterations_));

      paretoFrontFile_ = settings.getProperty("PARETO_FRONT_FILE", "");
    }

    return configure();
  }
} // GDE3_Settings
