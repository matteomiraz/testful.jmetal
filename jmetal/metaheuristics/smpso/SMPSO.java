/**
 * SMPSO.java
 * 
 * @author Juan J. Durillo
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.metaheuristics.smpso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import jmetal.base.Algorithm;
import jmetal.base.DecisionVariables;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.archive.CrowdingArchive;
import jmetal.base.operator.comparator.CrowdingDistanceComparator;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.mutation.NonUniformMutation;
import jmetal.base.operator.mutation.PolynomialMutation;
import jmetal.base.operator.mutation.UniformMutation;
import jmetal.base.operator.selection.Selection;
import jmetal.base.variable.Real;
import jmetal.qualityIndicator.Hypervolume;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

public class SMPSO<V extends Real>
	extends Algorithm<V, Crossover<V>, Mutation<V>, Selection<V, Solution<V>>, LocalSearch<V>> {

  private static final long serialVersionUID = -5445178816136270387L;
	/**
   * Stores the problem to solve
   */
  private Problem<V> problem_;
  /**
   * Stores the number of particles_ used
   */
  private int particlesSize_;
  /**
   * Stores the maximum size for the archive
   */
  private int archiveSize_;
  /**
   * Stores the maximum number of iteration_
   */
  private int maxIterations_;
  /**
   * Stores the current number of iteration_
   */
  private int iteration_;
  /**
   * Stores the perturbation used by the non-uniform mutation
   */
  private double perturbation_;
  /**
   * Stores the distribution index used by polynomial mutation
   */
  private double mutationDistributionIndex_;
  /**
   * Stores the particles
   */
  private SolutionSet<V> particles_;
  /**
   * Stores the best_ solutions founds so far for each particles
   */
  private Solution<V>[] best_;
  /**
   * Stores the leaders_
   */
  private CrowdingArchive<V> leaders_;
  /**
   * Stores the speed_ of each particle
   */
  private double[][] speed_;
  /**
   * Stores a comparator for checking dominance
   */
  private Comparator<Solution<V>> dominance_;
  /**
   * Stores a comparator for crowding checking
   */
  private Comparator<Solution<V>> crowdingDistanceComparator_;
  /**
   * Stores a operator for uniform mutations
   */
  private UniformMutation uniformMutation_;
  /**
   * Stores a operator for non uniform mutations
   */
  private NonUniformMutation nonUniformMutation_;
  /**
   * Stores a operator for polynomial mutations
   */
  private PolynomialMutation<V> polynomialMutation_;
  /**
   * eta_ value
   */
  QualityIndicator<V> indicators_; // QualityIndicator object
  int requiredEvaluations_; // Use in the example of use of the
  double r1Max_;
  double r1Min_;
  double r2Max_;
  double r2Min_;
  double C1Max_;
  double C1Min_;
  double C2Max_;
  double C2Min_;
  double WMax_;
  double WMin_;
  double ChVel1_;
  double ChVel2_;

  /** 
   * Constructor
   * @param problem Problem to solve
   */
  public SMPSO(Problem<V> problem) {
    problem_ = problem;

    r1Max_ = 1.0;
    r1Min_ = 0.0;
    r2Max_ = 1.0;
    r2Min_ = 0.0;
    C1Max_ = 2.5;
    C1Min_ = 1.5;
    C2Max_ = 2.5;
    C2Min_ = 1.5;
    WMax_ = 0.1;
    WMin_ = 0.1;
    ChVel1_ = -1;
    ChVel2_ = -1;
  } // Constructor

  public SMPSO(Problem<V> problem,
    Vector<Double> variables,
    String trueParetoFront) throws FileNotFoundException {
    problem_ = problem;

    r1Max_ = variables.get(0);
    r1Min_ = variables.get(1);
    r2Max_ = variables.get(2);
    r2Min_ = variables.get(3);
    C1Max_ = variables.get(4);
    C1Min_ = variables.get(5);
    C2Max_ = variables.get(6);
    C2Min_ = variables.get(7);
    WMax_ = variables.get(8);
    WMin_ = variables.get(9);
    ChVel1_ = variables.get(10);
    ChVel2_ = variables.get(11);

    hy_ = new Hypervolume();
    jmetal.qualityIndicator.util.MetricsUtil mu = new jmetal.qualityIndicator.util.MetricsUtil();
    trueFront_ = mu.readNonDominatedSolutionSet(trueParetoFront);
    hy_.hypervolume(trueFront_.writeObjectivesToMatrix(),
      trueFront_.writeObjectivesToMatrix(),
      problem_.getNumberOfObjectives());

  } // SMPSO
  private Hypervolume hy_;
  private SolutionSet<V> trueFront_;
  private double deltaMax_[];
  private double deltaMin_[];
  boolean success_;

  /** 
   * Constructor
   * @param problem Problem to solve
   */
  public SMPSO(Problem<V> problem, String trueParetoFront) throws FileNotFoundException {
    problem_ = problem;
    //System.out.println("Pareto front file: " + trueParetoFront) ;
    hy_ = new Hypervolume();
    jmetal.qualityIndicator.util.MetricsUtil mu = new jmetal.qualityIndicator.util.MetricsUtil();
    trueFront_ = mu.readNonDominatedSolutionSet(trueParetoFront);
    hy_.hypervolume(trueFront_.writeObjectivesToMatrix(),
      trueFront_.writeObjectivesToMatrix(),
      problem_.getNumberOfObjectives());


    // Default configuration
    r1Max_ = 1.0;
    r1Min_ = 0.0;
    r2Max_ = 1.0;
    r2Min_ = 0.0;
    C1Max_ = 2.5;
    C1Min_ = 1.5;
    C2Max_ = 2.5;
    C2Min_ = 1.5;
    WMax_ = 0.1;
    WMin_ = 0.1;
    ChVel1_ = -1;
    ChVel2_ = -1;
  } // Constructor

  /**
   * Initialize all parameter of the algorithm
   */
  @SuppressWarnings("unchecked")
	public void initParams() {
    particlesSize_ = ((Integer) getInputParameter("swarmSize")).intValue();
    archiveSize_ = ((Integer) getInputParameter("archiveSize")).intValue();
    maxIterations_ = ((Integer) getInputParameter("maxIterations")).intValue();
    mutationDistributionIndex_ = ((Double) getInputParameter("mutationDistributionIndex")).intValue();
    //eta_           = ((Double)getInputParameter("eta")).doubleValue();

    indicators_ = (QualityIndicator<V>) getInputParameter("indicators");
    requiredEvaluations_ = 0;

    iteration_ = 0 ;

    success_ = false;

    particles_ = new SolutionSet<V>(particlesSize_);
    best_ = new Solution[particlesSize_];
    leaders_ = new CrowdingArchive<V>(archiveSize_, problem_.getNumberOfObjectives());

    // Create the dominator for equadless and dominance
    dominance_ = new DominanceComparator<V>();
    crowdingDistanceComparator_ = new CrowdingDistanceComparator<V>();

    // Create the speed_ vector
    speed_ = new double[particlesSize_][problem_.getNumberOfVariables()];

    uniformMutation_ = new UniformMutation();
    uniformMutation_.setPerturbationIndex(perturbation_);
    uniformMutation_.setProbability(1.0 / problem_.getNumberOfVariables());
    nonUniformMutation_ = new NonUniformMutation();
    nonUniformMutation_.setPerturbation(perturbation_);
    nonUniformMutation_.setMaxIterations(maxIterations_);
    nonUniformMutation_.setProbability(1.0 / problem_.getNumberOfVariables());
    polynomialMutation_ = new PolynomialMutation<V>() ;
    polynomialMutation_.setDistributionIndex(mutationDistributionIndex_);
    polynomialMutation_.setProbability(1.0 / problem_.getNumberOfVariables());

    deltaMax_ = new double[problem_.getNumberOfVariables()];
    deltaMin_ = new double[problem_.getNumberOfVariables()];
    for (int i = 0; i < problem_.getNumberOfVariables(); i++) {
      deltaMax_[i] = (problem_.getUpperLimit(i) -
        problem_.getLowerLimit(i)) / 2.0;
      deltaMin_[i] = -deltaMax_[i];
    }

  } // initParams 

  // Adaptive inertia 
  private double inertiaWeight(int iter, int miter, double wma, double wmin) {
    return wma; // - (((wma-wmin)*(double)iter)/(double)miter);
  } // inertiaWeight

  // constriction coefficient (M. Clerc)
  private double constrictionCoefficient(double c1, double c2) {
    double rho = c1 + c2;
    //rho = 1.0 ;
    if (rho <= 4) {
      return 1.0;
    } else {
      return 2 / (2 - rho - Math.sqrt(Math.pow(rho, 2.0) - 4.0 * rho));
    }
  } // constrictionCoefficient


  // velocity bounds
  private double velocityConstriction(double v, double[] deltaMax,
                                      double[] deltaMin, int variableIndex,
                                      int particleIndex) throws IOException {


    //System.out.println("v: " + v + "\tdmax: " + dmax + "\tdmin: " + dmin) ;
    double result;

    double dmax = deltaMax[variableIndex];
    double dmin = deltaMin[variableIndex];

    result = v;

    if (v > dmax) {
      result = dmax;
    }

    if (v < dmin) {
      result = dmin;
    }

    return result;
  } // velocityConstriction

  /**
   * Update the speed of each particle
   * @throws JMException 
   */
  private void computeSpeed(int iter, int miter) throws JMException, IOException {
    double r1, r2, C1, C2;
    double wmax, wmin;
    DecisionVariables<V> bestGlobal;

    for (int i = 0; i < particlesSize_; i++) {
      DecisionVariables<V> particle = particles_.get(i).getDecisionVariables();
      DecisionVariables<V> bestParticle = best_[i].getDecisionVariables();

      //Select a global best_ for calculate the speed of particle i, bestGlobal
      Solution<V> one, two;
      int pos1 = PseudoRandom.randInt(0, leaders_.size() - 1);
      int pos2 = PseudoRandom.randInt(0, leaders_.size() - 1);
      one = leaders_.get(pos1);
      two = leaders_.get(pos2);

      if (crowdingDistanceComparator_.compare(one, two) < 1) {
        bestGlobal = one.getDecisionVariables();
      } else {
        bestGlobal = two.getDecisionVariables();
      //Params for velocity equation
      }
      r1 = PseudoRandom.randDouble(r1Min_, r1Max_);
      r2 = PseudoRandom.randDouble(r2Min_, r2Max_);
      C1 = PseudoRandom.randDouble(C1Min_, C1Max_);
      C2 = PseudoRandom.randDouble(C2Min_, C2Max_);

      wmax = WMax_;
      wmin = WMin_;

      for (int var = 0; var < particle.size(); var++) {
        //Computing the velocity of this particle 
        speed_[i][var] = velocityConstriction(constrictionCoefficient(C1, C2) *
          (inertiaWeight(iter, miter, wmax, wmin) *
          speed_[i][var] +
          C1 * r1 * (bestParticle.variables_.get(var).getValue() -
          particle.variables_.get(var).getValue()) +
          C2 * r2 * (bestGlobal.variables_.get(var).getValue() -
          particle.variables_.get(var).getValue())), deltaMax_, //[var],
          deltaMin_, //[var], 
          var,
          i);
      }
    }
  } // computeSpeed

  /**
   * Update the position of each particle
   * @throws JMException 
   */
  private void computeNewPositions() throws JMException {
    for (int i = 0; i < particlesSize_; i++) {
      DecisionVariables<V> particle = particles_.get(i).getDecisionVariables();
      //particle.move(speed_[i]);
      for (int var = 0; var < particle.size(); var++) {
        particle.variables_.get(var).setValue((particle.variables_.get(var).getValue() + speed_[i][var]));
        if (particle.variables_.get(var).getValue() < problem_.getLowerLimit(var)) {
          particle.variables_.get(var).setValue(problem_.getLowerLimit(var));
          speed_[i][var] = speed_[i][var] * ChVel1_; //    
        }
        if (particle.variables_.get(var).getValue() > problem_.getUpperLimit(var)) {
          particle.variables_.get(var).setValue(problem_.getUpperLimit(var));
          speed_[i][var] = speed_[i][var] * ChVel2_; //   
        }
      }
    }
  } // computeNewPositions

  /**
   * Apply a mutation operator to all particles in the swarm
   * @throws JMException 
   */
  private void mopsoMutation(int actualIteration, int totalIterations) throws JMException {
    //There are three groups of particles_, the ones that are mutated with
    //a non-uniform mutation operator, the ones that are mutated with a 
    //uniform mutation and the one that no are mutated
    nonUniformMutation_.setCurrentIteration(actualIteration);
    //*/

    for (int i = 0; i < particles_.size(); i++) {
      if ( (i % 6) == 0)
        polynomialMutation_.execute(particles_.get(i)) ;
      //if (i % 3 == 0) { //particles_ mutated with a non-uniform mutation %3
      //  nonUniformMutation_.execute(particles_.get(i));
      //} else if (i % 3 == 1) { //particles_ mutated with a uniform mutation operator
      //  uniformMutation_.execute(particles_.get(i));
      //} else //particles_ without mutation
      //;
    }
  } // mopsoMutation

  /**   
   * Runs of the SMPSO algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution  
   * @throws JMException 
   */
  public SolutionSet<V> execute() throws JMException {
    initParams();

    success_ = false;
    //->Step 1 (and 3) Create the initial population and evaluate
    for (int i = 0; i < particlesSize_; i++) {
      Solution<V> particle = new Solution<V>(problem_);
      problem_.evaluate(particle);
      problem_.evaluateConstraints(particle);
      particles_.add(particle);
    }

    //-> Step2. Initialize the speed_ of each particle to 0
    for (int i = 0; i < particlesSize_; i++) {
      for (int j = 0; j < problem_.getNumberOfVariables(); j++) {
        speed_[i][j] = 0.0;
      }
    }


    // Step4 and 5   
    for (int i = 0; i < particles_.size(); i++) {
      Solution<V> particle = new Solution<V>(particles_.get(i));
      leaders_.add(particle);
    }

    //-> Step 6. Initialize the memory of each particle
    for (int i = 0; i < particles_.size(); i++) {
      Solution<V> particle = new Solution<V>(particles_.get(i));
      best_[i] = particle;
    }

    //Crowding the leaders_
    Distance.crowdingDistanceAssignment(leaders_, problem_.getNumberOfObjectives());

    //-> Step 7. Iterations ..        
    while (iteration_ < maxIterations_) {
      try {
        //Compute the speed_
        computeSpeed(iteration_, maxIterations_);
      } catch (IOException ex) {
        Logger.getLogger(SMPSO.class.getName()).log(Level.SEVERE, null, ex);
      }

      //Compute the new positions for the particles_            
      computeNewPositions();

      //Mutate the particles_          
      mopsoMutation(iteration_, maxIterations_);

      //Evaluate the new particles_ in new positions
      for (int i = 0; i < particles_.size(); i++) {
        Solution<V> particle = particles_.get(i);
        problem_.evaluate(particle);
      }

      //Actualize the archive          
      for (int i = 0; i < particles_.size(); i++) {
        Solution<V> particle = new Solution<V>(particles_.get(i));
        leaders_.add(particle);
      }

      //Actualize the memory of this particle
      for (int i = 0; i < particles_.size(); i++) {
        int flag = dominance_.compare(particles_.get(i), best_[i]);
        if (flag != 1) { // the new particle is best_ than the older remeber        
          Solution<V> particle = new Solution<V>(particles_.get(i));
          //this.best_.reemplace(i,particle);
          best_[i] = particle;
        }
      }

      //Crowding the leaders_
      Distance.crowdingDistanceAssignment(leaders_,
        problem_.getNumberOfObjectives());
      iteration_++;
    }
    return this.leaders_;
  } // execute

  /** 
   * Gets the leaders of the SMPSO algorithm
   */
  public SolutionSet<V> getLeader() {
    return leaders_;
  }  // getLeader   
} // SMPSO
