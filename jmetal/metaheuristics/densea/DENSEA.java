/**
 * DENSEA.java
 *
 * @author Juanjo Durillo
 */

package jmetal.metaheuristics.densea;

import java.util.Comparator;

import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.operator.comparator.CrowdingComparator;
import jmetal.base.operator.comparator.EqualSolutions;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.Selection;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;

public class DENSEA<V extends Variable>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {

  private static final long serialVersionUID = -2886797988032373886L;
	private Problem<V> problem_;

  /* Create a new instance of DENSEA algorithm */
  public DENSEA(Problem<V> problem) {
    problem_ = problem;
  }

  //Implements the Densea delete duplicate elements
  public void deleteDuplicates(SolutionSet<V> population) {
    Comparator<Solution<V>> equalIndividuals = new EqualSolutions<V>();
    for (int i = 0; i < population.size()/2; i++) {
      for (int j = i+1; j < population.size()/2; j++) {
        int flag = equalIndividuals.compare(population.get(i),population.get(j));
        if (flag == 0) {
          Solution<V> aux = population.get(j);
          population.replace(j,population.get((population.size()/2)+j));
          population.replace((population.size()/2)+j,aux);          
        }
      }
    }
  }

  /* Execute the algorithm */
  public SolutionSet<V> execute() throws JMException {
    int populationSize, maxEvaluations, evaluations               ;
    SolutionSet<V> population                                        ;

    //Read the params
    populationSize    = getPopulationSize();
    maxEvaluations    = getMaxEvaluations();                

    //Init the variables
    population        = new SolutionSet<V>(populationSize);        
    evaluations       = 0;                

    //-> Create the initial population
    Solution<V> newIndividual;
    for (int i = 0; i < populationSize; i++) {
      newIndividual = new Solution<V>(problem_);                    
      problem_.evaluate(newIndividual);            
      problem_.evaluateConstraints(newIndividual);
      evaluations++;
      population.add(newIndividual);
    } //for       
    //<-

    Ranking<V> r;

    while (evaluations < maxEvaluations) {
      SolutionSet<V> P3 = new SolutionSet<V>(populationSize);
      for (int i = 0; i < populationSize/2; i++) {
        Solution<V> [] offSpring;
        Solution<V> parent1 = selectionOperator.execute(population);
        Solution<V> parent2 = selectionOperator.execute(population);
        offSpring = crossoverOperator.execute(parent1, parent2);
        mutationOperator.execute(offSpring[0]);
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);
        evaluations++;
        mutationOperator.execute(offSpring[1]);
        problem_.evaluate(offSpring[1]);
        problem_.evaluateConstraints(offSpring[1]);
        evaluations++;
        P3.add(offSpring[0]);
        P3.add(offSpring[1]);
      }

      r = new Ranking<V>(P3);
      for (int i = 0; i < r.getNumberOfSubfronts();i++) {
        Distance.crowdingDistanceAssignment(r.getSubfront(i),problem_.getNumberOfObjectives());
      }
      P3.sort(new CrowdingComparator<V>());

      System.out.println("Una population!!!!!!!");
      for (int i = 0; i < P3.size(); i++) {
        System.out.println(P3.get(i).getRank());
        //System.out.println(P3.get(i).getCrowdingDistance());
      }

      population.sort(new CrowdingComparator<V>());
      //deleteDuplicates(population);
      //deleteDuplicates(P3);
      SolutionSet<V> auxiliar = new SolutionSet<V>(populationSize);
      for (int i = 0; i < (populationSize/2);i++) {
        auxiliar.add(population.get(i));
      }

      for (int j = 0; j < (populationSize/2);j++) {
        auxiliar.add(population.get(j));
      }

      population = auxiliar;

      r = new Ranking<V>(population);
      for (int i = 0; i < r.getNumberOfSubfronts();i++) {
        Distance.crowdingDistanceAssignment(r.getSubfront(i),problem_.getNumberOfObjectives());
      }
    }
    r = new Ranking<V>(population);
    return r.getSubfront(0);
  }
}

