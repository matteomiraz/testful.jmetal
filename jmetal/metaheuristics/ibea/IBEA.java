/**
 * IBEA.java
 *
 *
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.metaheuristics.ibea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.Selection;
import jmetal.util.JMException;
import jmetal.util.Ranking;

/**
 * This class representing the SPEA2 algorithm
 */
public class IBEA<V extends Variable> 
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {

  private static final long serialVersionUID = 3196948190912308055L;

	/**
   * Defines the number of tournaments for creating the mating pool
   */
  public static final int TOURNAMENTS_ROUNDS = 1;

  /**
   * Stores the problem to solve
   */
  private Problem<V> problem_;

  /**
   * Stores the value of the indicator between each pair of solutions into
   * the solution set
   */
  private List<List<Double>> indicatorValues_;

  /**
   *
   */
  private double maxIndicatorValue_;
  /**
  * Constructor.
  * Create a new IBEA instance
  * @param problem Problem to solve
  */
  public IBEA(Problem<V> problem) {
    this.problem_ = problem;
  } // Spea2

  /**
   * calculates the hypervolume of that portion of the objective space that
   * is dominated by individual a but not by individual b
   */
  double calcHypervolumeIndicator(Solution<V> p_ind_a,
                                  Solution<V> p_ind_b,
                                  int d,
                                  double maximumValues [],
                                  double minimumValues []) {
    double a, b, r, max;
    double volume = 0;
    double rho = 2.0;

    r = rho * (maximumValues[d-1] - minimumValues[d-1]);
    max = minimumValues[d-1] + r;


    a = p_ind_a.getObjective(d-1);
    if (p_ind_b == null)
      b = max;
    else
      b = p_ind_b.getObjective(d-1);

    if (d == 1)
    {
      if (a < b)
        volume = (b - a) / r;
      else
        volume = 0;
    }
    else
    {
      if (a < b)
      {
         volume = calcHypervolumeIndicator(p_ind_a, null, d - 1, maximumValues, minimumValues) *
         (b - a) / r;
         volume += calcHypervolumeIndicator(p_ind_a, p_ind_b, d - 1, maximumValues, minimumValues) *
         (max - b) / r;
      }
      else
      {
         volume = calcHypervolumeIndicator(p_ind_a, p_ind_b, d - 1, maximumValues, minimumValues) *
         (max - b) / r;
      }
    }

    return (volume);
  }



    /**
   * This structure store the indicator values of each pair of elements
   */
  public void computeIndicatorValuesHD(SolutionSet<V> solutionSet,
                                     double [] maximumValues,
                                     double [] minimumValues) {
    SolutionSet<V> A, B;
    // Initialize the structures
    indicatorValues_ = new ArrayList<List<Double>>();
    maxIndicatorValue_ = - Double.MAX_VALUE;

    for (int j = 0; j < solutionSet.size(); j++) {
      A = new SolutionSet<V>(1);
      A.add(solutionSet.get(j));

      List<Double> aux = new ArrayList<Double>();
      for (int i = 0; i < solutionSet.size(); i++) {
        B = new SolutionSet<V>(1);
        B.add(solutionSet.get(i));

        int flag = (new DominanceComparator<V>()).compare(A.get(0), B.get(0));

        double value = 0.0;
        if (flag == -1) {
            value = - calcHypervolumeIndicator(A.get(0), B.get(0), problem_.getNumberOfObjectives(), maximumValues, minimumValues);
        } else {
            value = calcHypervolumeIndicator(B.get(0), A.get(0), problem_.getNumberOfObjectives(), maximumValues, minimumValues);
        }
        //double value = epsilon.epsilon(matrixA,matrixB,problem_.getNumberOfObjectives());

        //System.out.println(value);
        //Update the max value of the indicator
        if (Math.abs(value) > maxIndicatorValue_)
          maxIndicatorValue_ = Math.abs(value);
        aux.add(value);
     }
     indicatorValues_.add(aux);
   }
  } // computeIndicatorValues



  /**
   * Calculate the fitness for the individual at position pos
   */
  public void fitness(SolutionSet<V> solutionSet,int pos) {
      double fitness = 0.0;
      double kappa   = 0.05;
    
      for (int i = 0; i < solutionSet.size(); i++) {
        if (i!=pos) {
           fitness += Math.exp((-1 * indicatorValues_.get(i).get(pos)/maxIndicatorValue_) / kappa);
        }
      }
      solutionSet.get(pos).setFitness(fitness);
  }


  /**
   * Calculate the fitness for the entire population.
  **/
  public void calculateFitness(SolutionSet<V> solutionSet) {
    // Obtains the lower and upper bounds of the population
    double [] maximumValues = new double[problem_.getNumberOfObjectives()];
    double [] minimumValues = new double[problem_.getNumberOfObjectives()];

    for (int i = 0; i < problem_.getNumberOfObjectives();i++) {
        maximumValues[i] = - Double.MAX_VALUE; // i.e., the minus maxium value
        minimumValues[i] =   Double.MAX_VALUE; // i.e., the maximum value
    }

    for (int pos = 0; pos < solutionSet.size(); pos++) {
        for (int obj = 0; obj < problem_.getNumberOfObjectives(); obj++) {
          double value = solutionSet.get(pos).getObjective(obj);
          if (value > maximumValues[obj])
              maximumValues[obj] = value;
          if (value < minimumValues[obj])
              minimumValues[obj] = value;
        }
    }

    computeIndicatorValuesHD(solutionSet,maximumValues,minimumValues);
    for (int pos =0; pos < solutionSet.size(); pos++) {
        fitness(solutionSet,pos);
    }
  }



  /** 
   * Update the fitness before removing an individual
   */
  public void removeWorst(SolutionSet<V> solutionSet) {
   
    // Find the worst;
    double worst      = solutionSet.get(0).getFitness();
    int    worstIndex = 0;
    double kappa = 0.05;
     
    for (int i = 1; i < solutionSet.size(); i++) {
      if (solutionSet.get(i).getFitness() > worst) {
        worst = solutionSet.get(i).getFitness();
        worstIndex = i;
      }
    }

    //if (worstIndex == -1) {
    //    System.out.println("Yes " + worst);
    //}
    //System.out.println("Solution Size "+solutionSet.size());
    //System.out.println(worstIndex);

    // Update the population
    for (int i = 0; i < solutionSet.size(); i++) {
      if (i!=worstIndex) {
          double fitness = solutionSet.get(i).getFitness();
          fitness -= Math.exp((- indicatorValues_.get(worstIndex).get(i)/maxIndicatorValue_) / kappa);
          solutionSet.get(i).setFitness(fitness);
      }
    }

    // remove worst from the indicatorValues list
    indicatorValues_.remove(worstIndex); // Remove its own list
    Iterator<List<Double>> it = indicatorValues_.iterator();
    while (it.hasNext())
        it.next().remove(worstIndex);

    // remove the worst individual from the population
    solutionSet.remove(worstIndex);
  } // removeWorst


  /**
  * Runs of the IBEA algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution
  * @throws JMException
  */
  public SolutionSet<V> execute() throws JMException{
    int populationSize, archiveSize, maxEvaluations, evaluations;
    SolutionSet<V> solutionSet, archive, offSpringSolutionSet;

    //Read the params
    populationSize = ((Integer)getInputParameter("populationSize")).intValue();
    archiveSize    = ((Integer)getInputParameter("archiveSize")).intValue();
    maxEvaluations = ((Integer)getInputParameter("maxEvaluations")).intValue();

    //Initialize the variables
    solutionSet  = new SolutionSet<V>(populationSize);
    archive     = new SolutionSet<V>(archiveSize);
    evaluations = 0;

    //-> Create the initial solutionSet
    Solution<V> newSolution;
    for (int i = 0; i < populationSize; i++) {
      newSolution = new Solution<V>(problem_);
      problem_.evaluate(newSolution);
      problem_.evaluateConstraints(newSolution);
      evaluations++;
      solutionSet.add(newSolution);
    }

    while (evaluations < maxEvaluations){
      SolutionSet<V> union = solutionSet.union(archive);
      calculateFitness(union);
      archive = union;
      
      while (archive.size() > populationSize) {
        removeWorst(archive);
      }
      // Create a new offspringPopulation
      offSpringSolutionSet= new SolutionSet<V>(populationSize);
      while (offSpringSolutionSet.size() < populationSize){
      	Solution<V> parent1, parent2;
      	int j = 0;
        do{
          j++;
          parent1 = selectionOperator.execute(archive);
        } while (j < IBEA.TOURNAMENTS_ROUNDS); // do-while
        int k = 0;
        do{
          k++;
          parent2 = selectionOperator.execute(archive);
        } while (k < IBEA.TOURNAMENTS_ROUNDS); // do-while

        //make the crossover
        Solution<V> [] offSpring = crossoverOperator.execute(parent1, parent2);
        mutationOperator.execute(offSpring[0]);
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);
        offSpringSolutionSet.add(offSpring[0]);
        evaluations++;
      } // while
      // End Create a offSpring solutionSet
      solutionSet = offSpringSolutionSet;
    } // while

    Ranking<V> ranking = new Ranking<V>(archive);
    return ranking.getSubfront(0);
  } // execute
} // Spea2
