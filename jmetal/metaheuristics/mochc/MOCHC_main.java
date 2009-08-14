/**
 * MOCHC_main.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.mochc;

import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.problems.RadioNetworkDesign;

public class MOCHC_main {

  public static void main(String [] args) {
    try {                               
      Problem problem = new RadioNetworkDesign(149);

      Algorithm algorithm = null;
      algorithm = new MOCHC(problem);
      
      algorithm.setInputParameter("initialConvergenceCount",0.25);
      algorithm.setInputParameter("preservedPopulation",0.05);
      algorithm.setInputParameter("convergenceValue",3);
      algorithm.setInputParameter("populationSize",100);
      algorithm.setInputParameter("maxEvaluations",60000);
      
      Operator crossoverOperator      ;
      Operator mutationOperator       ;
      Operator parentsSelection       ;
      Operator newGenerationSelection ;
      
      // Crossover operator
      crossoverOperator = CrossoverFactory.getCrossoverOperator("HUXCrossover");
      //crossoverOperator = CrossoverFactory.getCrossoverOperator("SinglePointCrossover");
      crossoverOperator.setParameter("probability",1.0);
     
      //parentsSelection = new RandomSelection();
      //newGenerationSelection = new RankingAndCrowdingSelection(problem);
      parentsSelection = SelectionFactory.getSelectionOperator("RandomSelection") ;     
      newGenerationSelection = SelectionFactory.getSelectionOperator("RankingAndCrowdingSelection") ;   
      newGenerationSelection.setParameter("problem", problem) ;          
     
      // Mutation operator
      mutationOperator = MutationFactory.getMutationOperator("BitFlipMutation");                    
      mutationOperator.setParameter("probability",0.35);
      
      algorithm.addOperator("crossover",crossoverOperator);
      algorithm.addOperator("cataclysmicMutation",mutationOperator);
      algorithm.addOperator("parentSelection",parentsSelection);
      algorithm.addOperator("newGenerationSelection",newGenerationSelection);
      
      // Execute the Algorithm 
      long initTime = System.currentTimeMillis();
      SolutionSet population = algorithm.execute();
      long estimatedTime = System.currentTimeMillis() - initTime;
      System.out.println("Total execution time: "+estimatedTime);

      // Print results
      population.printVariablesToFile("VAR");
      population.printObjectivesToFile("FUN");
    } //try           
    catch (Exception e) {
      System.err.println(e);
      e.printStackTrace();
    } //catch    
  }//main
}
