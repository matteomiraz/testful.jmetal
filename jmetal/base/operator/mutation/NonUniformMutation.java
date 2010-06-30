/**
 * NonUniformMutation.java
 * @author Juan J. Durillo
 * @version 1.0
 *
 */
package jmetal.base.operator.mutation;

import jmetal.base.Solution;
import jmetal.base.variable.Real;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
  * This class implements a non-uniform mutation operator.
  * NOTE: the type of the solutions must be <code>SolutionType_.Real</code>
  */
public class NonUniformMutation extends Mutation<Real> {

  private static final long serialVersionUID = 7432414654294815093L;

	/**
   * perturbation_ stores the perturbation value used in the Non Uniform
   * mutation operator
   */
  private Double perturbation_ = null;

  /**
   * maxIterations_ stores the maximun number of iterations.
   */
  private Long maxIterations_ = null;

  /**
   * actualIteration_ stores the iteration in which the operator is going to be
   * applied
   */
  private Long actualIteration_ = null;

  /**
  * Constructor
  * Creates a new instance of the non uniform mutation
  */
  public NonUniformMutation()
  {
  } // NonUniformMutation


  /**
  * Perform the mutation operation
  * @param probability Mutation probability
  * @param solution The solution to mutate
   * @throws JMException
  */
  public void doMutation(double probability, Solution<Real> solution) throws JMException {

    for (int var = 0; var < solution.getDecisionVariables().size(); var++) {
      if (PseudoRandom.randDouble() < probability) {
        double rand = PseudoRandom.randDouble();
        double tmp;

        if (rand <= 0.5)
        {
          tmp = delta(
                  solution.getDecisionVariables().variables_.get(var).getUpperBound() -
                  solution.getDecisionVariables().variables_.get(var).getValue(),
                  perturbation_.doubleValue());
          tmp += solution.getDecisionVariables().variables_.get(var).getValue();
        }
        else
        {
          tmp = delta(
                  solution.getDecisionVariables().variables_.get(var).getLowerBound() -
                  solution.getDecisionVariables().variables_.get(var).getValue(),
                  perturbation_.doubleValue());
          tmp += solution.getDecisionVariables().variables_.get(var).getValue();
        }

        if (tmp < solution.getDecisionVariables().variables_.get(var).getLowerBound())
          tmp = solution.getDecisionVariables().variables_.get(var).getLowerBound();
        else if (tmp > solution.getDecisionVariables().variables_.get(var).getUpperBound())
          tmp = solution.getDecisionVariables().variables_.get(var).getUpperBound();

        solution.getDecisionVariables().variables_.get(var).setValue(tmp);
      }
    }
  } // doMutation


  /**
   * Calculates the delta value used in NonUniform mutation operator
   */
  private double delta(double y, double bMutationParameter) {
    double rand = PseudoRandom.randDouble();
    int it,maxIt;
    it    = actualIteration_.intValue();
    maxIt = maxIterations_.intValue();

    return (y * (1.0 -
                Math.pow(rand,
                         Math.pow((1.0 - it /(double) maxIt),bMutationParameter)
                         )));
  } // delta


	public void setPerturbation(double perturbation) {
		perturbation_ = perturbation;
	}


	public void setMaxIterations(long maxIterations) {
		maxIterations_ = maxIterations;
	}

	public void setCurrentIteration(long actualIteration) {
		actualIteration_ = actualIteration;
	}

  /**
  * Executes the operation
  * @param object An object containing a solution
  * @return An object containing the mutated solution
   * @throws JMException
  */
  public void execute(Solution<Real> solution) throws JMException {
    doMutation(probability,solution);
  } // execute
} // NonUniformMutation
