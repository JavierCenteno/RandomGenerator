/*
 * This is an API for random number generation.
 * Copyright (C) 2018 Javier Centeno Vega
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package api;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * This class offers methods to generate random variables.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @since 1.0
 * 
 */
public interface RandomGenerator {

	// -----------------------------------------------------------------------------
	// Random methods

	/**
	 * This method returns a Random object that wraps this generator.
	 * 
	 * @see Random
	 * @return A Random object that wraps this generator.
	 */
	public default Random getRandom() {
		/**
		 * This class wraps a RandomGenerator using the standard library Random so you
		 * can pass generators implemented with RandomGenerator to methods that require
		 * a Random object.
		 * 
		 * @see Random
		 * @author Javier Centeno Vega <jacenve@telefonica.net>
		 * @version 1.0
		 * @since 1.0
		 * 
		 */
		class RandomWrapper extends Random {

			// -----------------------------------------------------------------------------
			// Class fields

			private static final long serialVersionUID = 1L;

			// -----------------------------------------------------------------------------
			// Instance fields

			RandomGenerator source;

			// -----------------------------------------------------------------------------
			// Instance initializers

			public RandomWrapper(RandomGenerator source) {
				this.source = source;
			}

			// -----------------------------------------------------------------------------
			// Instance methods

			@Override
			public void setSeed(long seed) {
				// Ignore the seed that the Random() constructor will try to impose on us.
			}

			@Override
			public int next(int bits) {
				return source.generateIntegerBits(bits);
			}

			@Override
			public void nextBytes(byte[] bytes) {
				for (int i = 0; i < bytes.length; ++i) {
					bytes[i] = source.generateUniformByte();
				}
			}

			@Override
			public int nextInt() {
				return source.generateUniformInteger();
			}

			@Override
			public int nextInt(int n) {
				return source.generateUniformInteger(n);
			}

			@Override
			public long nextLong() {
				return source.generateUniformLong();
			}

			@Override
			public boolean nextBoolean() {
				return source.generateBoolean();
			}

			@Override
			public float nextFloat() {
				float result;
				do {
					result = source.generateUniformFloat();
				} while (result == 1.0f);
				return result;
			}

			@Override
			public double nextDouble() {
				double result;
				do {
					result = source.generateUniformDouble();
				} while (result == 1.0d);
				return result;
			}

			@Override
			public double nextGaussian() {
				return source.generateNormalDouble(0.0d, 1.0d);
			}

		}
		return new RandomWrapper(this);
	}

	// -----------------------------------------------------------------------------
	// Seed methods

	/**
	 * Get the size of this generator's seed in bytes.
	 * 
	 * @throws UnsupportedOperationException
	 *                                           If this generator doesn't support
	 *                                           setting its seed.
	 */
	public default int getSeedSize() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Resets this generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                           If the seed is too short.
	 * @throws UnsupportedOperationException
	 *                                           If this generator doesn't support
	 *                                           setting its seed.
	 */
	public default void setSeed(byte[] seed) {
		setState(seed);
	}

	// -----------------------------------------------------------------------------
	// State methods

	/**
	 * Get the size of this generator's state in bytes.
	 * 
	 * @throws UnsupportedOperationException
	 *                                           If this generator doesn't support
	 *                                           setting its state.
	 */
	public default int getStateSize() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get the current state of this generator.
	 * 
	 * @return The current state of this generator.
	 * @throws UnsupportedOperationException
	 *                                           If this generator doesn't support
	 *                                           getting its state.
	 */
	public default byte[] getState() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Set the state of this generator.
	 * 
	 * @param state
	 *                  A new state for this generator.
	 * @throws IllegalArgumentException
	 *                                           If the state is too short.
	 * @throws UnsupportedOperationException
	 *                                           If this generator doesn't support
	 *                                           setting its state.
	 */
	public default void setState(byte[] state) {
		throw new UnsupportedOperationException();
	}

	// -----------------------------------------------------------------------------
	// Boolean methods

	/**
	 * Get a random boolean.
	 * 
	 * @return A random boolean.
	 */
	public default boolean generateBoolean() {
		return generateUniformByte() < 0;
	}

	/**
	 * Get a random boolean.
	 * 
	 * @param probability
	 *                        Probability that this will return true.
	 * @return A random boolean.
	 */
	public default boolean generateBoolean(float probability) {
		return generateUniformFloat() < probability;
	}

	/**
	 * Get a random boolean.
	 * 
	 * @param probability
	 *                        Probability that this will return true.
	 * @return A random boolean.
	 */
	public default boolean generateBoolean(double probability) {
		return generateUniformDouble() < probability;
	}

	// -----------------------------------------------------------------------------
	// Bit methods

	/**
	 * Get a byte containing a number of random bits.
	 * 
	 * @param bits
	 *                 Number of bits to return.
	 * @return A byte containing a number of random bits.
	 * @throws IllegalArgumentException
	 *                                      If bits is not between 0 and 8.
	 */
	public default byte generateByteBits(int bits) {
		int shift = Byte.SIZE - bits;
		if (shift < 0 || bits < 0) {
			throw new IllegalArgumentException();
		}
		return (byte) (generateUniformByte() >>> shift);
	}

	/**
	 * Get a short integer containing a number of random bits.
	 * 
	 * @param bits
	 *                 Number of bits to return.
	 * @return A short integer containing a number of random bits.
	 * @throws IllegalArgumentException
	 *                                      If bits is not between 0 and 8.
	 */
	public default short generateShortBits(int bits) {
		int shift = Short.SIZE - bits;
		if (shift < 0 || bits < 0) {
			throw new IllegalArgumentException();
		}
		return (short) (generateUniformShort() >>> shift);
	}

