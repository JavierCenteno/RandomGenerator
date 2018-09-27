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
				return source.getRandomIntegerBits(bits);
			}

			@Override
			public void nextBytes(byte[] bytes) {
				for (int i = 0; i < bytes.length; ++i) {
					bytes[i] = source.getRandomUniformByte();
				}
			}

			@Override
			public int nextInt() {
				return source.getRandomUniformInteger();
			}

			@Override
			public int nextInt(int n) {
				return source.getRandomUniformInteger(n);
			}

			@Override
			public long nextLong() {
				return source.getRandomUniformLong();
			}

			@Override
			public boolean nextBoolean() {
				return source.getRandomBoolean();
			}

			@Override
			public float nextFloat() {
				float result;
				do {
					result = source.getRandomUniformFloat();
				} while (result == 1.0f);
				return result;
			}

			@Override
			public double nextDouble() {
				double result;
				do {
					result = source.getRandomUniformDouble();
				} while (result == 1.0d);
				return result;
			}

			@Override
			public double nextGaussian() {
				return source.getRandomNormalDouble(0.0d, 1.0d);
			}

		}
		return new RandomWrapper(this);
	}

	// -----------------------------------------------------------------------------
	// Seed methods

	/**
	 * Resets this generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 */
	public void setSeed(byte[] seed);

	// -----------------------------------------------------------------------------
	// State methods

	/**
	 * Get the current state of this generator.
	 * 
	 * @return The current state of this generator.
	 */
	public byte[] getState();

	/**
	 * Set the state of this generator.
	 * 
	 * @param state
	 *                  A new state for this generator.
	 * @throws IllegalArgumentException
	 *                                      If the state is too short.
	 */
	public void setState(byte[] state);

	// -----------------------------------------------------------------------------
	// Boolean methods

	/**
	 * Get a random boolean.
	 * 
	 * @return A random boolean.
	 */
	public default boolean getRandomBoolean() {
		return getRandomUniformByte() < 0;
	}

	/**
	 * Get a random boolean.
	 * 
	 * @param probability
	 *                        Probability that this will return true.
	 * @return A random boolean.
	 */
	public default boolean getRandomBoolean(float probability) {
		return getRandomUniformFloat() < probability;
	}

	/**
	 * Get a random boolean.
	 * 
	 * @param probability
	 *                        Probability that this will return true.
	 * @return A random boolean.
	 */
	public default boolean getRandomBoolean(double probability) {
		return getRandomUniformDouble() < probability;
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
	public default byte getRandomByteBits(int bits) {
		int shift = Byte.SIZE - bits;
		if (shift < 0 || bits < 0) {
			throw new IllegalArgumentException();
		}
		return (byte) (getRandomUniformByte() >>> shift);
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
	public default short getRandomShortBits(int bits) {
		int shift = Short.SIZE - bits;
		if (shift < 0 || bits < 0) {
			throw new IllegalArgumentException();
		}
		return (short) (getRandomUniformShort() >>> shift);
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
	public default int getRandomIntegerBits(int bits) {
		int shift = Integer.SIZE - bits;
		if (shift < 0 || bits < 0) {
			throw new IllegalArgumentException();
		}
		return getRandomUniformInteger() >>> shift;
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
	public default long getRandomLongBits(int bits) {
		int shift = Long.SIZE - bits;
		if (shift < 0 || bits < 0) {
			throw new IllegalArgumentException();
		}
		return getRandomUniformLong() >>> shift;
	}

	// -----------------------------------------------------------------------------
	// Byte methods

	/**
	 * Get a random byte.
	 * 
	 * @return A random byte.
	 */
	public default byte getRandomUniformByte() {
		return (byte) getRandomUniformShort();
	}

	/**
	 * Get a random byte between 0 and a number.
	 * 
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random byte between 0 and a number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default byte getRandomUniformByte(byte max) {
		return (byte) getRandomUniformShort(max);
	}

	/**
	 * Get a random byte between two numbers.
	 * 
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random byte between two numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default byte getRandomUniformByte(byte min, byte max) {
		return (byte) getRandomUniformShort(min, max);
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
	public default byte getRandomBatesByte(byte min, byte max, int number) {
		return (byte) getRandomBatesShort(min, max, number);
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
	public default byte getRandomNormalByte(double mean, double standardDeviation) {
		return (byte) getRandomNormalShort(mean, standardDeviation);
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
	public default byte getRandomLognormalByte(double logMean, double logStandardDeviation) {
		return (byte) getRandomLognormalShort(logMean, logStandardDeviation);
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
	public default byte getRandomTriangularByte(byte min, byte max, byte mode) {
		return (byte) getRandomTriangularShort(min, max, mode);
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
	public default byte getRandomParetoByte(double shape, double scale) {
		return (byte) getRandomParetoShort(shape, scale);
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
	public default byte getRandomWeibullByte(double shape, double scale) {
		return (byte) getRandomWeibullShort(shape, scale);
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
	public default byte getRandomExponentialByte(double scale) {
		return (byte) getRandomExponentialShort(scale);
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
	public default byte getRandomGammaByte(double shape, double scale) {
		return (byte) getRandomGammaShort(shape, scale);
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
	public default byte getRandomBetaByte(double shapeAlpha, double shapeBeta) {
		return (byte) getRandomBetaShort(shapeAlpha, shapeBeta);
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
	public default byte getRandomPoissonByte(double rate) {
		return (byte) getRandomPoissonShort(rate);
	}

	// -----------------------------------------------------------------------------
	// Short integer methods

	/**
	 * Get a random short integer.
	 * 
	 * @return A random short integer.
	 */
	public default short getRandomUniformShort() {
		return (short) getRandomUniformInteger();
	}

	/**
	 * Get a random short integer between 0 and a number.
	 * 
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random short integer between 0 and a number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default short getRandomUniformShort(short max) {
		return (short) getRandomUniformInteger(max);
	}

	/**
	 * Get a random short integer between two numbers.
	 * 
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random short integer between two numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default short getRandomUniformShort(short min, short max) {
		return (short) getRandomUniformInteger(min, max);
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
	public default short getRandomBatesShort(short min, short max, int number) {
		return (short) getRandomBatesInteger(min, max, number);
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
	public default short getRandomNormalShort(double mean, double standardDeviation) {
		return (short) getRandomNormalInteger(mean, standardDeviation);
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
	public default short getRandomLognormalShort(double logMean, double logStandardDeviation) {
		return (short) getRandomLognormalInteger(logMean, logStandardDeviation);
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
	public default short getRandomTriangularShort(short min, short max, short mode) {
		return (short) getRandomTriangularInteger(min, max, mode);
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
	public default short getRandomParetoShort(double shape, double scale) {
		return (short) getRandomParetoInteger(shape, scale);
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
	public default short getRandomWeibullShort(double shape, double scale) {
		return (short) getRandomWeibullInteger(shape, scale);
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
	public default short getRandomExponentialShort(double scale) {
		return (short) getRandomExponentialInteger(scale);
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
	public default short getRandomGammaShort(double shape, double scale) {
		return (short) getRandomGammaInteger(shape, scale);
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
	public default short getRandomBetaShort(double shapeAlpha, double shapeBeta) {
		return (short) getRandomBetaInteger(shapeAlpha, shapeBeta);
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
	public default short getRandomPoissonShort(double rate) {
		return (short) getRandomPoissonInteger(rate);
	}

	// -----------------------------------------------------------------------------
	// Integer methods

	/**
	 * Get a random integer.
	 * 
	 * @return A random integer.
	 */
	public default int getRandomUniformInteger() {
		return (int) getRandomUniformLong();
	}

	/**
	 * Get a random integer between 0 and a number.
	 * 
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random integer between 0 and a number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default int getRandomUniformInteger(int max) {
		return (int) getRandomUniformLong(max);
	}

	/**
	 * Get a random integer between two numbers.
	 * 
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random integer between two numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default int getRandomUniformInteger(int min, int max) {
		return (int) getRandomUniformLong(min, max);
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
	public default int getRandomBatesInteger(int min, int max, int number) {
		return (int) getRandomBatesLong(min, max, number);
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
	public default int getRandomNormalInteger(double mean, double standardDeviation) {
		return (int) getRandomNormalLong(mean, standardDeviation);
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
	public default int getRandomLognormalInteger(double logMean, double logStandardDeviation) {
		return (int) getRandomLognormalLong(logMean, logStandardDeviation);
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
	public default int getRandomTriangularInteger(int min, int max, int mode) {
		return (int) getRandomTriangularLong(min, max, mode);
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
	public default int getRandomParetoInteger(double shape, double scale) {
		return (int) getRandomParetoLong(shape, scale);
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
	public default int getRandomWeibullInteger(double shape, double scale) {
		return (int) getRandomWeibullLong(shape, scale);
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
	public default int getRandomExponentialInteger(double scale) {
		return (int) getRandomExponentialLong(scale);
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
	public default int getRandomGammaInteger(double shape, double scale) {
		return (int) getRandomGammaLong(shape, scale);
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
	public default int getRandomBetaInteger(double shapeAlpha, double shapeBeta) {
		return (int) getRandomBetaLong(shapeAlpha, shapeBeta);
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
	public default int getRandomPoissonInteger(double rate) {
		return (int) getRandomPoissonLong(rate);
	}

	// -----------------------------------------------------------------------------
	// Long integer methods

	/**
	 * Get a random long integer.
	 * 
	 * @return A random long integer.
	 */
	public long getRandomUniformLong();

	/**
	 * Get a random long integer between 0 and a number.
	 * 
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random long integer between 0 and a number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default long getRandomUniformLong(long max) {
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
			result = getRandomUniformLong() & 0x7F_FF_FF_FF_FF_FF_FF_FFL;
		} while (result > unbiasedMaximum);
		return result % max;
	}

	/**
	 * Get a random long integer between two numbers.
	 * 
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random long integer between two numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default long getRandomUniformLong(long min, long max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		return getRandomUniformLong(max - min) + min;
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
	public default long getRandomBatesLong(long min, long max, int number) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		int res = 0;
		long range = max - min;
		for (int i = 0; i < number; ++i) {
			res += getRandomUniformLong(range);
		}
		/*
		 * Improve the distribution of random numbers. Up until here, res is in [0,
		 * range * number - number]. By adding another random number in [0, number - 1],
		 * res will be in [0, range * number - 1]. After dividing res by number, res
		 * will be in [0, range - 1].
		 */
		res += getRandomUniformLong(number);
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
	public default long getRandomNormalLong(double mean, double standardDeviation) {
		return Math.round(getRandomNormalDouble(mean, standardDeviation));
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
	public default long getRandomLognormalLong(double logMean, double logStandardDeviation) {
		return (long) getRandomLognormalDouble(logMean, logStandardDeviation);
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
	public default long getRandomTriangularLong(long min, long max, long mode) {
		double doubleMin = (double) min;
		double doubleMax = (double) max;
		double doubleMode = (double) mode;
		// Improve the distribution when switching between long and double
		doubleMode = doubleMode * (doubleMax / (doubleMax - 1));
		return (long) (getRandomTriangularDouble(doubleMin, doubleMax, doubleMode));
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
	public default long getRandomParetoLong(double shape, double scale) {
		return (long) getRandomParetoDouble(shape, scale);
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
	public default long getRandomWeibullLong(double shape, double scale) {
		return (long) getRandomWeibullDouble(shape, scale);
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
	public default long getRandomExponentialLong(double scale) {
		return (long) getRandomExponentialDouble(scale);
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
	public default long getRandomGammaLong(double shape, double scale) {
		return (long) getRandomGammaDouble(shape, scale);
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
	public default long getRandomBetaLong(double shapeAlpha, double shapeBeta) {
		return (long) getRandomBetaDouble(shapeAlpha, shapeBeta);
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
	public default long getRandomPoissonLong(double rate) {
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
				u = getRandomUniformDouble();
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
	 * Get a random floating point in the range [0.0, 1.0].
	 * 
	 * @return A random floating point in the range [0.0, 1.0].
	 */
	public default float getRandomUniformFloat() {
		return getRandomUniformInteger((1 << 24) + 1) / (float) (1L << 24);
	}

	/**
	 * Get a random floating point between 0 and a number.
	 * 
	 * @param max
	 *                Maximum value of the number, inclusive.
	 * @return A random floating point between 0 and a number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default float getRandomUniformFloat(float max) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		return getRandomUniformFloat() * max;
	}

	/**
	 * Get a random floating point between two numbers.
	 * 
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, inclusive.
	 * @return A random floating point between two numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default float getRandomUniformFloat(float min, float max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		return min + getRandomUniformFloat(max - min);
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
	public default float getRandomBatesFloat(int number) {
		float res = 0;
		for (int i = 0; i < number; ++i) {
			res += getRandomUniformFloat();
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
	public default float getRandomBatesFloat(float max, int number) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		float res = 0;
		for (int i = 0; i < number; ++i) {
			res += getRandomUniformFloat(max);
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
	public default float getRandomBatesFloat(float min, float max, int number) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		float res = 0;
		for (int i = 0; i < number; ++i) {
			res += getRandomUniformFloat(min, max);
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
	public default float getRandomNormalFloat(double mean, double standardDeviation) {
		return (float) getRandomNormalDouble(mean, standardDeviation);
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
	public default float getRandomLognormalFloat(double logMean, double logStandardDeviation) {
		return (float) getRandomLognormalDouble(logMean, logStandardDeviation);
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
	public default float getRandomTriangularFloat(float min, float max, float mode) {
		if (!(min < mode && mode < max)) {
			throw new IllegalArgumentException();
		}
		float uniformFloat = getRandomUniformFloat(min, max);
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
	public default float getRandomParetoFloat(double shape, double scale) {
		return (float) getRandomParetoDouble(shape, scale);
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
	public default float getRandomWeibullFloat(double shape, double scale) {
		return (float) getRandomWeibullDouble(shape, scale);
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
	public default float getRandomExponentialFloat(double scale) {
		return (float) getRandomExponentialDouble(scale);
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
	public default float getRandomGammaFloat(double shape, double scale) {
		return (float) getRandomGammaDouble(shape, scale);
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
	public default float getRandomBetaFloat(double shapeAlpha, double shapeBeta) {
		return (float) getRandomBetaDouble(shapeAlpha, shapeBeta);
	}

	// -----------------------------------------------------------------------------
	// Double floating point methods

	/**
	 * Get a random double floating point in the range [0.0, 1.0].
	 * 
	 * @return A random double floating point in the range [0.0, 1.0].
	 */
	public default double getRandomUniformDouble() {
		return getRandomUniformLong((1L << 53) + 1) / (double) (1L << 53);
	}

	/**
	 * Get a random double floating point between 0 and a number.
	 * 
	 * @param max
	 *                Maximum value of the number, inclusive.
	 * @return A random double floating point between 0 and a number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default double getRandomUniformDouble(double max) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		return getRandomUniformDouble() * max;
	}

	/**
	 * Get a random double floating point between two numbers.
	 * 
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, inclusive.
	 * @return A random double floating point between two numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default double getRandomUniformDouble(double min, double max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		return min + getRandomUniformDouble(max - min);
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
	public default double getRandomBatesDouble(int number) {
		double res = 0;
		for (int i = 0; i < number; ++i) {
			res += getRandomUniformDouble();
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
	public default double getRandomBatesDouble(double max, int number) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		double res = 0;
		for (int i = 0; i < number; ++i) {
			res += getRandomUniformDouble(max);
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
	public default double getRandomBatesDouble(double min, double max, int number) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		double res = 0;
		for (int i = 0; i < number; ++i) {
			res += getRandomUniformDouble(min, max);
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
	public default double getRandomNormalDouble(double mean, double standardDeviation) {
		double random1;
		double random2;
		double squareSum;
		do {
			random1 = getRandomUniformDouble(-1, 1);
			random2 = getRandomUniformDouble(-1, 1);
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
	public default double getRandomLognormalDouble(double logMean, double logStandardDeviation) {
		return Math.exp(getRandomNormalDouble(logMean, logStandardDeviation));
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
	public default double getRandomTriangularDouble(double min, double max, double mode) {
		if (!(min <= mode && mode <= max)) {
			throw new IllegalArgumentException();
		}
		double uniformDouble = getRandomUniformDouble(min, max);
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
	public default double getRandomParetoDouble(double shape, double scale) {
		if (0 >= shape || 0 >= scale) {
			throw new IllegalArgumentException();
		}
		double random;
		do {
			random = getRandomUniformDouble();
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
	public default double getRandomWeibullDouble(double shape, double scale) {
		if (0 >= shape || 0 >= scale) {
			throw new IllegalArgumentException();
		}
		double random;
		do {
			random = getRandomUniformDouble();
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
	public default double getRandomExponentialDouble(double scale) {
		double uniform;
		do {
			uniform = getRandomUniformDouble();
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
	public default double getRandomGammaDouble(double shape, double scale) {
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
				normal = getRandomNormalDouble(0, 1);
				v = (1 + c * normal);
				v = v * v * v;
				dv = d * v;
				// Generate uniform in (0, 1]
				do {
					uniform = getRandomUniformDouble();
				} while (uniform == 0);
			} while (v <= 0 || Math.log(uniform) >= 0.5 * normal * normal + d - dv + d * Math.log(v));
			return dv * scale;
		} else if (shape == 1) {
			return getRandomExponentialDouble(scale);
		} else {
			double uniform;
			do {
				uniform = getRandomUniformDouble();
			} while (uniform == 0);
			return getRandomGammaDouble(shape + 1, scale) * Math.pow(uniform, 1 / shape);
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
	public default double getRandomBetaDouble(double shapeAlpha, double shapeBeta) {
		double gamma = getRandomGammaDouble(shapeAlpha, 1.0);
		if (gamma == 0) {
			return 0;
		} else {
			return gamma / (gamma + getRandomGammaDouble(shapeBeta, 1.0));
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
	public default char getRandomCharacter(String alphabet) {
		return alphabet.charAt(getRandomUniformInteger(alphabet.length()));
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
	public default String getRandomString(String alphabet, int lenght) {
		StringBuilder stringBuilder = new StringBuilder(lenght);
		for (int i = 0; i < lenght; ++i) {
			stringBuilder.append(getRandomCharacter(alphabet));
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
	public default <T> T getRandomElement(T[] elements) {
		int index = getRandomUniformInteger(elements.length);
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
			int j = getRandomUniformInteger(elements.length);
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
	public default <T> T getRandomElement(List<T> elements) {
		int index = getRandomUniformInteger(elements.size());
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
			int j = getRandomUniformInteger(elements.size());
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
	public default <T> T getRandomElement(Set<T> elements) {
		int index = getRandomUniformInteger(elements.size());
		Iterator<T> iterator = elements.iterator();
		while (index > 0) {
			iterator.next();
			--index;
		}
		return iterator.next();
	}

}
