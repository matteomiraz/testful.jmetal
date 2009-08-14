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
import jmetal.base.archive.CrowdingArchive;
import jmetal.base.operator.comparator.CrowdingComparator;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Neighborhood;
import jmetal.util.Ranking;

/** 
 * Class representing de MoCell algorithm
 */
public class aMOCell4 extends Algorithm{

  private static final long serialVersionUID = -912983109364330830L;
	//->fields
  private Problem problem_;          //The problem to solve        

  public aMOCell4(Problem problem){
    problem_ = problem;
  }

  /** Execute the algorithm 
   * @throws JMException */
  public SolutionSet execute() throws JMException {
    //Init the param
    int populationSize, archiveSize, maxEvaluations, evaluations;
    SolutionSet currentPopulation;
    CrowdingArchive archive;
    SolutionSet [] neighbors;    
    Neighborhood neighborhood;
    Comparator<Solution> dominance = new DominanceComparator();  
    Comparator<Solution> crowdingComparator = new CrowdingComparator();
    Distance distance = new Distance();

    //Init the param
    //Read the params
    populationSize    = ((Integer)getInputParameter("populationSize")).intValue();
    archiveSize       = ((Integer)getInputParameter("archiveSize")).intValue();
    maxEvaluations    = ((Integer)getInputParameter("maxEvaluations")).intValue();                                

    //Init the variables    
    currentPopulation  = new SolutionSet(populationSize);        
    archive            = new CrowdingArchive(archiveSize,problem_.getNumberOfObjectives());                
    evaluations        = 0;                        
    neighborhood       = new Neighborhood(populationSize);
    neighbors          = new SolutionSet[populationSize];

    //Create the initial population
    for (int i = 0; i < populationSize; i++){
      Solution individual = new Solution(problem_);
      problem_.evaluate(individual);           
      problem_.evaluateConstraints(individual);
      currentPopulation.add(individual);
      individual.setLocation(i);
      evaluations++;
    }


    while (evaluations < maxEvaluations){                                 
      for (int ind = 0; ind < currentPopulation.size(); ind++){
        Solution individual = new Solution(currentPopulation.get(ind));

        Solution [] offSpring;

        //neighbors[ind] = neighborhood.getFourNeighbors(currentPopulation,ind);
        neighbors[ind] = neighborhood.getEightNeighbors(currentPopulation,ind);                                                           
        neighbors[ind].add(individual);

        // parents
        Solution parent1 = (Solution)selectionOperator.execute(neighbors[ind]);
        Solution parent2;
        if (archive.size() > 0) {
          parent2 = (Solution)selectionOperator.execute(archive);
        } else {                   
          parent2 = (Solution)selectionOperator.execute(neighbors[ind]);
        }

        // Create a new individual, using genetic operators mutation and crossover
        offSpring = (Solution [])crossoverOperator.execute(parent1, parent2);               
        mutationOperator.execute(offSpring[0]);

        // Evaluate individual an his constraints
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);
        evaluations++;

        int flag = dominance.compare(individual,offSpring[0]);

        if (flag == 1) { //The new individual dominates
          offSpring[0].setLocation(individual.getLocation());                                      
          currentPopulation.replace(offSpring[0].getLocation(),offSpring[0]);
          archive.add(new Solution(offSpring[0]));                   
        } else if (flag == 0) { //The new individual is non-dominated               
          neighbors[ind].add(offSpring[0]);               
          offSpring[0].setLocation(-1);
          Ranking rank = new Ranking(neighbors[ind]);
          for (int j = 0; j < rank.getNumberOfSubfronts(); j++) {
            distance.crowdingDistanceAssignment(rank.getSubfront(j),
                                                problem_.getNumberOfObjectives());
          }
          neighbors[ind].sort(crowdingComparator); 
          Solution worst = neighbors[ind].get(neighbors[ind].size()-1);

          if (worst.getLocation() == -1) { //The worst is the offspring
            archive.add(new Solution(offSpring[0]));
          } else {
            offSpring[0].setLocation(worst.getLocation());
            currentPopulation.replace(offSpring[0].getLocation(),offSpring[0]);
            archive.add(new Solution(offSpring[0]));
          }                                          
        }
      }                                 
    }
    //System.out.println(evaluations);
    return archive;
  }        
}

