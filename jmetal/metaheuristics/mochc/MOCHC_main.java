/**
 * MOCHC_main.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.mochc;

import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.crossover.HUXCrossover;
import jmetal.base.operator.mutation.BitFlipMutationBinary;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.RandomSelection;
import jmetal.base.operator.selection.RankingAndCrowdingSelection;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.base.variable.Binary;
import jmetal.problems.RadioNetworkDesign;

public class MOCHC_main {

  @SuppressWarnings("unchecked")
	public static void main(String [] args) {
    try {                               
      Problem<Binary> problem = new RadioNetworkDesign(149);

      MOCHC<Binary> algorithm = new MOCHC<Binary>(problem);
      
      algorithm.setInputParameter("initialConvergenceCount",0.25);
      algorithm.setInputParameter("preservedPopulation",0.05);
      algorithm.setInputParameter("convergenceValue",3);
      algorithm.setInputParameter("populationSize",100);
      algorithm.setInputParameter("maxEvaluations",60000);
      
      HUXCrossover crossoverOperator      ;
      BitFlipMutationBinary mutationOperator       ;
      RandomSelection<Binary> parentsSelection       ;
      RankingAndCrowdingSelection<Binary> newGenerationSelection ;
      
      // Crossover operator
      crossoverOperator = (HUXCrossover) CrossoverFactory.getCrossoverOperator("HUXCrossover");
      //crossoverOperator = CrossoverFactory.getCrossoverOperator("SinglePointCrossover");
      crossoverOperator.setProbability(1.0);
     
      //parentsSelection = new RandomSelection();
      //newGenerationSelection = new RankingAndCrowdingSelection(problem);
      parentsSelection = (RandomSelection<Binary>) SelectionFactory.getSelectionOperator("RandomSelection") ;     
      newGenerationSelection = (RankingAndCrowdingSelection<Binary>) SelectionFactory.getSelectionOperator("RankingAndCrowdingSelection") ;   
      newGenerationSelection.setProblem(problem) ;          
     
      // Mutation operator
      mutationOperator = (BitFlipMutationBinary) MutationFactory.getMutationOperator("BitFlipMutationBinary");                    
      mutationOperator.setProbability(0.35);
      
      algorithm.setCrossover(crossoverOperator);
      algorithm.setMutation(mutationOperator);
      algorithm.setSelection(parentsSelection);
      algorithm.setNewGenerationSelection(newGenerationSelection);
      
      // Execute the Algorithm 
      long initTime = System.currentTimeMillis();
      SolutionSet<Binary> population = algorithm.execute();
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
