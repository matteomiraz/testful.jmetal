/*****
 * OMOPSO.java
 * 
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.metaheuristics.omopso;

import java.util.Comparator;

import jmetal.base.Algorithm;
import jmetal.base.DecisionVariables;
import jmetal.base.ProblemValue;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.archive.CrowdingArchive;
import jmetal.base.operator.comparator.CrowdingDistanceComparator;
import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.base.operator.comparator.EpsilonDominanceComparator;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.localSearch.LocalSearch;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.mutation.NonUniformMutation;
import jmetal.base.operator.mutation.UniformMutation;
import jmetal.base.operator.selection.Selection;
import jmetal.base.variable.Real;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.NonDominatedSolutionList;
import jmetal.util.PseudoRandom;

/**
 * This class representing an asychronous version of OMOPSO algorithm
 */
public class OMOPSO
	extends Algorithm<Real, Crossover<Real>, Mutation<Real>, Selection<Real, Solution<Real>>, LocalSearch<Real>> {
               
  private static final long serialVersionUID = 5286767531955701063L;

	/**
  * Stores the problem to solve
  */
  private ProblemValue<Real> problem_;
  
  /**
   * Stores the number of particles_ used
   */
  private int particlesSize_;
  
  /**
  * Stores the maximum size for the archive
  */
  private int archiveSize_;
  
  /**
  * Stores the current number of iteration_
  */
  private int iteration_;
  
  /**
  * Stores the perturbation used by the non-uniform mutation
  */
  private double perturbation_;
  
  /**
  * Stores the particles
  */
  private SolutionSet<Real> particles_;
  
  /**
   * Stores the best_ solutions founds so far for each particles
   */
  private Solution<Real>[] best_;
  
  /**
  * Stores the leaders_
  */
  private CrowdingArchive<Real> leaders_ ;
  
  /**
  * Stores the epsilon-archive
  */
  private NonDominatedSolutionList<Real> eArchive_;
  
  /**
  * Stores the speed_ of each particle
  */
  private double [][] speed_;  
  
  /**
  * Stores a comparator for checking dominance
  */
  private Comparator<Solution<Real>> dominance_;
  
  /**
  * Stores a comparator for crowding checking
  */
  private Comparator<Solution<Real>> crowdingDistanceComparator_;
  
  /**
  * Stores a operator for uniform mutations
  */
  private UniformMutation uniformMutation_;
  
  /**
  * Stores a operator for non uniform mutations
  */ 
  private NonUniformMutation nonUniformMutation_;
  
  /**
  * eta_ value
  */
  private double eta_ = 0.0075;


  /** 
  * Constructor
  * @param problem Problem to solve
  */    
  public OMOPSO(ProblemValue<Real> problem) {                
    problem_ = problem;        
  } // OMOPSO
  
  
	public void setSwarmSize(int particlesSize) {
		particlesSize_ = particlesSize;
	}
  
	public void setArchiveSize(int archiveSize) {
		archiveSize_ = archiveSize;
	}
	
	public void setPerturbationIndex(double perturbation) {
		perturbation_ = perturbation;
	}

	/**
   * Initialize all parameter of the algorithm
   */
  @SuppressWarnings("unchecked")
	public void initParams(){
    particles_     = new SolutionSet<Real>(particlesSize_);        
    best_          = new Solution[particlesSize_];
    leaders_       = new CrowdingArchive<Real>(archiveSize_,problem_.getNumberOfObjectives());
    eArchive_      = new NonDominatedSolutionList<Real>(new EpsilonDominanceComparator<Real>(eta_));
    

    // Create the dominator for equadless and dominance
    dominance_          = new DominanceComparator<Real>();    
    crowdingDistanceComparator_ = new CrowdingDistanceComparator<Real>();
    
    // Create the speed_ vector
    speed_ = new double[particlesSize_][problem_.getNumberOfVariables()];
    
    uniformMutation_ = new UniformMutation();
    uniformMutation_.setPerturbationIndex(perturbation_);
    uniformMutation_.setProbability(1.0/problem_.getNumberOfVariables());
    nonUniformMutation_ = new NonUniformMutation();
    nonUniformMutation_.setPerturbation(perturbation_);        
    nonUniformMutation_.setMaxIterations(getMaxEvaluations());
    nonUniformMutation_.setProbability(1.0/problem_.getNumberOfVariables());
  } // initParams
           
  
  /**
   * Update the spped of each particle
   * @throws JMException 
   */
  private void computeSpeed() throws JMException{        
    double r1,r2,W,C1,C2; 
    DecisionVariables<Real> bestGlobal;                                            
        
    for (int i = 0; i < particlesSize_; i++){
      DecisionVariables<Real> particle     = particles_.get(i).getDecisionVariables();
      DecisionVariables<Real> bestParticle = best_[i].getDecisionVariables();                        

      //Select a global best_ for calculate the speed of particle i, bestGlobal
      Solution<Real> one, two;
      int pos1 = PseudoRandom.randInt(0,leaders_.size()-1);
      int pos2 = PseudoRandom.randInt(0,leaders_.size()-1);
      one = leaders_.get(pos1);
      two = leaders_.get(pos2);

      if (crowdingDistanceComparator_.compare(one,two) < 1)
        bestGlobal = one.getDecisionVariables();
      else
        bestGlobal = two.getDecisionVariables();
      //
            
      //Params for velocity equation
      r1 = PseudoRandom.randDouble();
      r2 = PseudoRandom.randDouble();
      C1 = PseudoRandom.randDouble(1.5,2.0);
      C2 = PseudoRandom.randDouble(1.5,2.0);
      W  = PseudoRandom.randDouble(0.1,0.5);            
      //

      for (int var = 0; var < particle.size(); var++){                                     
        //Computing the velocity of this particle
        speed_[i][var] = W  * speed_[i][var] +
                   C1 * r1 * (bestParticle.variables_.get(var).getValue() - 
                              particle.variables_.get(var).getValue()) +
                   C2 * r2 * (bestGlobal.variables_.get(var).getValue() - 
                              particle.variables_.get(var).getValue());
      }
                
    }
  } // computeSpeed
     
  /**
   * Update the position of each particle
   * @throws JMException 
   */
  private void computeNewPositions() throws JMException{
    for (int i = 0; i < particlesSize_; i++){
      DecisionVariables<Real> particle = particles_.get(i).getDecisionVariables();
      //particle.move(speed_[i]);
      for (int var = 0; var < particle.size(); var++){
        particle.variables_.get(var).setValue(particle.variables_.get(var).getValue()+ speed_[i][var]);
        if (particle.variables_.get(var).getValue() < problem_.getLowerLimit(var)){
          particle.variables_.get(var).setValue(problem_.getLowerLimit(var));                    
          speed_[i][var] = speed_[i][var] * -1.0;    
        }
        if (particle.variables_.get(var).getValue() > problem_.getUpperLimit(var)){
          particle.variables_.get(var).setValue(problem_.getUpperLimit(var));                    
          speed_[i][var] = speed_[i][var] * -1.0;    
        }                                             
      }
    }
  } // computeNewPositions
        
   
  /**
   * Apply a mutation operator to all particles in the swarm
   * @throws JMException 
   */
  private void mopsoMutation(int actualIteration, int totalIterations) throws JMException{       
    //There are three groups of particles_, the ones that are mutated with
    //a non-uniform mutation operator, the ones that are mutated with a 
    //uniform mutation and the one that no are mutated
    nonUniformMutation_.setCurrentIteration(actualIteration);
    //*/

    for (int i = 0; i < particles_.size();i++)            
      if (i % 3 == 0) { //particles_ mutated with a non-uniform mutation
        nonUniformMutation_.execute(particles_.get(i));                                
      } else if (i % 3 == 1) { //particles_ mutated with a uniform mutation operator
        uniformMutation_.execute(particles_.get(i));                
      } else //particles_ without mutation
          ;      
  } // mopsoMutation
   
    
  /**   
  * Runs of the OMOPSO algorithm.
  * @return a <code>SolutionSet</code> that is a set of non dominated solutions
  * as a result of the algorithm execution  
   * @throws JMException 
  */  
  public SolutionSet<Real> execute() throws JMException{
    initParams();

    //->Step 1 (and 3) Create the initial population and evaluate
    for (int i = 0; i < particlesSize_; i++){
      Solution<Real> particle = new Solution<Real>(problem_);
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
    for (int i = 0; i < particles_.size(); i++){
      Solution<Real> particle = new Solution<Real>(particles_.get(i));            
      if (leaders_.add(particle)){
        eArchive_.add(new Solution<Real>(particle));
      }
    }
                
    //-> Step 6. Initialice the memory of each particle
    for (int i = 0; i < particles_.size(); i++){
      Solution<Real> particle = new Solution<Real>(particles_.get(i));           
      best_[i] = particle;
    }
        
    //Crowding the leaders_
    Distance.crowdingDistanceAssignment(leaders_,problem_.getNumberOfObjectives());        

    //-> Step 7. Iterations ..        
    while (iteration_ < getMaxEvaluations()){
      //Compute the speed_        
      computeSpeed();
            
      //Compute the new positions for the particles_            
      computeNewPositions();

      //Mutate the particles_          
      mopsoMutation(iteration_,getMaxEvaluations());                       
            
      //Evaluate the new particles_ in new positions
      for (int i = 0; i < particles_.size(); i++){
        Solution<Real> particle = particles_.get(i);
        problem_.evaluate(particle);                
        problem_.evaluateConstraints(particle);                
      }
            
      //Actualize the archive          
      for (int i = 0; i < particles_.size(); i++){
        Solution<Real> particle = new Solution<Real>(particles_.get(i));                
        if (leaders_.add(particle)){
          eArchive_.add(new Solution<Real>(particle));
        }                
      }
            
      //Actualize the memory of this particle
      for (int i = 0; i < particles_.size();i++){
        int flag = dominance_.compare(particles_.get(i),best_[i]);
        if (flag != 1) { // the new particle is best_ than the older remeber        
          Solution<Real> particle = new Solution<Real>(particles_.get(i));                    
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
    //return eArchive_;
  } // execute
    
  /** 
   * Gets the leaders of the OMOPSO algorithm
   */
  public SolutionSet<Real> getLeader(){
    return leaders_;
  }  // getLeader 
} // OMOPSO