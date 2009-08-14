/**
 * MutationImprovement.java
 * @author Juan J. Durillo
 * @version 1.0
 * 
 */

package jmetal.base.operator.localSearch;

import java.util.Comparator;

import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.archive.CrowdingArchive;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.base.operator.comparator.OverallConstraintViolationComparator;
import jmetal.base.operator.mutation.Mutation;
import jmetal.util.JMException;

/**
 * This class implements an local search operator based in the use of a 
 * mutation operator. An archive is used to store the non-dominated solutions
 * found during the search.
 */
public class MutationLocalSearch<T extends Variable> extends LocalSearch<T> {
    
  private static final long serialVersionUID = 6132501458863378035L;

	/**
   * Stores the problem to solve
   */
  private Problem<T> problem_;
    
  /**
  * Stores a reference to the archive in which the non-dominated solutions are
  * inserted
  */
  private SolutionSet<T> archive_;


  /**
   * Stores comparators for dealing with constraints and dominance checking, 
   * respectively.
   */
  private Comparator<Solution<T>> constraintComparator_ ;
  private Comparator<Solution<T>> dominanceComparator_ ;
  
  /**
   * Stores the mutation operator 
   */
  private Mutation<T> mutationOperator_;
  
  /**
   * Stores the number of evaluations_ carried out
   */
  int evaluations_ ;
  
  
  /**
  * Constructor. 
  * Creates a new local search object.
  * @param problem The problem to solve
  * @param mutationOperator The mutation operator 
  * @param archive The archive
  */
  public MutationLocalSearch(Problem<T>     problem,
                             Mutation<T>    mutationOperator,
                             SolutionSet<T> archive) {
    evaluations_          = 0      ;
    problem_              = problem;
    archive_              = archive;
    dominanceComparator_  = new DominanceComparator<T>();
    constraintComparator_ = new OverallConstraintViolationComparator<T>();
  } //Mutation improvement


  /**
  * Constructor. 
  * Creates a new local search object.
  * @param problem The problem to solve
  * @param mutationOperator The mutation operator 
  */
  public MutationLocalSearch(Problem<T> problem, Mutation<T> mutationOperator) {
    evaluations_ = 0 ;
    problem_ = problem;
    mutationOperator_ = mutationOperator;
    dominanceComparator_ = new DominanceComparator<T>();
    constraintComparator_ = new OverallConstraintViolationComparator<T>();
  } // MutationLocalSearch
  
 /**
   * Executes the local search. The maximum number of iterations is given by 
   * the param "improvementRounds", which is in the parameter list of the 
   * operator. The archive to store the non-dominated solutions is also in the 
   * parameter list.
   * @param object Object representing a solution
   * @return An object containing the new improved solution
 * @throws JMException 
   */
  @Override
  public Solution<T> execute(Solution<T> solution) throws JMException {
    int i = 0;
    int best = 0;
    evaluations_ = 0;        

    if (improvementRounds <= 0)
      return new Solution<T>(solution);
        
    do 
    {
      i++;
      Solution<T> mutatedSolution = new Solution<T>(solution);
      mutationOperator_.execute(mutatedSolution);
            
            
      // Evaluate the getNumberOfConstraints
      if (problem_.getNumberOfConstraints() > 0)
      {
        problem_.evaluateConstraints(mutatedSolution);
        best = constraintComparator_.compare(mutatedSolution,solution);
        if (best == 0) //none of then is better that the other one
        {
          problem_.evaluate(mutatedSolution);
          evaluations_++;
          best = dominanceComparator_.compare(mutatedSolution,solution);
        } 
        else if (best == -1) //mutatedSolution is best
        {
          problem_.evaluate(mutatedSolution);
          evaluations_++;
        }
      }
      else
      {
        problem_.evaluate(mutatedSolution);
        evaluations_++;
        best = dominanceComparator_.compare(mutatedSolution,solution);
      }
      if (best == -1) // This is: Mutated is best
        solution = mutatedSolution;
      else if (best == 1) // This is: Original is best
        //delete mutatedSolution
        ;
      else // This is mutatedSolution and original are non-dominated
      {
        //this.archive_.addIndividual(new Solution(solution));                
        //solution = mutatedSolution;
        if (archive_ != null)
          archive_.add(mutatedSolution);
      }                            
    }
    while (i < improvementRounds);
    return new Solution<T>(solution);
  } // execute
  
   
  /** 
   * Returns the number of evaluations maded
   */
  public int getEvaluations() {
    return evaluations_;
  } // evaluations


	public void setArchive(CrowdingArchive<T> archive) {
		this.archive_ = archive;
	}
} // MutationLocalSearch