	/**
	 * Get an integer containing a number of random bits.
	 * 
	 * @param bits
	 *                 Number of bits to return.
	 * @return An integer containing a number of random bits.
	 * @throws IllegalArgumentException
	 *                                      If bits is not between 0 and 8.
	 */
	public default int generateIntegerBits(int bits) {
		int shift = Integer.SIZE - bits;
		if (shift < 0 || bits < 0) {
			throw new IllegalArgumentException();
		}
		return generateUniformInteger() >>> shift;
	}

	/**
	 * Get a long integer containing a number of random bits.
	 * 
	 * @param bits
	 *                 Number of bits to return.
	 * @return A long integer containing a number of random bits.
	 * @throws IllegalArgumentException
	 *                                      If bits is not between 0 and 8.
	 */
	public default long generateLongBits(int bits) {
		int shift = Long.SIZE - bits;
		if (shift < 0 || bits < 0) {
			throw new IllegalArgumentException();
		}
		return generateUniformLong() >>> shift;
	}

	// -----------------------------------------------------------------------------
	// Byte methods

	/**
	 * @see RandomGenerator#generateUniformByte()
	 */
	public default byte generateByte() {
		return generateUniformByte();
	}

	/**
	 * @see RandomGenerator#generateUniformByte(byte)
	 */
	public default byte generateByte(byte max) {
		return generateUniformByte(max);
	}

	/**
	 * @see RandomGenerator#generateUniformByte(byte, byte)
	 */
	public default byte generateByte(byte min, byte max) {
		return generateUniformByte(min, max);
	}

	/**
	 * Get a random byte with a uniform distribution.
	 * 
	 * @return A random byte with a uniform distribution.
	 */
	public default byte generateUniformByte() {
		return (byte) generateUniformShort();
	}

	/**
	 * Get a random byte with a uniform distribution between 0 and a number.
	 * 
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random byte with a uniform distribution between 0 and a number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default byte generateUniformByte(byte max) {
		return (byte) generateUniformShort(max);
	}

	/**
	 * Get a random byte with a uniform distribution between two numbers.
	 * 
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random byte with a uniform distribution between two numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default byte generateUniformByte(byte min, byte max) {
		return (byte) generateUniformShort(min, max);
	}

	/**
	 * Get a random byte with a Bates distribution between two numbers.
	 * 
	 * @param min
	 *                   Minimum value of the number, inclusive.
	 * @param max
	 *                   Maximum value of the number, exclusive.
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random byte with a Bates distribution.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default byte generateBatesByte(byte min, byte max, int number) {
		return (byte) generateBatesShort(min, max, number);
	}

	/**
	 * Get a random byte with a normal distribution.
	 * 
	 * @param mean
	 *                              Mean of the distribution.
	 * @param standardDeviation
	 *                              Standard deviation of the distribution.
	 * @return A random byte with a normal distribution.
	 */
	public default byte generateNormalByte(double mean, double standardDeviation) {
		return (byte) generateNormalShort(mean, standardDeviation);
	}

	/**
	 * Get a random byte with a lognormal distribution.
	 * 
	 * @param logMean
	 *                                 Mean of the natural logarithm of the
	 *                                 distribution.
	 * @param logStandardDeviation
	 *                                 Standard deviation of the natural logarithm
	 *                                 of the distribution.
	 * @return A random byte with a lognormal distribution.
	 */
	public default byte generateLognormalByte(double logMean, double logStandardDeviation) {
		return (byte) generateLognormalShort(logMean, logStandardDeviation);
	}

	/**
	 * Get a random byte with a triangular distribution.
	 * 
	 * @param min
	 *                 Minimum value of the number, inclusive.
	 * @param max
	 *                 Maximum value of the number, exclusive.
	 * @param mode
	 *                 Most common value of the number.
	 * @return A random byte with a triangular distribution.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than or equal to
	 *                                      mode and mode is not smaller than max.
	 */
	public default byte generateTriangularByte(byte min, byte max, byte mode) {
		return (byte) generateTriangularShort(min, max, mode);
	}

	/**
	 * Get a random byte with a Pareto distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random byte with a Pareto distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default byte generateParetoByte(double shape, double scale) {
		return (byte) generateParetoShort(shape, scale);
	}

	/**
	 * Get a random byte with a Weibull distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random byte with a Weibull distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default byte generateWeibullByte(double shape, double scale) {
		return (byte) generateWeibullShort(shape, scale);
	}

	/**
	 * Get a random byte with an exponential distribution.
	 * 
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random byte with an exponential distribution.
	 * @throws IllegalArgumentException
	 *                                      If scale is not larger than 0.
	 */
	public default byte generateExponentialByte(double scale) {
		return (byte) generateExponentialShort(scale);
	}

	/**
	 * Get a random byte with a gamma distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random byte with a gamma distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default byte generateGammaByte(double shape, double scale) {
		return (byte) generateGammaShort(shape, scale);
	}

	/**
	 * Get a random byte with a beta distribution.
	 * 
	 * @param shapeAlpha
	 *                       Alpha shape parameter of the distribution.
	 * @param shapeBeta
	 *                       Beta shape parameter of the distribution.
	 * @return A random byte with a beta distribution.
	 * @throws IllegalArgumentException
	 *                                      If shapeAlpha is not larger than 0 or
	 *                                      shapeBeta is not larger than 0.
	 */
	public default byte generateBetaByte(double shapeAlpha, double shapeBeta) {
		return (byte) generateBetaShort(shapeAlpha, shapeBeta);
	}

