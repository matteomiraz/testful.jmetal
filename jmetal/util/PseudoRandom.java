/**
 * PseudoRandom.java
 *
 * @author Juan J. Durillo
 * @version 1.0
 *
 */
package jmetal.util;

import ec.util.MersenneTwisterFast;

/**
 * Manages random number generators
 */
public abstract class PseudoRandom {

	/** the seed being used to generate numbers */
	private static long seed;
	/**
	 * Returns the seed being used to generate numbers
	 * @return the seed being used to generate numbers
	 */
	public static long getSeed() {
		return seed;
	}

	/** Mersenne Twister Fast random number generator */
	private static MersenneTwisterFast MT;

	/**
	 * Use Mersenne Twister to generate random numbers (and disable other random number generators)
	 * @param seed_ the seed to use
	 */
	public static void setupMersenneTwisterFast(long seed_) {
		seed = seed_;
		MT = new MersenneTwisterFast(seed);
		randomJava = null;
	}


	/** java random number generator */
	private static java.util.Random randomJava;

	/**
	 * Use the java's Random class to generate random numbers (and disable other random number generators)
	 * @param seed_ the seed to use
	 */
	public static void setupJavaRandom(long seed_) {
		seed = seed_;
		MT = null;
		randomJava = new java.util.Random(seed);
	}

	static {
		setupMersenneTwisterFast(System.currentTimeMillis());
	}

	public static MersenneTwisterFast getMersenneTwisterFast() {
		return MT;
	}

	/**
	 * Returns a random int value using the Java random generator.
	 * @return A random int value.
	 */
	public static int randInt() {
		if (MT != null)
			return MT.nextInt();

		return randomJava.nextInt();
	} // randInt

	/**
	 * Returns a random double value using the PseudoRandom generator.
	 * @return A random double value.
	 */
	public static double randDouble() {
		if (MT != null)
			return MT.nextDouble();

		return randomJava.nextDouble();
	} // randDouble

	/**
	 * Returns a random int value between a minimum bound and maximum bound
	 * using the PseudoRandom generator.
	 *
	 * @param minBound The minimum bound.
	 * @param maxBound The maximum bound.
	 * @return A pseudo random int value between minBound and maxBound.
	 */
	public static int randInt(int minBound, int maxBound) {
		if (MT != null)
			return minBound + MT.nextInt(maxBound - minBound + 1);

		 return minBound + randomJava.nextInt(maxBound-minBound+1);
	} // randInt

	/**
	 * Returns a random double value between a minimum bound and a maximum bound
	 * using the PseudoRandom generator.
	 *
	 * @param minBound The minimum bound.
	 * @param maxBound The maximum bound.
	 * @return A pseudo random double value between minBound and maxBound
	 */
	public static double randDouble(double minBound, double maxBound) {
		if (MT != null)
			return minBound + (maxBound - minBound) * MT.nextDouble();

		return minBound + (maxBound - minBound)*randomJava.nextDouble();
	} // randDouble

} // PseudoRandom
