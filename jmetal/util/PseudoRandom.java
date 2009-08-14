/**
 * PseudoRandom.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 *
 */
package jmetal.util;


/**
 * Class representing some randoms facilities
 */
public abstract class PseudoRandom {
  
	private static final MersenneTwisterFast MT;
	
  /** generator used to obtain the random values */
  private static final RandomGenerator random;
 
  /** other generator used to obtain the random values */
  private static final java.util.Random randomJava;
    
  static {
  	// configuration 1: use mersenne twister fast
  	MT = new MersenneTwisterFast();
  	random = null;
  	randomJava = null;
  	
  	// configuration 2: use RandomGenerator & java.util.Random
  	// MT = null;
  	//random = new RandomGenerator();
  	//randomJava = new java.util.Random();
  }
  
  
	public static MersenneTwisterFast getMersenneTwisterFast() {
		return MT;
	}
  
  /** 
   * Returns a random int value using the Java random generator.
   * @return A random int value.
   */
  public static int randInt() {
  	if(MT != null) return MT.nextInt();
  	
    return randomJava.nextInt();
  } // randInt
    
  /** 
   * Returns a random double value using the PseudoRandom generator.
   * Returns A random double value.
   */
  public static double randDouble() {
  	if(MT != null) return MT.nextDouble();
  	
    return random.rndreal(0.0,1.0);
    //return randomJava.nextDouble();
  } // randDouble
    
  /** 
   * Returns a random int value between a minimum bound and maximum bound using
   * the PseudoRandom generator.
   * @param minBound The minimum bound.
   * @param maxBound The maximum bound.
   * Return A pseudo random int value between minBound and maxBound.
   */
  public static int randInt(int minBound, int maxBound) {
  	if(MT != null) return minBound + MT.nextInt(maxBound-minBound+1);
  	
  	return random.rnd(minBound,maxBound);
    //return minBound + randomJava.nextInt(maxBound-minBound+1);
  } // randInt
    
  /** Returns a random double value between a minimum bound and a maximum bound
   * using the PseudoRandom generator.
   * @param minBound The minimum bound.
   * @param maxBound The maximum bound.
   * @return A pseudo random double value between minBound and maxBound
   */
  public static double randDouble(double minBound, double maxBound) {
  	if(MT != null) return minBound + (maxBound - minBound)*randomJava.nextDouble();
  	
    return random.rndreal(minBound,maxBound);
    //return minBound + (maxBound - minBound)*randomJava.nextDouble();
  } // randDouble    
} // PseudoRandom