	/**
	 * Get a random byte with a poisson distribution.
	 * 
	 * @param rate
	 *                 Rate parameter of the distribution.
	 * @return A random byte with a poisson distribution.
	 * @throws IllegalArgumentException
	 *                                      If rate is not larger than 0.
	 */
	public default byte generatePoissonByte(double rate) {
		return (byte) generatePoissonShort(rate);
	}

	// -----------------------------------------------------------------------------
	// Short integer methods

	/**
	 * @see RandomGenerator#generateUniformShort()
	 */
	public default short generateShort() {
		return generateUniformShort();
	}

	/**
	 * @see RandomGenerator#generateUniformShort(short)
	 */
	public default short generateShort(short max) {
		return generateUniformShort(max);
	}

	/**
	 * @see RandomGenerator#generateUniformShort(short, short)
	 */
	public default short generateShort(short min, short max) {
		return generateUniformShort(min, max);
	}

	/**
	 * Get a random short integer with a uniform distribution.
	 * 
	 * @return A random short integer with a uniform distribution.
	 */
	public default short generateUniformShort() {
		return (short) generateUniformInteger();
	}

	/**
	 * Get a random short integer with a uniform distribution between 0 and a
	 * number.
	 * 
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random short integer with a uniform distribution between 0 and a
	 *         number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default short generateUniformShort(short max) {
		return (short) generateUniformInteger(max);
	}

	/**
	 * Get a random short integer with a uniform distribution between two numbers.
	 * 
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random short integer with a uniform distribution between two
	 *         numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default short generateUniformShort(short min, short max) {
		return (short) generateUniformInteger(min, max);
	}

	/**
	 * Get a random short integer with a Bates distribution between two numbers.
	 * 
	 * @param min
	 *                   Minimum value of the number, inclusive.
	 * @param max
	 *                   Maximum value of the number, exclusive.
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random short integer with a Bates distribution.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default short generateBatesShort(short min, short max, int number) {
		return (short) generateBatesInteger(min, max, number);
	}

	/**
	 * Get a random short integer with a normal distribution.
	 * 
	 * @param mean
	 *                              Mean of the distribution.
	 * @param standardDeviation
	 *                              Standard deviation of the distribution.
	 * @return A random short integer with a normal distribution.
	 */
	public default short generateNormalShort(double mean, double standardDeviation) {
		return (short) generateNormalInteger(mean, standardDeviation);
	}

	/**
	 * Get a random short integer with a lognormal distribution.
	 * 
	 * @param logMean
	 *                                 Mean of the natural logarithm of the
	 *                                 distribution.
	 * @param logStandardDeviation
	 *                                 Standard deviation of the natural logarithm
	 *                                 of the distribution.
	 * @return A random short integer with a lognormal distribution.
	 */
	public default short generateLognormalShort(double logMean, double logStandardDeviation) {
		return (short) generateLognormalInteger(logMean, logStandardDeviation);
	}

	/**
	 * Get a random short integer with a triangular distribution.
	 * 
	 * @param min
	 *                 Minimum value of the number, inclusive.
	 * @param max
	 *                 Maximum value of the number, exclusive.
	 * @param mode
	 *                 Most common value of the number.
	 * @return A random short integer with a triangular distribution.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than or equal to
	 *                                      mode and mode is not smaller than max.
	 */
	public default short generateTriangularShort(short min, short max, short mode) {
		return (short) generateTriangularInteger(min, max, mode);
	}

	/**
	 * Get a random short integer with a Pareto distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random short integer with a Pareto distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default short generateParetoShort(double shape, double scale) {
		return (short) generateParetoInteger(shape, scale);
	}

	/**
	 * Get a random short integer with a Weibull distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random short integer with a Weibull distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default short generateWeibullShort(double shape, double scale) {
		return (short) generateWeibullInteger(shape, scale);
	}

	/**
	 * Get a random short integer with an exponential distribution.
	 * 
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random short integer with an exponential distribution.
	 * @throws IllegalArgumentException
	 *                                      If scale is not larger than 0.
	 */
	public default short generateExponentialShort(double scale) {
		return (short) generateExponentialInteger(scale);
	}

	/**
	 * Get a random short integer with a gamma distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random short integer with a gamma distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default short generateGammaShort(double shape, double scale) {
		return (short) generateGammaInteger(shape, scale);
	}

	/**
	 * Get a random short integer with a beta distribution.
	 * 
	 * @param shapeAlpha
	 *                       Alpha shape parameter of the distribution.
	 * @param shapeBeta
	 *                       Beta shape parameter of the distribution.
	 * @return A random short integer with a beta distribution.
	 * @throws IllegalArgumentException
	 *                                      If shapeAlpha is not larger than 0 or
	 *                                      shapeBeta is not larger than 0.
	 */
	public default short generateBetaShort(double shapeAlpha, double shapeBeta) {
		return (short) generateBetaInteger(shapeAlpha, shapeBeta);
	}

	/**
	 * Get a random short integer with a poisson distribution.
	 * 
	 * @param rate
	 *                 Rate parameter of the distribution.
	 * @return A random short integer with a poisson distribution.
	 * @throws IllegalArgumentException
	 *                                      If rate is not larger than 0.
	 */
	public default short generatePoissonShort(double rate) {
		return (short) generatePoissonInteger(rate);
	}

	// -----------------------------------------------------------------------------
	// Integer methods

	/**
	 * @see RandomGenerator#generateUniformInteger()
	 */
	public default int generateInteger() {
		return generateUniformInteger();
	}

	/**
	 * @see RandomGenerator#generateUniformInteger(int)
	 */
	public default int generateInteger(int max) {
		return generateUniformInteger(max);
	}

