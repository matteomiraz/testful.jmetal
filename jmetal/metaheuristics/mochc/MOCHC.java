/**
 * MOCHC.java
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.metaheuristics.mochc;

import java.util.Comparator;

import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.archive.CrowdingArchive;
import jmetal.base.operator.comparator.CrowdingComparator;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.RankingAndCrowdingSelection;
import jmetal.base.operator.selection.Selection;
import jmetal.base.variable.Binary;
import jmetal.util.JMException;

/**
 *
 * Class implementing the CHC algorithm.
 */
public class MOCHC <V extends Variable>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>[]>, LocalSearch<V>> {

  private static final long serialVersionUID = 1543452947367589923L;
	/**
  * Stores the Problem<V> to solve
  */
  private Problem<V> Problem_;
  private RankingAndCrowdingSelection<V> newGenerationSelection;
  
  /**
  * Constructor
  * Creates a new instance of MOCHC 
  */
  public MOCHC(Problem<V> Problem) {
    Problem_ = Problem;
  }
  
	public void setNewGenerationSelection(RankingAndCrowdingSelection<V> newGenerationSelection) {
		this.newGenerationSelection = newGenerationSelection;
	}
  
  /** 
  * Compares two SolutionSet<V>s to determine if both are equals
  * @param SolutionSet<V> A <code>SolutionSet<V></code>
  * @param newSolutionSet<V> A <code>SolutionSet<V></code>
  * @return true if both are cotains the same Solution<V>s, false in other case
  */
  public boolean equals(SolutionSet<V> SolutionSet, SolutionSet<V> newSolutionSet) {
    boolean found;
    for (int i = 0; i < SolutionSet.size(); i++){

      int j = 0;
      found = false;
      while (j < newSolutionSet.size()) {

        if (SolutionSet.get(i).equals(newSolutionSet.get(j))) {
          found = true;
        }
        j++;
      }
      if (!found) {
        return false;
      }
    }
    return true;
  } // equals

  /**
   * Calculate the hamming distance between two Solution<V>s
   * @param Solution<V>One A <code>Solution<V></code>
   * @param Solution<V>Two A <code>Solution<V></code>
   * @return the hamming distance between Solution<V>s
   */
  public int hammingDistance(Solution<V> SolutionOne, Solution<V> SolutionTwo) {
    int distance = 0;
    for (int i = 0; i < Problem_.getNumberOfVariables(); i++) {
      distance += 
        ((Binary)SolutionOne.getDecisionVariables().variables_.get(i)).
        hammingDistance((Binary)SolutionTwo.getDecisionVariables().variables_.get(i));
    }
    
    return distance;
  } // hammingDistance 

  /**   
  * Runs of the MOCHC algorithm.
  * @return a <code>SolutionSet<V></code> that is a set of non dominated Solution<V>s
  * as a result of the algorithm execution  
  */  
  public SolutionSet<V> execute() throws JMException {
    int iterations       ;
    int populationSize   ;
    int convergenceValue ;
    int maxEvaluations   ;
    int minimumDistance  ;
    int evaluations      ;
    
    Comparator<Solution<V>> crowdingComparator = new CrowdingComparator<V>();
    
    double preservedPopulation     ;
    double initialConvergenceCount ;
    boolean condition = false;
    SolutionSet<V> SolutionSet, offspringPopulation,newPopulation;

    // Read parameters
    initialConvergenceCount = 
           ((Double)getInputParameter("initialConvergenceCount")).doubleValue();
    preservedPopulation     = 
           ((Double)getInputParameter("preservedPopulation")).doubleValue();
    convergenceValue        = 
           ((Integer)getInputParameter("convergenceValue")).intValue();
    populationSize          = 
           ((Integer)getInputParameter("populationSize")).intValue();
    maxEvaluations          = 
           ((Integer)getInputParameter("maxEvaluations")).intValue();


    iterations  = 0 ;
    evaluations = 0 ;
    
    //Calculate the maximum Problem<V> sizes
    Solution<V> aux = new Solution<V>(Problem_);
    int size = 0;
    for (int var = 0; var < Problem_.getNumberOfVariables(); var++) {
      size += ((Binary)aux.getDecisionVariables().variables_.get(var)).getNumberOfBits();
    }
    minimumDistance = (int) Math.floor(initialConvergenceCount * size);

    SolutionSet = new SolutionSet<V>(populationSize);
    for (int i = 0; i < populationSize; i++) {
      Solution<V> Solution = new Solution<V>(Problem_);
      Problem_.evaluate(Solution);
      Problem_.evaluateConstraints(Solution);
      evaluations++;        
      SolutionSet.add(Solution);
    }      

    while (!condition) {
      offspringPopulation = new SolutionSet<V>(populationSize);
      for (int i = 0; i < SolutionSet.size()/2; i++) {
        Solution<V> [] parents   = selectionOperator.execute(SolutionSet);         

        //Equality condition between Solution<V>s
        if (hammingDistance(parents[0],parents[1]) >= (minimumDistance)) {
          Solution<V> [] offspring = (Solution<V> [])crossoverOperator.execute(parents[0], parents[1]);
          Problem_.evaluate(offspring[0]);
          Problem_.evaluateConstraints(offspring[0]);
          Problem_.evaluate(offspring[1]);
          Problem_.evaluateConstraints(offspring[1]);
          evaluations += 2;
          offspringPopulation.add(offspring[0]);
          offspringPopulation.add(offspring[1]);
        }
      }
      SolutionSet<V> union = SolutionSet.union(offspringPopulation);
      newGenerationSelection.setPopulationSize(populationSize);
      newPopulation = (SolutionSet<V>)newGenerationSelection.execute(union);

      if (equals(SolutionSet,newPopulation)) {
        minimumDistance--;
      }
      if (minimumDistance <= -convergenceValue) {

        minimumDistance = (int) (1.0/size * (1 - 1.0/size) * size);
        //minimumDistance = (int) (0.35 * (1 - 0.35) * size);

        int preserve = (int)Math.floor(preservedPopulation*populationSize);
        newPopulation = new SolutionSet<V>(populationSize);
        SolutionSet.sort(crowdingComparator);
        for (int i = 0; i < preserve; i++) {
          newPopulation.add(new Solution<V>(SolutionSet.get(i)));
        }
        for (int i = preserve;i < populationSize; i++) {
          Solution<V> Solution = new Solution<V>(SolutionSet.get(i));
          mutationOperator.execute(Solution);
          Problem_.evaluate(Solution);
          Problem_.evaluateConstraints(Solution);
          newPopulation.add(Solution);
        }                        
      }
      iterations++;

      SolutionSet = newPopulation;
      if (evaluations >= maxEvaluations) {
        condition = true;
      }
    }
    
    
    CrowdingArchive<V> archive;
    archive = new CrowdingArchive<V>(populationSize,Problem_.getNumberOfObjectives()) ;
    for (int i = 0; i < SolutionSet.size(); i++) {
      archive.add(SolutionSet.get(i)) ;
    }

    return archive;
  } // execute
}  // MOCHC
