/**
 * Neighborhood.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 */

package jmetal.util;

import jmetal.base.SolutionSet;
import jmetal.base.Variable;

/**
 * Class representing neighborhoods for a <code>Solution</code> into a
 * <code>SolutionSet</code>.
 */ 
public class Neighborhood<T extends Variable> {    
  
  /**
   * Maximum rate considered
   */
  private static int MAXRADIO = 2;
  
  /**
   * Stores the neighborhood.
   * structure_ [i] represents a neighborhood for a solution.
   * structure_ [i][j] represents a neighborhood with a ratio.
   * structure_ [i][j][k] represents a neighbor of a solution.
   */
  private int [][][] structure_;
  
  /**
   * Stores the size of the solutionSet.
   */
  private int solutionSetSize_;
  
  /**
   * Stores the size for each row
   */
  private int rowSize_;
  
  /**
   * Enum type for defining the North, South, East, West, North-West, South-West,
   * North-East, South-East neighbor.
   */
  enum Row {N, S, E, W, NW, SW, NE, SE};
    
  /**
   * Constructor.
   * Defines a neighborhood of a given size.
   * @param solutionSetSize The size.
   */
  public Neighborhood(int solutionSetSize) {            
    solutionSetSize_ = solutionSetSize;
    //Create the structure_ for store the neighborhood
    structure_ = new int[solutionSetSize_][MAXRADIO][];
        
    //For each individual, and different rates the individual has a different 
    //number of neighborhoods
    for (int ind = 0; ind < solutionSetSize_; ind ++) {
      for (int radio = 0; radio < MAXRADIO; radio ++) {
        if (radio == 0) {//neighboors whit rate 1
          structure_[ind][radio] = new int[8];
        } else if (radio == 1) { //neighboors whit rate 2
          structure_[ind][radio] = new int[24];       
        } // if
      } // for
    } // for
        
    //Calculate the size of a row
    rowSize_ = (int) Math.sqrt((double)solutionSetSize_);
        
        
    //Calculates the neighbors of a individual 
    for (int ind = 0; ind < solutionSetSize_; ind++){
      //rate 1
      //North neighbors
      if (ind > rowSize_ - 1){
        structure_[ind][0][Row.N.ordinal()] = ind - rowSize_;
      } else {
        structure_[ind][0][Row.N.ordinal()] = 
       (ind - rowSize_ + solutionSetSize) % solutionSetSize;                                                          
      }
            
      //East neighbors
      if  ((ind + 1) % rowSize_ == 0)
        structure_[ind][0][Row.E.ordinal()] = (ind - (rowSize_ - 1));
      else
        structure_[ind][0][Row.E.ordinal()] = (ind + 1);

      //Western neigbors
      if (ind % rowSize_ == 0) {
        structure_[ind][0][Row.W.ordinal()] = (ind + (rowSize_ - 1));
      } else {
        structure_[ind][0][Row.W.ordinal()] = (ind - 1);
      }

      //South neigbors
      structure_[ind][0][Row.S.ordinal()] = (ind + rowSize_) % solutionSetSize;                        
    }                
        
    for (int ind = 0; ind < solutionSetSize_; ind++){
      structure_[ind][0][Row.NE.ordinal()] = 
        structure_[structure_[ind][0][Row.N.ordinal()]][0][Row.E.ordinal()];
      structure_[ind][0][Row.NW.ordinal()] = 
        structure_[structure_[ind][0][Row.N.ordinal()]][0][Row.W.ordinal()];
      structure_[ind][0][Row.SE.ordinal()] = 
        structure_[structure_[ind][0][Row.S.ordinal()]][0][Row.E.ordinal()];
      structure_[ind][0][Row.SW.ordinal()] = 
        structure_[structure_[ind][0][Row.S.ordinal()]][0][Row.W.ordinal()];
    }
  } // Neighborhood
    
  /**
   * Returns a <code>SolutionSet</code> with the North, Sout, East and West
   * neighbors solutions of ratio 0 of a given location into a given 
   * <code>SolutionSet</code>.
   * @param solutionSet The <code>SolutionSet</code>.
   * @param location The location.
   * @return a <code>SolutionSet</code> with the neighbors.
   */
  public SolutionSet<T> getFourNeighbors(SolutionSet<T> solutionSet, int location){
    //SolutionSet that contains the neighbors (to return)
    SolutionSet<T> neighbors;
        
    //instance the solutionSet to a non dominated li of individuals
    neighbors = new SolutionSet<T>(24);
        
    //Gets the neighboords N, S, E, W
    int index;        
        
    //North
    index = structure_[location][0][Row.N.ordinal()];        
    neighbors.add(solutionSet.get(index));
      
    //South
    index = structure_[location][0][Row.S.ordinal()];
    neighbors.add(solutionSet.get(index));

    //East
    index = structure_[location][0][Row.E.ordinal()];
    neighbors.add(solutionSet.get(index));

    //West
    index = structure_[location][0][Row.W.ordinal()];
    neighbors.add(solutionSet.get(index));         
    
    //Return the list of non-dominated individuals
    return neighbors;        
  } //getFourNeighbors           
    
  /**
   * Returns a <code>SolutionSet</code> with the North, Sout, East, West, 
   * North-West, South-West, North-East and South-East neighbors solutions of
   * ratio 0 of a given location into a given <code>SolutionSet</code>.
   * solutions of a given location into a given <code>SolutionSet</code>.
   * @param population The <code>SolutionSet</code>.
   * @param individual The individual.
   * @return a <code>SolutionSet</code> with the neighbors.
   */
  public SolutionSet<T> getEightNeighbors(SolutionSet<T> population, int individual){
    //SolutionSet that contains the neighbors (to return)
    SolutionSet<T> neighbors;

    //instance the population to a non dominated li of individuals
    neighbors = new SolutionSet<T>(24);

    //Gets the neighboords N, S, E, W
    int index;        
        
    //N
    index = this.structure_[individual][0][Row.N.ordinal()];        
    neighbors.add(population.get(index));

    //S
    index = this.structure_[individual][0][Row.S.ordinal()];
    neighbors.add(population.get(index));

    //E
    index = this.structure_[individual][0][Row.E.ordinal()];
    neighbors.add(population.get(index));

    //W
    index = this.structure_[individual][0][Row.W.ordinal()];
    neighbors.add(population.get(index));

    //NE
    index = this.structure_[individual][0][Row.NE.ordinal()];
    neighbors.add(population.get(index));

    //NW
    index = this.structure_[individual][0][Row.NW.ordinal()];
    neighbors.add(population.get(index));

    //SE
    index = this.structure_[individual][0][Row.SE.ordinal()];
    neighbors.add(population.get(index));

    //SW
    index = this.structure_[individual][0][Row.SW.ordinal()];
    neighbors.add(population.get(index));


    //Return the list of non-dominated individuals
    return neighbors;        
  }  // getEightNeighbors
} // Neighborhood