	/**
	 * @see RandomGenerator#generateUniformInteger(int, int)
	 */
	public default int generateInteger(int min, int max) {
		return generateUniformInteger(min, max);
	}

	/**
	 * Get a random integer with a uniform distribution.
	 * 
	 * @return A random integer with a uniform distribution.
	 */
	public default int generateUniformInteger() {
		return (int) generateUniformLong();
	}

	/**
	 * Get a random integer with a uniform distribution between 0 and a number.
	 * 
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random integer with a uniform distribution between 0 and a number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default int generateUniformInteger(int max) {
		return (int) generateUniformLong(max);
	}

	/**
	 * Get a random integer with a uniform distribution between two numbers.
	 * 
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random integer with a uniform distribution between two numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default int generateUniformInteger(int min, int max) {
		return (int) generateUniformLong(min, max);
	}

	/**
	 * Get a random integer with a Bates distribution between two numbers.
	 * 
	 * @param min
	 *                   Minimum value of the number, inclusive.
	 * @param max
	 *                   Maximum value of the number, exclusive.
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random integer with a Bates distribution.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default int generateBatesInteger(int min, int max, int number) {
		return (int) generateBatesLong(min, max, number);
	}

	/**
	 * Get a random integer with a normal distribution.
	 * 
	 * @param mean
	 *                              Mean of the distribution.
	 * @param standardDeviation
	 *                              Standard deviation of the distribution.
	 * @return A random integer with a normal distribution.
	 */
	public default int generateNormalInteger(double mean, double standardDeviation) {
		return (int) generateNormalLong(mean, standardDeviation);
	}

	/**
	 * Get a random integer with a lognormal distribution.
	 * 
	 * @param logMean
	 *                                 Mean of the natural logarithm of the
	 *                                 distribution.
	 * @param logStandardDeviation
	 *                                 Standard deviation of the natural logarithm
	 *                                 of the distribution.
	 * @return A random integer with a lognormal distribution.
	 */
	public default int generateLognormalInteger(double logMean, double logStandardDeviation) {
		return (int) generateLognormalLong(logMean, logStandardDeviation);
	}

	/**
	 * Get a random integer with a triangular distribution.
	 * 
	 * @param min
	 *                 Minimum value of the number, inclusive.
	 * @param max
	 *                 Maximum value of the number, exclusive.
	 * @param mode
	 *                 Most common value of the number.
	 * @return A random integer with a triangular distribution.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than or equal to
	 *                                      mode and mode is not smaller than max.
	 */
	public default int generateTriangularInteger(int min, int max, int mode) {
		return (int) generateTriangularLong(min, max, mode);
	}

	/**
	 * Get a random integer with a Pareto distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random integer with a Pareto distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default int generateParetoInteger(double shape, double scale) {
		return (int) generateParetoLong(shape, scale);
	}

	/**
	 * Get a random integer with a Weibull distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random integer with a Weibull distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default int generateWeibullInteger(double shape, double scale) {
		return (int) generateWeibullLong(shape, scale);
	}

	/**
	 * Get a random integer with an exponential distribution.
	 * 
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random integer with an exponential distribution.
	 * @throws IllegalArgumentException
	 *                                      If scale is not larger than 0.
	 */
	public default int generateExponentialInteger(double scale) {
		return (int) generateExponentialLong(scale);
	}

	/**
	 * Get a random integer with a gamma distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random integer with a gamma distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default int generateGammaInteger(double shape, double scale) {
		return (int) generateGammaLong(shape, scale);
	}

	/**
	 * Get a random integer with a beta distribution.
	 * 
	 * @param shapeAlpha
	 *                       Alpha shape parameter of the distribution.
	 * @param shapeBeta
	 *                       Beta shape parameter of the distribution.
	 * @return A random integer with a beta distribution.
	 * @throws IllegalArgumentException
	 *                                      If shapeAlpha is not larger than 0 or
	 *                                      shapeBeta is not larger than 0.
	 */
	public default int generateBetaInteger(double shapeAlpha, double shapeBeta) {
		return (int) generateBetaLong(shapeAlpha, shapeBeta);
	}

	/**
	 * Get a random integer with a poisson distribution.
	 * 
	 * @param rate
	 *                 Rate parameter of the distribution.
	 * @return A random integer with a poisson distribution.
	 * @throws IllegalArgumentException
	 *                                      If rate is not larger than 0.
	 */
	public default int generatePoissonInteger(double rate) {
		return (int) generatePoissonLong(rate);
	}

	// -----------------------------------------------------------------------------
	// Long integer methods

	/**
	 * @see RandomGenerator#generateUniformLong()
	 */
	public default long generateLong() {
		return generateUniformLong();
	}

	/**
	 * @see RandomGenerator#generateUniformLong(long)
	 */
	public default long generateLong(long max) {
		return generateUniformLong(max);
	}

	/**
	 * @see RandomGenerator#generateUniformLong(long, long)
	 */
	public default long generateLong(long min, long max) {
		return generateUniformLong(min, max);
	}

	/**
	 * Get a random long integer with a uniform distribution.
	 * 
	 * @return A random long integer with a uniform distribution.
	 */
	public long generateUniformLong();

