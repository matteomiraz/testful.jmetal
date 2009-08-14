/**
* Distance.java
*
* @author Antonio J. Nebro
* @author Juanjo Durillo
* @version 1.0
*/
package jmetal.util;

import jmetal.base.DecisionVariables;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.base.VariableValue;
import jmetal.base.operator.comparator.ObjectiveComparator;

/**
 * This class implements some facilities for distances
 */
public class Distance {      
    
  /** 
  * Constructor.
  */
  public Distance() {
    //do nothing.
  } // Distance
        
    
  /** 
  * Returns a matrix with distances between solutions in a 
  * <code>SolutionSet</code>.
  * @param solutionSet The <code>SolutionSet</code>.
  * @return a matrix with distances.
  */
  public static <T extends Variable> double[][] distanceMatrix(SolutionSet<T> solutionSet) {
    Solution<T> solutionI, solutionJ;

    //The matrix of distances
    double [][] distance = new double [solutionSet.size()][solutionSet.size()];        
    //-> Calculate the distances
    for (int i = 0; i < solutionSet.size(); i++){
      distance[i][i] = 0.0;
      solutionI = solutionSet.get(i);
      for (int j = i + 1; j < solutionSet.size(); j++){
        solutionJ = solutionSet.get(j);
        distance[i][j] = distanceBetweenObjectives(solutionI,solutionJ);                
        distance[j][i] = distance[i][j];            
      } // for
    } // for        
    
    //->Return the matrix of distances
    return distance;
  } // distanceMatrix
    
 /** Returns the minimum distance from a <code>Solution</code> to a 
  * <code>SolutionSet</code>.
  * @param solution The <code>Solution</code>.
  * @param solutionSet The <code>SolutionSet</code>.
  * @return The minimum distance between solution and the set.
 * @throws JMException 
  */  
  public static <J extends VariableValue> double distanceToSolutionSet(Solution<J> solution, 
		                              SolutionSet<J> solutionSet) throws JMException{
    //At start point the distance is the max
    double distance = Double.MAX_VALUE;    
        
    // found the min distance respect to population
    for (int i = 0; i < solutionSet.size();i++){            
      double aux = distanceBetweenSolutions(solution,solutionSet.get(i));
      if (aux < distance)
        distance = aux;
    } // for
    
    //->Return the best distance
    return distance;
  } // distanceToSolutionSet
    
    
 /** Returns the distance between two solutions in the search space.
  *  @param solutionI The first <code>Solution</code>. 
  *  @param solutionJ The second <code>Solution</code>.
  *  @return the distance between solutions.
 * @throws JMException 
  */
  public static <J extends VariableValue> double distanceBetweenSolutions(Solution<J> solutionI, Solution<J> solutionJ) 
  throws JMException{                
    //->Obtain his decision variables
    DecisionVariables<J> decisionVariableI = solutionI.getDecisionVariables();
    DecisionVariables<J> decisionVariableJ = solutionJ.getDecisionVariables();    
    
    double diff;    //Auxiliar var
    double distance = 0.0;
    //-> Calculate the Euclidean distance
    for (int i = 0; i < decisionVariableI.size(); i++){
      diff = decisionVariableI.variables_.get(i).getValue() -
             decisionVariableJ.variables_.get(i).getValue();
      distance += Math.pow(diff,2.0);
    } // for    
        
    //-> Return the euclidean distance
    return Math.sqrt(distance);
  } // distanceBetweenSolutions
    
 /** Returns the distance between two solutions in objective space.
  *  @param solutionI The first <code>Solution</code>.
  *  @param solutionJ The second <code>Solution</code>.
  *  @return the distance between solutions in objective space.
  */
  public static <T extends Variable> double distanceBetweenObjectives(Solution<T> solutionI, Solution<T> solutionJ){                
    double diff;    //Auxiliar var
    double distance = 0.0;
    //-> Calculate the euclidean distance
    for (int nObj = 0; nObj < solutionI.numberOfObjectives();nObj++){
      diff = solutionI.getObjective(nObj) - solutionJ.getObjective(nObj);
      distance += Math.pow(diff,2.0);           
    } // for   
        
    //Return the euclidean distance
    return Math.sqrt(distance);
  } // distanceBetweenObjectives.
           
 /** Assigns crowding distances to all solutions in a <code>SolutionSet</code>.
  * @param solutionSet The <code>SolutionSet</code>.
  * @param nObjs Number of objectives.
  */
  public static <T extends Variable> void crowdingDistanceAssignment(SolutionSet<T> solutionSet, int nObjs) {
    int size = solutionSet.size();        
                
    if (size == 0)
      return;
    
    if (size == 1) {
      solutionSet.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
      return;
    } // if
        
    if (size == 2) {
      solutionSet.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
      solutionSet.get(1).setCrowdingDistance(Double.POSITIVE_INFINITY);
      return;
    } // if       
        
    //Use a new SolutionSet to evite alter original solutionSet
    SolutionSet<T> front = new SolutionSet<T>(size);
    for (int i = 0; i < size; i++){
      front.add(solutionSet.get(i));
    }
        
    for (int i = 0; i < size; i++)
      front.get(i).setCrowdingDistance(0.0);        
        
    double objetiveMaxn;
    double objetiveMinn;
    double distance;
                
    for (int i = 0; i<nObjs; i++) {          
      // Sort the population by Obj n            
      front.sort(new ObjectiveComparator<T>(i));
      objetiveMinn = front.get(0).getObjective(i);      
      objetiveMaxn = front.get(front.size()-1).getObjective(i);      
      
      //Set de crowding distance            
      front.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
      front.get(size-1).setCrowdingDistance(Double.POSITIVE_INFINITY);                                      
      
      for (int j = 1; j < size-1; j++) {
        distance = front.get(j+1).getObjective(i) - front.get(j-1).getObjective(i);                    
        distance = distance / (objetiveMaxn - objetiveMinn);        
        distance += front.get(j).getCrowdingDistance();                
        front.get(j).setCrowdingDistance(distance);   
      } // for
    } // for        
  } // crowdingDistanceAssing            
} // Distance

