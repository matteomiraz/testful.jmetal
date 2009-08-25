/**
 * aMOCell4b.java
 * @author Juan J. Durillo
 * @version 1.0
 *
 */
package jmetal.metaheuristics.mocell;

import java.util.Comparator;

import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.archive.CrowdingArchive;
import jmetal.base.operator.comparator.CrowdingComparator;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.Selection;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Neighborhood;
import jmetal.util.Ranking;

/** 
 * Class representing de MoCell algorithm
 */
public class aMOCell4<V extends Variable>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {

  private static final long serialVersionUID = -912983109364330830L;
	//->fields
  private Problem<V> problem_;          //The problem to solve        

  public aMOCell4(Problem<V> problem){
    problem_ = problem;
  }

  /** Execute the algorithm 
   * @throws JMException */
  @SuppressWarnings("unchecked")
	public SolutionSet<V> execute() throws JMException {
    //Init the param
    int populationSize, archiveSize, maxEvaluations, evaluations;
    SolutionSet<V> currentPopulation;
    CrowdingArchive<V> archive;
    SolutionSet<V> [] neighbors;    
    Neighborhood<V> neighborhood;
    Comparator<Solution<V>> dominance = new DominanceComparator<V>();  
    Comparator<Solution<V>> crowdingComparator = new CrowdingComparator<V>();

    //Init the param
    //Read the params
    populationSize    = ((Integer)getInputParameter("populationSize")).intValue();
    archiveSize       = ((Integer)getInputParameter("archiveSize")).intValue();
    maxEvaluations    = ((Integer)getInputParameter("maxEvaluations")).intValue();                                

    //Init the variables    
    currentPopulation  = new SolutionSet<V>(populationSize);        
    archive            = new CrowdingArchive<V>(archiveSize,problem_.getNumberOfObjectives());                
    evaluations        = 0;                        
    neighborhood       = new Neighborhood<V>(populationSize);
    neighbors          = new SolutionSet[populationSize];

    //Create the initial population
    for (int i = 0; i < populationSize; i++){
      Solution<V> individual = new Solution<V>(problem_);
      problem_.evaluate(individual);           
      problem_.evaluateConstraints(individual);
      currentPopulation.add(individual);
      individual.setLocation(i);
      evaluations++;
    }


    while (evaluations < maxEvaluations){                                 
      for (int ind = 0; ind < currentPopulation.size(); ind++){
        Solution<V> individual = new Solution<V>(currentPopulation.get(ind));

        Solution<V> [] offSpring;

        //neighbors[ind] = neighborhood.getFourNeighbors(currentPopulation,ind);
        neighbors[ind] = neighborhood.getEightNeighbors(currentPopulation,ind);                                                           
        neighbors[ind].add(individual);

        // parents
        Solution<V> parent1 = selectionOperator.execute(neighbors[ind]);
        Solution<V> parent2;
        if (archive.size() > 0) {
          parent2 = selectionOperator.execute(archive);
        } else {                   
          parent2 = selectionOperator.execute(neighbors[ind]);
        }

        // Create a new individual, using genetic operators mutation and crossover
        offSpring = crossoverOperator.execute(parent1, parent2);               
        mutationOperator.execute(offSpring[0]);

        // Evaluate individual an his constraints
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);
        evaluations++;

        int flag = dominance.compare(individual,offSpring[0]);

        if (flag == 1) { //The new individual dominates
          offSpring[0].setLocation(individual.getLocation());                                      
          currentPopulation.replace(offSpring[0].getLocation(),offSpring[0]);
          archive.add(new Solution<V>(offSpring[0]));                   
        } else if (flag == 0) { //The new individual is non-dominated               
          neighbors[ind].add(offSpring[0]);               
          offSpring[0].setLocation(-1);
          Ranking<V> rank = new Ranking<V>(neighbors[ind]);
          for (int j = 0; j < rank.getNumberOfSubfronts(); j++) {
            Distance.crowdingDistanceAssignment(rank.getSubfront(j),
                                                problem_.getNumberOfObjectives());
          }
          neighbors[ind].sort(crowdingComparator); 
          Solution<V> worst = neighbors[ind].get(neighbors[ind].size()-1);

          if (worst.getLocation() == -1) { //The worst is the offspring
            archive.add(new Solution<V>(offSpring[0]));
          } else {
            offSpring[0].setLocation(worst.getLocation());
            currentPopulation.replace(offSpring[0].getLocation(),offSpring[0]);
            archive.add(new Solution<V>(offSpring[0]));
          }                                          
        }
      }                                 
    }
    //System.out.println(evaluations);
    return archive;
  }        
}