	/**
	 * Get a random long integer with a uniform distribution between 0 and a number.
	 * 
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random long integer with a uniform distribution between 0 and a
	 *         number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default long generateUniformLong(long max) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		long result;
		// Distance from the highest multiple of max to the absolute maximum value of
		// long
		long moduloBias = (Long.MIN_VALUE - max) % max;
		// Maximum value without modulo bias, inclusive
		long unbiasedMaximum = Long.MAX_VALUE - moduloBias;
		do {
			result = generateUniformLong() & 0x7F_FF_FF_FF_FF_FF_FF_FFL;
		} while (result > unbiasedMaximum);
		return result % max;
	}

	/**
	 * Get a random long integer with a uniform distribution between two numbers.
	 * 
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random long integer with a uniform distribution between two
	 *         numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default long generateUniformLong(long min, long max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		return generateUniformLong(max - min) + min;
	}

	/**
	 * Get a random long integer with a Bates distribution between two numbers.
	 * 
	 * @param min
	 *                   Minimum value of the number, inclusive.
	 * @param max
	 *                   Maximum value of the number, exclusive.
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random long integer with a Bates distribution.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default long generateBatesLong(long min, long max, int number) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		int res = 0;
		long range = max - min;
		for (int i = 0; i < number; ++i) {
			res += generateUniformLong(range);
		}
		/*
		 * Improve the distribution of random numbers. Up until here, res is in [0,
		 * range * number - number]. By adding another random number in [0, number - 1],
		 * res will be in [0, range * number - 1]. After dividing res by number, res
		 * will be in [0, range - 1].
		 */
		res += generateUniformLong(number);
		res /= number;
		res += min;
		return res;
	}

	/**
	 * Get a random long integer with a normal distribution.
	 * 
	 * @param mean
	 *                              Mean of the distribution.
	 * @param standardDeviation
	 *                              Standard deviation of the distribution.
	 * @return A random long integer with a normal distribution.
	 */
	public default long generateNormalLong(double mean, double standardDeviation) {
		return Math.round(generateNormalDouble(mean, standardDeviation));
	}

	/**
	 * Get a random long integer with a lognormal distribution.
	 * 
	 * @param logMean
	 *                                 Mean of the natural logarithm of the
	 *                                 distribution.
	 * @param logStandardDeviation
	 *                                 Standard deviation of the natural logarithm
	 *                                 of the distribution.
	 * @return A random long integer with a lognormal distribution.
	 */
	public default long generateLognormalLong(double logMean, double logStandardDeviation) {
		return (long) generateLognormalDouble(logMean, logStandardDeviation);
	}

	/**
	 * Get a random long integer with a triangular distribution.
	 * 
	 * @param min
	 *                 Minimum value of the number, inclusive.
	 * @param max
	 *                 Maximum value of the number, exclusive.
	 * @param mode
	 *                 Most common value of the number.
	 * @return A random long integer with a triangular distribution.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than or equal to
	 *                                      mode and mode is not smaller than max.
	 */
	public default long generateTriangularLong(long min, long max, long mode) {
		double doubleMin = (double) min;
		double doubleMax = (double) max;
		double doubleMode = (double) mode;
		// Improve the distribution when switching between long and double
		doubleMode = doubleMode * (doubleMax / (doubleMax - 1));
		return (long) (generateTriangularDouble(doubleMin, doubleMax, doubleMode));
	}

	/**
	 * Get a random long integer with a Pareto distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random long integer with a Pareto distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default long generateParetoLong(double shape, double scale) {
		return (long) generateParetoDouble(shape, scale);
	}

	/**
	 * Get a random long integer with a Weibull distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random long integer with a Weibull distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default long generateWeibullLong(double shape, double scale) {
		return (long) generateWeibullDouble(shape, scale);
	}

	/**
	 * Get a random long integer with an exponential distribution.
	 * 
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random long integer with an exponential distribution.
	 * @throws IllegalArgumentException
	 *                                      If scale is not larger than 0.
	 */
	public default long generateExponentialLong(double scale) {
		return (long) generateExponentialDouble(scale);
	}

	/**
	 * Get a random long integer with a gamma distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random long integer with a gamma distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default long generateGammaLong(double shape, double scale) {
		return (long) generateGammaDouble(shape, scale);
	}

	/**
	 * Get a random long integer with a beta distribution.
	 * 
	 * @param shapeAlpha
	 *                       Alpha shape parameter of the distribution.
	 * @param shapeBeta
	 *                       Beta shape parameter of the distribution.
	 * @return A random long integer with a beta distribution.
	 * @throws IllegalArgumentException
	 *                                      If shapeAlpha is not larger than 0 or
	 *                                      shapeBeta is not larger than 0.
	 */
	public default long generateBetaLong(double shapeAlpha, double shapeBeta) {
		return (long) generateBetaDouble(shapeAlpha, shapeBeta);
	}

	/**
	 * Get a random long integer with a poisson distribution.
	 * 
	 * @param rate
	 *                 Rate parameter of the distribution.
	 * @return A random long integer with a poisson distribution.
	 * @throws IllegalArgumentException
	 *                                      If rate is not larger than 0.
	 */
	public default long generatePoissonLong(double rate) {
		if (0 >= rate) {
			throw new IllegalArgumentException();
		}
		double l = rate;
		long k = -1;
		double p = 1.0;
		do {
			k = k + 1;
			double u;
			do {
				u = generateUniformDouble();
			} while (u == 0.0 || u == 1.0);
			p = p * u;
			while (p < 1.0 && l > 0.0) {
				if (l > 500.0) {
					p = p * Math.pow(Math.E, 500.0);
					l = l - 500.0;
				} else {
					p = p * Math.pow(Math.E, l);
					l = 0.0;
				}
			}
		} while (p > 1.0);
		return k;
	}

	// -----------------------------------------------------------------------------
	// Floating point methods

	/**
	 * @see RandomGenerator#generateUniformFloat()
	 */
	public default float generateFloat() {
		return generateUniformFloat();
	}

	/**
	 * @see RandomGenerator#generateUniformFloat(float)
	 */
	public default float generateFloat(float max) {
		return generateUniformFloat(max);
	}

	/**
	 * @see RandomGenerator#generateUniformFloat(float, float)
	 */
	public default float generateFloat(float min, float max) {
		return generateUniformFloat(min, max);
	}

	/**
	 * Get a random floating point with a uniform distribution in the range [0.0,
	 * 1.0].
	 * 
	 * @return A random floating point with a uniform distribution in the range
	 *         [0.0, 1.0].
	 */
	public default float generateUniformFloat() {
		return generateUniformInteger((1 << 24) + 1) / (float) (1L << 24);
	}

	/**
	 * Get a random floating point with a uniform distribution between 0 and a
	 * number.
	 * 
	 * @param max
	 *                Maximum value of the number, inclusive.
	 * @return A random floating point with a uniform distribution between 0 and a
	 *         number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default float generateUniformFloat(float max) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		return generateUniformFloat() * max;
	}

	/**
	 * Get a random floating point with a uniform distribution between two numbers.
	 * 
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, inclusive.
	 * @return A random floating point with a uniform distribution between two
	 *         numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default float generateUniformFloat(float min, float max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		return min + generateUniformFloat(max - min);
	}

	/**
	 * Get a random floating point with a Bates distribution in the range [0.0,
	 * 1.0].
	 * 
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random floating point with a Bates distribution in the range [0.0,
	 *         1.0].
	 */
	public default float generateBatesFloat(int number) {
		float res = 0;
		for (int i = 0; i < number; ++i) {
			res += generateUniformFloat();
		}
		res /= number;
		return res;
	}

	/**
	 * Get a random floating point with a Bates distribution between 0 and a number.
	 * 
	 * @param max
	 *                   Maximum value of the number, inclusive.
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random floating point with a Bates distribution between 0 and a
	 *         number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default float generateBatesFloat(float max, int number) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		float res = 0;
		for (int i = 0; i < number; ++i) {
			res += generateUniformFloat(max);
		}
		res /= number;
		return res;
	}

	/**
	 * Get a random floating point with a Bates distribution between two numbers.
	 * 
	 * @param min
	 *                   Minimum value of the number, inclusive.
	 * @param max
	 *                   Maximum value of the number, inclusive.
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random floating point with a Bates distribution between two
	 *         numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default float generateBatesFloat(float min, float max, int number) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		float res = 0;
		for (int i = 0; i < number; ++i) {
			res += generateUniformFloat(min, max);
		}
		res /= number;
		return res;
	}

	/**
	 * Get a random floating point with a normal distribution.
	 * 
	 * @param mean
	 *                              Mean of the distribution.
	 * @param standardDeviation
	 *                              Standard deviation of the distribution.
	 * @return A random floating point with a normal distribution.
	 */
	public default float generateNormalFloat(double mean, double standardDeviation) {
		return (float) generateNormalDouble(mean, standardDeviation);
	}

	/**
	 * Get a random floating point with a lognormal distribution.
	 * 
	 * @param logMean
	 *                                 Mean of the natural logarithm of the
	 *                                 distribution.
	 * @param logStandardDeviation
	 *                                 Standard deviation of the natural logarithm
	 *                                 of the distribution.
	 * @return A random floating point with a lognormal distribution.
	 */
	public default float generateLognormalFloat(double logMean, double logStandardDeviation) {
		return (float) generateLognormalDouble(logMean, logStandardDeviation);
	}

	/**
	 * Get a random floating point with a triangular distribution.
	 * 
	 * @param min
	 *                 Minimum value of the number, inclusive.
	 * @param max
	 *                 Maximum value of the number, inclusive.
	 * @param mode
	 *                 Most common value of the number.
	 * @return A random floating point with a triangular distribution.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than or equal to
	 *                                      mode and mode is not smaller than max.
	 */
	public default float generateTriangularFloat(float min, float max, float mode) {
		if (!(min < mode && mode < max)) {
			throw new IllegalArgumentException();
		}
		float uniformFloat = generateUniformFloat(min, max);
		if (uniformFloat < mode) {
			return (float) (min + Math.sqrt(uniformFloat * (mode - min)));
		} else {
			return max - (float) Math.sqrt((max - uniformFloat) * (max - mode));
		}
	}

	/**
	 * Get a random floating point with a Pareto distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random floating point with a Pareto distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default float generateParetoFloat(double shape, double scale) {
		return (float) generateParetoDouble(shape, scale);
	}

	/**
	 * Get a random floating point with a Weibull distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random floating point with a Weibull distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default float generateWeibullFloat(double shape, double scale) {
		return (float) generateWeibullDouble(shape, scale);
	}

	/**
	 * Get a random floating point with an exponential distribution.
	 * 
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random floating point with an exponential distribution.
	 * @throws IllegalArgumentException
	 *                                      If scale is not larger than 0.
	 */
	public default float generateExponentialFloat(double scale) {
		return (float) generateExponentialDouble(scale);
	}

	/**
	 * Get a random floating point with a gamma distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random floating point with a gamma distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default float generateGammaFloat(double shape, double scale) {
		return (float) generateGammaDouble(shape, scale);
	}

	/**
	 * Get a random floating point with a beta distribution.
	 * 
	 * @param shapeAlpha
	 *                       Alpha shape parameter of the distribution.
	 * @param shapeBeta
	 *                       Beta shape parameter of the distribution.
	 * @return A random floating point with a beta distribution.
	 * @throws IllegalArgumentException
	 *                                      If shapeAlpha is not larger than 0 or
	 *                                      shapeBeta is not larger than 0.
	 */
	public default float generateBetaFloat(double shapeAlpha, double shapeBeta) {
		return (float) generateBetaDouble(shapeAlpha, shapeBeta);
	}

	// -----------------------------------------------------------------------------
	// Double floating point methods

	/**
	 * @see RandomGenerator#generateUniformDouble()
	 */
	public default double generateDouble() {
		return generateUniformDouble();
	}

	/**
	 * @see RandomGenerator#generateUniformDouble(double)
	 */
	public default double generateDouble(float max) {
		return generateUniformDouble(max);
	}

	/**
	 * @see RandomGenerator#generateUniformDouble(double, double)
	 */
	public default double generateDouble(double min, double max) {
		return generateUniformDouble(min, max);
	}

	/**
	 * Get a random double floating point with a uniform distribution in the range
	 * [0.0, 1.0].
	 * 
	 * @return A random double floating point with a uniform distribution in the
	 *         range [0.0, 1.0].
	 */
	public default double generateUniformDouble() {
		return generateUniformLong((1L << 53) + 1) / (double) (1L << 53);
	}

	/**
	 * Get a random double floating point with a uniform distribution between 0 and
	 * a number.
	 * 
	 * @param max
	 *                Maximum value of the number, inclusive.
	 * @return A random double floating point with a uniform distribution between 0
	 *         and a number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default double generateUniformDouble(double max) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		return generateUniformDouble() * max;
	}

	/**
	 * Get a random double floating point with a uniform distribution between two
	 * numbers.
	 * 
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, inclusive.
	 * @return A random double floating point with a uniform distribution between
	 *         two numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default double generateUniformDouble(double min, double max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		return min + generateUniformDouble(max - min);
	}

	/**
	 * Get a random double floating point with a Bates distribution in the range
	 * [0.0, 1.0].
	 * 
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random double floating point with a Bates distribution in the range
	 *         [0.0, 1.0].
	 */
	public default double generateBatesDouble(int number) {
		double res = 0;
		for (int i = 0; i < number; ++i) {
			res += generateUniformDouble();
		}
		res /= number;
		return res;
	}

	/**
	 * Get a random double floating point with a Bates distribution between 0 and a
	 * number.
	 * 
	 * @param max
	 *                   Maximum value of the number, inclusive.
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random double floating point with a Bates distribution between 0
	 *         and a number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default double generateBatesDouble(double max, int number) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		double res = 0;
		for (int i = 0; i < number; ++i) {
			res += generateUniformDouble(max);
		}
		res /= number;
		return res;
	}

	/**
	 * Get a random double floating point with a Bates distribution between two
	 * numbers.
	 * 
	 * @param min
	 *                   Minimum value of the number, inclusive.
	 * @param max
	 *                   Maximum value of the number, inclusive.
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random double floating point with a Bates distribution between two
	 *         numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default double generateBatesDouble(double min, double max, int number) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		double res = 0;
		for (int i = 0; i < number; ++i) {
			res += generateUniformDouble(min, max);
		}
		res /= number;
		return res;
	}

	/**
	 * Get a random double floating point with a normal distribution.
	 * 
	 * @param mean
	 *                              Mean of the distribution.
	 * @param standardDeviation
	 *                              Standard deviation of the distribution.
	 * @return A random double floating point with a normal distribution.
	 */
	public default double generateNormalDouble(double mean, double standardDeviation) {
		double random1;
		double random2;
		double squareSum;
		do {
			random1 = generateUniformDouble(-1, 1);
			random2 = generateUniformDouble(-1, 1);
			squareSum = random1 * random1 + random2 * random2;
		} while (squareSum >= 1 || squareSum == 0);
		return standardDeviation * random1 * Math.sqrt(-2 * Math.log(squareSum) / squareSum) + mean;
	}

	/**
	 * Get a random double floating point with a lognormal distribution.
	 * 
	 * @param logMean
	 *                                 Mean of the natural logarithm of the
	 *                                 distribution.
	 * @param logStandardDeviation
	 *                                 Standard deviation of the natural logarithm
	 *                                 of the distribution.
	 * @return A random double floating point with a lognormal distribution.
	 */
	public default double generateLognormalDouble(double logMean, double logStandardDeviation) {
		return Math.exp(generateNormalDouble(logMean, logStandardDeviation));
	}

	/**
	 * Get a random double floating point with a triangular distribution.
	 * 
	 * @param min
	 *                 Minimum value of the number, inclusive.
	 * @param max
	 *                 Maximum value of the number, inclusive.
	 * @param mode
	 *                 Most common value of the number.
	 * @return A random double floating point with a triangular distribution.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than or equal to
	 *                                      mode and mode is not smaller than max.
	 */
	public default double generateTriangularDouble(double min, double max, double mode) {
		if (!(min <= mode && mode <= max)) {
			throw new IllegalArgumentException();
		}
		double uniformDouble = generateUniformDouble(min, max);
		if (uniformDouble < mode) {
			return (min + Math.sqrt(uniformDouble * (mode - min)));
		} else {
			return max - Math.sqrt((max - uniformDouble) * (max - mode));
		}
	}

	/**
	 * Get a random double floating point with a Pareto distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random double floating point with a Pareto distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default double generateParetoDouble(double shape, double scale) {
		if (0 >= shape || 0 >= scale) {
			throw new IllegalArgumentException();
		}
		double random;
		do {
			random = generateUniformDouble();
		} while (random == 0);
		return scale / Math.pow(random, 1 / shape);
	}

	/**
	 * Get a random double floating point with a Weibull distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random double floating point with a Weibull distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default double generateWeibullDouble(double shape, double scale) {
		if (0 >= shape || 0 >= scale) {
			throw new IllegalArgumentException();
		}
		double random;
		do {
			random = generateUniformDouble();
		} while (random == 0);
		return scale * Math.pow(-Math.log(random), 1 / shape);
	}

	/**
	 * Get a random double floating point with an exponential distribution.
	 * 
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random double floating point with an exponential distribution.
	 * @throws IllegalArgumentException
	 *                                      If scale is not larger than 0.
	 */
	public default double generateExponentialDouble(double scale) {
		double uniform;
		do {
			uniform = generateUniformDouble();
		} while (uniform == 0);
		return Math.log(uniform) * scale;
	}

	/**
	 * Get a random double floating point with a gamma distribution.
	 * 
	 * @param shape
	 *                  Shape parameter of the distribution.
	 * @param scale
	 *                  Scale parameter of the distribution.
	 * @return A random double floating point with a gamma distribution.
	 * @throws IllegalArgumentException
	 *                                      If shape is not larger than 0 or scale
	 *                                      is not larger than 0.
	 */
	public default double generateGammaDouble(double shape, double scale) {
		if (0 >= shape || 0 >= scale) {
			throw new IllegalArgumentException();
		}
		if (shape > 1) {
			double d = shape - 1.0d / 3.0d;
			double c = 1 / Math.sqrt(9 * d);
			double v;
			double dv;
			double uniform;
			double normal;
			do {
				normal = generateNormalDouble(0, 1);
				v = (1 + c * normal);
				v = v * v * v;
				dv = d * v;
				// Generate uniform in (0, 1]
				do {
					uniform = generateUniformDouble();
				} while (uniform == 0);
			} while (v <= 0 || Math.log(uniform) >= 0.5 * normal * normal + d - dv + d * Math.log(v));
			return dv * scale;
		} else if (shape == 1) {
			return generateExponentialDouble(scale);
		} else {
			double uniform;
			do {
				uniform = generateUniformDouble();
			} while (uniform == 0);
			return generateGammaDouble(shape + 1, scale) * Math.pow(uniform, 1 / shape);
		}
	}

	/**
	 * Get a random double floating point with a beta distribution.
	 * 
	 * @param shapeAlpha
	 *                       Alpha shape parameter of the distribution.
	 * @param shapeBeta
	 *                       Beta shape parameter of the distribution.
	 * @return A random double floating point with a beta distribution.
	 * @throws IllegalArgumentException
	 *                                      If shapeAlpha is not larger than 0 or
	 *                                      shapeBeta is not larger than 0.
	 */
	public default double generateBetaDouble(double shapeAlpha, double shapeBeta) {
		double gamma = generateGammaDouble(shapeAlpha, 1.0);
		if (gamma == 0) {
			return 0;
		} else {
			return gamma / (gamma + generateGammaDouble(shapeBeta, 1.0));
		}
	}

	// -----------------------------------------------------------------------------
	// Character methods

	/**
	 * Gets a random character from the given String.
	 * 
	 * @param alphabet
	 *                     A string of characters this method will pick from.
	 * @return A random character from the given String.
	 */
	public default char generateCharacter(String alphabet) {
		return alphabet.charAt(generateUniformInteger(alphabet.length()));
	}

	// -----------------------------------------------------------------------------
	// String methods

	/**
	 * Gets a random String from characters picked from the given String.
	 * 
	 * @param alphabet
	 *                     A string of characters this method will pick from.
	 * @return A random String from characters picked from the given String.
	 */
	public default String generateString(String alphabet, int lenght) {
		StringBuilder stringBuilder = new StringBuilder(lenght);
		for (int i = 0; i < lenght; ++i) {
			stringBuilder.append(generateCharacter(alphabet));
		}
		return stringBuilder.toString();
	}

	// -----------------------------------------------------------------------------
	// Array methods

	/**
	 * Get a random element from an array.
	 * 
	 * @param elements
	 *                     An array of elements.
	 * @return A random element from an array.
	 */
	public default <T> T pick(T[] elements) {
		int index = generateUniformInteger(elements.length);
		return elements[index];
	}

	/**
	 * Shuffle the elements in an array randomly.
	 * 
	 * @param elements
	 *                     An array of elements.
	 * @return A shuffled array.
	 */
	public default <T> T[] shuffle(T[] elements) {
		for (int i = 0; i < elements.length; ++i) {
			int j = generateUniformInteger(elements.length);
			T temporary = elements[i];
			elements[i] = elements[j];
			elements[j] = temporary;
		}
		return elements;
	}

	// -----------------------------------------------------------------------------
	// Collection methods

	/**
	 * Get a random element from a list.
	 * 
	 * @param elements
	 *                     A list of elements.
	 * @return A random element from a list.
	 */
	public default <T> T pick(List<T> elements) {
		int index = generateUniformInteger(elements.size());
		return elements.get(index);
	}

	/**
	 * Shuffle the elements in a list randomly.
	 * 
	 * @param elements
	 *                     A list of elements.
	 * @return A shuffled list.
	 */
	public default <T> List<T> shuffle(List<T> elements) {
		for (int i = 0; i < elements.size(); ++i) {
			int j = generateUniformInteger(elements.size());
			T temporary = elements.get(i);
			elements.set(i, elements.get(j));
			elements.set(j, temporary);
		}
		return elements;
	}

	/**
	 * Get a random element from a set.
	 * 
	 * @param elements
	 *                     A set of elements.
	 * @return A random element from a set.
	 */
	public default <T> T pick(Set<T> elements) {
		int index = generateUniformInteger(elements.size());
		Iterator<T> iterator = elements.iterator();
		while (index > 0) {
			iterator.next();
			--index;
		}
		return iterator.next();
	}

}
