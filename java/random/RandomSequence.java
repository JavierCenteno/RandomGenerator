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

package random;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class represents a sequence of random numbers.
 * 
 * All methods are defined using nextUniformLong(), so in order to extend this
 * class, you just need to implement that method. This, however, may be
 * inefficient for generators that generate less than 8 bytes at a time, in
 * which case it's recommended to extend a method that generates a smaller
 * primitive and then extend the rest of the primitive methods to use the
 * extended method instead.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @since 1.0
 * 
 */
public interface RandomSequence {

	// -----------------------------------------------------------------------------
	// Seed methods

	/**
	 * Resets this generator with the given seed.
	 * 
	 * @param seed
	 *            A seed.
	 */
	public void setSeed(long seed);

	// -----------------------------------------------------------------------------
	// State methods

	/**
	 * Get the current state of this generator.
	 * 
	 * @return The current state of this generator.
	 */
	public long[] getState();

	/**
	 * Set the state of this generator.
	 * 
	 * @param state
	 *            A new state for this generator.
	 */
	public void setState(long[] state);

	// -----------------------------------------------------------------------------
	// Boolean methods

	/**
	 * Get a random boolean.
	 * 
	 * @return A random boolean.
	 */
	public default boolean nextBoolean() {
		return nextUniformByte() < 0;
	}

	/**
	 * Get a random boolean.
	 * 
	 * @param probability
	 *            Probability that this will return true.
	 * @return A random boolean.
	 */
	public default boolean nextBoolean(float probability) {
		return nextUniformFloat() < probability;
	}

	/**
	 * Get a random boolean.
	 * 
	 * @param probability
	 *            Probability that this will return true.
	 * @return A random boolean.
	 */
	public default boolean nextBoolean(double probability) {
		return nextUniformDouble() < probability;
	}

	// -----------------------------------------------------------------------------
	// Bit methods

	/**
	 * Get a long integer containing a number of random bits.
	 * 
	 * @param bits
	 *            Number of bits to return.
	 * @return A long integer containing a number of random bits.
	 */
	public default long nextBits(int bits) {
		return nextUniformLong() >>> (64 - bits);
	}

	// -----------------------------------------------------------------------------
	// Byte methods

	/**
	 * Get a random byte.
	 * 
	 * @return A random byte.
	 */
	public default byte nextUniformByte() {
		return (byte) nextUniformShort();
	}

	/**
	 * Get a random byte between 0 and a number.
	 * 
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @return A random byte between 0 and a number.
	 * @throws IllegalArgumentException
	 *             If max is not larger than 0.
	 */
	public default byte nextUniformByte(byte max) {
		return (byte) nextUniformShort(max);
	}

	/**
	 * Get a random byte between two numbers.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @return A random byte between two numbers.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than max.
	 */
	public default byte nextUniformByte(byte min, byte max) {
		return (byte) nextUniformShort(min, max);
	}

	/**
	 * Get a random byte with a Bates distribution between two numbers.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @param number
	 *            Number of random numbers generated to create this result.
	 * @return A random byte with a Bates distribution.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than max.
	 */
	public default byte nextBatesByte(byte min, byte max, int number) {
		return (byte) nextBatesShort(min, max, number);
	}

	/**
	 * Get a random byte with a normal distribution.
	 * 
	 * @param mean
	 *            Mean of the distribution.
	 * @param standardDeviation
	 *            Standard deviation of the distribution.
	 * @return A random byte with a normal distribution.
	 */
	public default byte nextNormalByte(double mean, double standardDeviation) {
		return (byte) nextNormalShort(mean, standardDeviation);
	}

	/**
	 * Get a random byte with a lognormal distribution.
	 * 
	 * @param logMean
	 *            Mean of the natural logarithm of the distribution.
	 * @param logStandardDeviation
	 *            Standard deviation of the natural logarithm of the distribution.
	 * @return A random byte with a lognormal distribution.
	 */
	public default byte nextLognormalByte(double logMean, double logStandardDeviation) {
		return (byte) nextLognormalShort(logMean, logStandardDeviation);
	}

	/**
	 * Get a random byte with a triangular distribution.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @param mode
	 *            Most common value of the number.
	 * @return A random byte with a triangular distribution.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than or equal to mode and mode is not
	 *             smaller than max.
	 */
	public default byte nextTriangularByte(byte min, byte max, byte mode) {
		return (byte) nextTriangularShort(min, max, mode);
	}

	/**
	 * Get a random byte with a Pareto distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random byte with a Pareto distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default byte nextParetoByte(double shape, double scale) {
		return (byte) nextParetoShort(shape, scale);
	}

	/**
	 * Get a random byte with a Weibull distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random byte with a Weibull distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default byte nextWeibullByte(double shape, double scale) {
		return (byte) nextWeibullShort(shape, scale);
	}

	/**
	 * Get a random byte with an exponential distribution.
	 * 
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random byte with an exponential distribution.
	 * @throws IllegalArgumentException
	 *             If scale is not larger than 0.
	 */
	public default byte nextExponentialByte(double scale) {
		return (byte) nextExponentialShort(scale);
	}

	/**
	 * Get a random byte with a gamma distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random byte with a gamma distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default byte nextGammaByte(double shape, double scale) {
		return (byte) nextGammaShort(shape, scale);
	}

	/**
	 * Get a random byte with a beta distribution.
	 * 
	 * @param shapeAlpha
	 *            Alpha shape parameter of the distribution.
	 * @param shapeBeta
	 *            Beta shape parameter of the distribution.
	 * @return A random byte with a beta distribution.
	 * @throws IllegalArgumentException
	 *             If shapeAlpha is not larger than 0 or shapeBeta is not larger
	 *             than 0.
	 */
	public default byte nextBetaByte(double shapeAlpha, double shapeBeta) {
		return (byte) nextBetaShort(shapeAlpha, shapeBeta);
	}

	/**
	 * Get a random byte with a poisson distribution.
	 * 
	 * @param rate
	 *            Rate parameter of the distribution.
	 * @return A random byte with a poisson distribution.
	 * @throws IllegalArgumentException
	 *             If rate is not larger than 0.
	 */
	public default byte nextPoissonByte(double rate) {
		return (byte) nextPoissonShort(rate);
	}

	// -----------------------------------------------------------------------------
	// Short integer methods

	/**
	 * Get a random short integer.
	 * 
	 * @return A random short integer.
	 */
	public default short nextUniformShort() {
		return (short) nextUniformInteger();
	}

	/**
	 * Get a random short integer between 0 and a number.
	 * 
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @return A random short integer between 0 and a number.
	 * @throws IllegalArgumentException
	 *             If max is not larger than 0.
	 */
	public default short nextUniformShort(short max) {
		return (short) nextUniformInteger(max);
	}

	/**
	 * Get a random short integer between two numbers.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @return A random short integer between two numbers.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than max.
	 */
	public default short nextUniformShort(short min, short max) {
		return (short) nextUniformInteger(min, max);
	}

	/**
	 * Get a random short integer with a Bates distribution between two numbers.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @param number
	 *            Number of random numbers generated to create this result.
	 * @return A random short integer with a Bates distribution.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than max.
	 */
	public default short nextBatesShort(short min, short max, int number) {
		return (short) nextBatesInteger(min, max, number);
	}

	/**
	 * Get a random short integer with a normal distribution.
	 * 
	 * @param mean
	 *            Mean of the distribution.
	 * @param standardDeviation
	 *            Standard deviation of the distribution.
	 * @return A random short integer with a normal distribution.
	 */
	public default short nextNormalShort(double mean, double standardDeviation) {
		return (short) nextNormalInteger(mean, standardDeviation);
	}

	/**
	 * Get a random short integer with a lognormal distribution.
	 * 
	 * @param logMean
	 *            Mean of the natural logarithm of the distribution.
	 * @param logStandardDeviation
	 *            Standard deviation of the natural logarithm of the distribution.
	 * @return A random short integer with a lognormal distribution.
	 */
	public default short nextLognormalShort(double logMean, double logStandardDeviation) {
		return (short) nextLognormalInteger(logMean, logStandardDeviation);
	}

	/**
	 * Get a random short integer with a triangular distribution.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @param mode
	 *            Most common value of the number.
	 * @return A random short integer with a triangular distribution.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than or equal to mode and mode is not
	 *             smaller than max.
	 */
	public default short nextTriangularShort(short min, short max, short mode) {
		return (short) nextTriangularInteger(min, max, mode);
	}

	/**
	 * Get a random short integer with a Pareto distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random short integer with a Pareto distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default short nextParetoShort(double shape, double scale) {
		return (short) nextParetoInteger(shape, scale);
	}

	/**
	 * Get a random short integer with a Weibull distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random short integer with a Weibull distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default short nextWeibullShort(double shape, double scale) {
		return (short) nextWeibullInteger(shape, scale);
	}

	/**
	 * Get a random short integer with an exponential distribution.
	 * 
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random short integer with an exponential distribution.
	 * @throws IllegalArgumentException
	 *             If scale is not larger than 0.
	 */
	public default short nextExponentialShort(double scale) {
		return (short) nextExponentialInteger(scale);
	}

	/**
	 * Get a random short integer with a gamma distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random short integer with a gamma distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default short nextGammaShort(double shape, double scale) {
		return (short) nextGammaInteger(shape, scale);
	}

	/**
	 * Get a random short integer with a beta distribution.
	 * 
	 * @param shapeAlpha
	 *            Alpha shape parameter of the distribution.
	 * @param shapeBeta
	 *            Beta shape parameter of the distribution.
	 * @return A random short integer with a beta distribution.
	 * @throws IllegalArgumentException
	 *             If shapeAlpha is not larger than 0 or shapeBeta is not larger
	 *             than 0.
	 */
	public default short nextBetaShort(double shapeAlpha, double shapeBeta) {
		return (short) nextBetaInteger(shapeAlpha, shapeBeta);
	}

	/**
	 * Get a random short integer with a poisson distribution.
	 * 
	 * @param rate
	 *            Rate parameter of the distribution.
	 * @return A random short integer with a poisson distribution.
	 * @throws IllegalArgumentException
	 *             If rate is not larger than 0.
	 */
	public default short nextPoissonShort(double rate) {
		return (short) nextPoissonInteger(rate);
	}

	// -----------------------------------------------------------------------------
	// Integer methods

	/**
	 * Get a random integer.
	 * 
	 * @return A random integer.
	 */
	public default int nextUniformInteger() {
		return (int) nextUniformLong();
	}

	/**
	 * Get a random integer between 0 and a number.
	 * 
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @return A random integer between 0 and a number.
	 * @throws IllegalArgumentException
	 *             If max is not larger than 0.
	 */
	public default int nextUniformInteger(int max) {
		return (int) nextUniformLong(max);
	}

	/**
	 * Get a random integer between two numbers.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @return A random integer between two numbers.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than max.
	 */
	public default int nextUniformInteger(int min, int max) {
		return (int) nextUniformLong(min, max);
	}

	/**
	 * Get a random integer with a Bates distribution between two numbers.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @param number
	 *            Number of random numbers generated to create this result.
	 * @return A random integer with a Bates distribution.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than max.
	 */
	public default int nextBatesInteger(int min, int max, int number) {
		return (int) nextBatesLong(min, max, number);
	}

	/**
	 * Get a random integer with a normal distribution.
	 * 
	 * @param mean
	 *            Mean of the distribution.
	 * @param standardDeviation
	 *            Standard deviation of the distribution.
	 * @return A random integer with a normal distribution.
	 */
	public default int nextNormalInteger(double mean, double standardDeviation) {
		return (int) nextNormalLong(mean, standardDeviation);
	}

	/**
	 * Get a random integer with a lognormal distribution.
	 * 
	 * @param logMean
	 *            Mean of the natural logarithm of the distribution.
	 * @param logStandardDeviation
	 *            Standard deviation of the natural logarithm of the distribution.
	 * @return A random integer with a lognormal distribution.
	 */
	public default int nextLognormalInteger(double logMean, double logStandardDeviation) {
		return (int) nextLognormalLong(logMean, logStandardDeviation);
	}

	/**
	 * Get a random integer with a triangular distribution.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @param mode
	 *            Most common value of the number.
	 * @return A random integer with a triangular distribution.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than or equal to mode and mode is not
	 *             smaller than max.
	 */
	public default int nextTriangularInteger(int min, int max, int mode) {
		return (int) nextTriangularLong(min, max, mode);
	}

	/**
	 * Get a random integer with a Pareto distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random integer with a Pareto distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default int nextParetoInteger(double shape, double scale) {
		return (int) nextParetoLong(shape, scale);
	}

	/**
	 * Get a random integer with a Weibull distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random integer with a Weibull distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default int nextWeibullInteger(double shape, double scale) {
		return (int) nextWeibullLong(shape, scale);
	}

	/**
	 * Get a random integer with an exponential distribution.
	 * 
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random integer with an exponential distribution.
	 * @throws IllegalArgumentException
	 *             If scale is not larger than 0.
	 */
	public default int nextExponentialInteger(double scale) {
		return (int) nextExponentialLong(scale);
	}

	/**
	 * Get a random integer with a gamma distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random integer with a gamma distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default int nextGammaInteger(double shape, double scale) {
		return (int) nextGammaLong(shape, scale);
	}

	/**
	 * Get a random integer with a beta distribution.
	 * 
	 * @param shapeAlpha
	 *            Alpha shape parameter of the distribution.
	 * @param shapeBeta
	 *            Beta shape parameter of the distribution.
	 * @return A random integer with a beta distribution.
	 * @throws IllegalArgumentException
	 *             If shapeAlpha is not larger than 0 or shapeBeta is not larger
	 *             than 0.
	 */
	public default int nextBetaInteger(double shapeAlpha, double shapeBeta) {
		return (int) nextBetaLong(shapeAlpha, shapeBeta);
	}

	/**
	 * Get a random integer with a poisson distribution.
	 * 
	 * @param rate
	 *            Rate parameter of the distribution.
	 * @return A random integer with a poisson distribution.
	 * @throws IllegalArgumentException
	 *             If rate is not larger than 0.
	 */
	public default long nextPoissonInteger(double rate) {
		return (int) nextPoissonLong(rate);
	}

	// -----------------------------------------------------------------------------
	// Long integer methods

	/**
	 * Get a random long integer.
	 * 
	 * @return A random long integer.
	 */
	public long nextUniformLong();

	/**
	 * Get a random long integer between 0 and a number.
	 * 
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @return A random long integer between 0 and a number.
	 * @throws IllegalArgumentException
	 *             If max is not larger than 0.
	 */
	public default long nextUniformLong(long max) {
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
			result = nextUniformLong() & 0x7F_FF_FF_FF_FF_FF_FF_FFL;
		} while (result > unbiasedMaximum);
		return result % max;
	}

	/**
	 * Get a random long integer between two numbers.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @return A random long integer between two numbers.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than max.
	 */
	public default long nextUniformLong(long min, long max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		return nextUniformLong(max - min) + min;
	}

	/**
	 * Get a random long integer with a Bates distribution between two numbers.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @param number
	 *            Number of random numbers generated to create this result.
	 * @return A random long integer with a Bates distribution.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than max.
	 */
	public default long nextBatesLong(long min, long max, int number) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		int res = 0;
		long range = max - min;
		for (int i = 0; i < number; ++i) {
			res += nextUniformLong(range);
		}
		/*
		 * Improve the distribution of random numbers. Up until here, res is in [0,
		 * range * number - number]. By adding another random number in [0, number - 1],
		 * res will be in [0, range * number - 1]. After dividing res by number, res
		 * will be in [0, range - 1].
		 */
		res += nextUniformLong(number);
		res /= number;
		res += min;
		return res;
	}

	/**
	 * Get a random long integer with a normal distribution.
	 * 
	 * @param mean
	 *            Mean of the distribution.
	 * @param standardDeviation
	 *            Standard deviation of the distribution.
	 * @return A random long integer with a normal distribution.
	 */
	public default long nextNormalLong(double mean, double standardDeviation) {
		return Math.round(nextNormalDouble(mean, standardDeviation));
	}

	/**
	 * Get a random long integer with a lognormal distribution.
	 * 
	 * @param logMean
	 *            Mean of the natural logarithm of the distribution.
	 * @param logStandardDeviation
	 *            Standard deviation of the natural logarithm of the distribution.
	 * @return A random long integer with a lognormal distribution.
	 */
	public default long nextLognormalLong(double logMean, double logStandardDeviation) {
		return (long) nextLognormalDouble(logMean, logStandardDeviation);
	}

	/**
	 * Get a random long integer with a triangular distribution.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, exclusive.
	 * @param mode
	 *            Most common value of the number.
	 * @return A random long integer with a triangular distribution.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than or equal to mode and mode is not
	 *             smaller than max.
	 */
	public default long nextTriangularLong(long min, long max, long mode) {
		double doubleMin = (double) min;
		double doubleMax = (double) max;
		double doubleMode = (double) mode;
		// Improve the distribution when switching between long and double
		doubleMode = doubleMode * (doubleMax / (doubleMax - 1));
		return (long) (nextTriangularDouble(doubleMin, doubleMax, doubleMode));
	}

	/**
	 * Get a random long integer with a Pareto distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random long integer with a Pareto distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default long nextParetoLong(double shape, double scale) {
		return (long) nextParetoDouble(shape, scale);
	}

	/**
	 * Get a random long integer with a Weibull distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random long integer with a Weibull distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default long nextWeibullLong(double shape, double scale) {
		return (long) nextWeibullDouble(shape, scale);
	}

	/**
	 * Get a random long integer with an exponential distribution.
	 * 
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random long integer with an exponential distribution.
	 * @throws IllegalArgumentException
	 *             If scale is not larger than 0.
	 */
	public default long nextExponentialLong(double scale) {
		return (long) nextExponentialDouble(scale);
	}

	/**
	 * Get a random long integer with a gamma distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random long integer with a gamma distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default long nextGammaLong(double shape, double scale) {
		return (long) nextGammaDouble(shape, scale);
	}

	/**
	 * Get a random long integer with a beta distribution.
	 * 
	 * @param shapeAlpha
	 *            Alpha shape parameter of the distribution.
	 * @param shapeBeta
	 *            Beta shape parameter of the distribution.
	 * @return A random long integer with a beta distribution.
	 * @throws IllegalArgumentException
	 *             If shapeAlpha is not larger than 0 or shapeBeta is not larger
	 *             than 0.
	 */
	public default long nextBetaLong(double shapeAlpha, double shapeBeta) {
		return (long) nextBetaDouble(shapeAlpha, shapeBeta);
	}

	/**
	 * Get a random long integer with a poisson distribution.
	 * 
	 * @param rate
	 *            Rate parameter of the distribution.
	 * @return A random long integer with a poisson distribution.
	 * @throws IllegalArgumentException
	 *             If rate is not larger than 0.
	 */
	public default long nextPoissonLong(double rate) {
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
				u = nextUniformDouble();
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
	public default float nextUniformFloat() {
		return nextUniformInteger((1 << 24) + 1) / (float) (1L << 24);
	}

	/**
	 * Get a random floating point between 0 and a number.
	 * 
	 * @param max
	 *            Maximum value of the number, inclusive.
	 * @return A random floating point between 0 and a number.
	 * @throws IllegalArgumentException
	 *             If max is not larger than 0.
	 */
	public default float nextUniformFloat(float max) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		return nextUniformFloat() * max;
	}

	/**
	 * Get a random floating point between two numbers.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, inclusive.
	 * @return A random floating point between two numbers.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than max.
	 */
	public default float nextUniformFloat(float min, float max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		return min + nextUniformFloat(max - min);
	}

	/**
	 * Get a random floating point with a Bates distribution in the range [0.0,
	 * 1.0].
	 * 
	 * @param number
	 *            Number of random numbers generated to create this result.
	 * @return A random floating point with a Bates distribution in the range [0.0,
	 *         1.0].
	 */
	public default float nextBatesFloat(int number) {
		float res = 0;
		for (int i = 0; i < number; ++i) {
			res += nextUniformFloat();
		}
		res /= number;
		return res;
	}

	/**
	 * Get a random floating point with a Bates distribution between 0 and a number.
	 * 
	 * @param max
	 *            Maximum value of the number, inclusive.
	 * @param number
	 *            Number of random numbers generated to create this result.
	 * @return A random floating point with a Bates distribution between 0 and a
	 *         number.
	 * @throws IllegalArgumentException
	 *             If max is not larger than 0.
	 */
	public default float nextBatesFloat(float max, int number) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		float res = 0;
		for (int i = 0; i < number; ++i) {
			res += nextUniformFloat(max);
		}
		res /= number;
		return res;
	}

	/**
	 * Get a random floating point with a Bates distribution between two numbers.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, inclusive.
	 * @param number
	 *            Number of random numbers generated to create this result.
	 * @return A random floating point with a Bates distribution between two
	 *         numbers.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than max.
	 */
	public default float nextBatesFloat(float min, float max, int number) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		float res = 0;
		for (int i = 0; i < number; ++i) {
			res += nextUniformFloat(min, max);
		}
		res /= number;
		return res;
	}

	/**
	 * Get a random floating point with a normal distribution.
	 * 
	 * @param mean
	 *            Mean of the distribution.
	 * @param standardDeviation
	 *            Standard deviation of the distribution.
	 * @return A random floating point with a normal distribution.
	 */
	public default float nextNormalFloat(double mean, double standardDeviation) {
		return (float) nextNormalDouble(mean, standardDeviation);
	}

	/**
	 * Get a random floating point with a lognormal distribution.
	 * 
	 * @param logMean
	 *            Mean of the natural logarithm of the distribution.
	 * @param logStandardDeviation
	 *            Standard deviation of the natural logarithm of the distribution.
	 * @return A random floating point with a lognormal distribution.
	 */
	public default float nextLognormalFloat(double logMean, double logStandardDeviation) {
		return (float) nextLognormalDouble(logMean, logStandardDeviation);
	}

	/**
	 * Get a random floating point with a triangular distribution.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, inclusive.
	 * @param mode
	 *            Most common value of the number.
	 * @return A random floating point with a triangular distribution.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than or equal to mode and mode is not
	 *             smaller than max.
	 */
	public default float nextTriangularFloat(float min, float max, float mode) {
		if (!(min < mode && mode < max)) {
			throw new IllegalArgumentException();
		}
		float uniformFloat = nextUniformFloat(min, max);
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
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random floating point with a Pareto distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default float nextParetoFloat(double shape, double scale) {
		return (float) nextParetoDouble(shape, scale);
	}

	/**
	 * Get a random floating point with a Weibull distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random floating point with a Weibull distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default float nextWeibullFloat(double shape, double scale) {
		return (float) nextWeibullDouble(shape, scale);
	}

	/**
	 * Get a random floating point with an exponential distribution.
	 * 
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random floating point with an exponential distribution.
	 * @throws IllegalArgumentException
	 *             If scale is not larger than 0.
	 */
	public default float nextExponentialFloat(double scale) {
		return (float) nextExponentialDouble(scale);
	}

	/**
	 * Get a random floating point with a gamma distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random floating point with a gamma distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default float nextGammaFloat(double shape, double scale) {
		return (float) nextGammaDouble(shape, scale);
	}

	/**
	 * Get a random floating point with a beta distribution.
	 * 
	 * @param shapeAlpha
	 *            Alpha shape parameter of the distribution.
	 * @param shapeBeta
	 *            Beta shape parameter of the distribution.
	 * @return A random floating point with a beta distribution.
	 * @throws IllegalArgumentException
	 *             If shapeAlpha is not larger than 0 or shapeBeta is not larger
	 *             than 0.
	 */
	public default float nextBetaFloat(double shapeAlpha, double shapeBeta) {
		return (float) nextBetaDouble(shapeAlpha, shapeBeta);
	}

	// -----------------------------------------------------------------------------
	// Double floating point methods

	/**
	 * Get a random double floating point in the range [0.0, 1.0].
	 * 
	 * @return A random double floating point in the range [0.0, 1.0].
	 */
	public default double nextUniformDouble() {
		return nextUniformLong((1L << 53) + 1) / (double) (1L << 53);
	}

	/**
	 * Get a random double floating point between 0 and a number.
	 * 
	 * @param max
	 *            Maximum value of the number, inclusive.
	 * @return A random double floating point between 0 and a number.
	 * @throws IllegalArgumentException
	 *             If max is not larger than 0.
	 */
	public default double nextUniformDouble(double max) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		return nextUniformDouble() * max;
	}

	/**
	 * Get a random double floating point between two numbers.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, inclusive.
	 * @return A random double floating point between two numbers.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than max.
	 */
	public default double nextUniformDouble(double min, double max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		return min + nextUniformDouble(max - min);
	}

	/**
	 * Get a random double floating point with a Bates distribution in the range
	 * [0.0, 1.0].
	 * 
	 * @param number
	 *            Number of random numbers generated to create this result.
	 * @return A random double floating point with a Bates distribution in the range
	 *         [0.0, 1.0].
	 */
	public default double nextBatesDouble(int number) {
		double res = 0;
		for (int i = 0; i < number; ++i) {
			res += nextUniformDouble();
		}
		res /= number;
		return res;
	}

	/**
	 * Get a random double floating point with a Bates distribution between 0 and a
	 * number.
	 * 
	 * @param max
	 *            Maximum value of the number, inclusive.
	 * @param number
	 *            Number of random numbers generated to create this result.
	 * @return A random double floating point with a Bates distribution between 0
	 *         and a number.
	 * @throws IllegalArgumentException
	 *             If max is not larger than 0.
	 */
	public default double nextBatesDouble(double max, int number) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		double res = 0;
		for (int i = 0; i < number; ++i) {
			res += nextUniformDouble(max);
		}
		res /= number;
		return res;
	}

	/**
	 * Get a random double floating point with a Bates distribution between two
	 * numbers.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, inclusive.
	 * @param number
	 *            Number of random numbers generated to create this result.
	 * @return A random double floating point with a Bates distribution between two
	 *         numbers.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than max.
	 */
	public default double nextBatesDouble(double min, double max, int number) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		double res = 0;
		for (int i = 0; i < number; ++i) {
			res += nextUniformDouble(min, max);
		}
		res /= number;
		return res;
	}

	/**
	 * Get a random double floating point with a normal distribution.
	 * 
	 * @param mean
	 *            Mean of the distribution.
	 * @param standardDeviation
	 *            Standard deviation of the distribution.
	 * @return A random double floating point with a normal distribution.
	 */
	public default double nextNormalDouble(double mean, double standardDeviation) {
		double random1;
		double random2;
		double squareSum;
		do {
			random1 = nextUniformDouble(-1, 1);
			random2 = nextUniformDouble(-1, 1);
			squareSum = random1 * random1 + random2 * random2;
		} while (squareSum >= 1 || squareSum == 0);
		return standardDeviation * random1 * Math.sqrt(-2 * Math.log(squareSum) / squareSum) + mean;
	}

	/**
	 * Get a random double floating point with a lognormal distribution.
	 * 
	 * @param logMean
	 *            Mean of the natural logarithm of the distribution.
	 * @param logStandardDeviation
	 *            Standard deviation of the natural logarithm of the distribution.
	 * @return A random double floating point with a lognormal distribution.
	 */
	public default double nextLognormalDouble(double logMean, double logStandardDeviation) {
		return Math.exp(nextNormalDouble(logMean, logStandardDeviation));
	}

	/**
	 * Get a random double floating point with a triangular distribution.
	 * 
	 * @param min
	 *            Minimum value of the number, inclusive.
	 * @param max
	 *            Maximum value of the number, inclusive.
	 * @param mode
	 *            Most common value of the number.
	 * @return A random double floating point with a triangular distribution.
	 * @throws IllegalArgumentException
	 *             If min is not smaller than or equal to mode and mode is not
	 *             smaller than max.
	 */
	public default double nextTriangularDouble(double min, double max, double mode) {
		if (!(min <= mode && mode <= max)) {
			throw new IllegalArgumentException();
		}
		double uniformDouble = nextUniformDouble(min, max);
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
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random double floating point with a Pareto distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default double nextParetoDouble(double shape, double scale) {
		if (0 >= shape || 0 >= scale) {
			throw new IllegalArgumentException();
		}
		double random;
		do {
			random = nextUniformDouble();
		} while (random == 0);
		return scale / Math.pow(random, 1 / shape);
	}

	/**
	 * Get a random double floating point with a Weibull distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random double floating point with a Weibull distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default double nextWeibullDouble(double shape, double scale) {
		if (0 >= shape || 0 >= scale) {
			throw new IllegalArgumentException();
		}
		double random;
		do {
			random = nextUniformDouble();
		} while (random == 0);
		return scale * Math.pow(-Math.log(random), 1 / shape);
	}

	/**
	 * Get a random double floating point with an exponential distribution.
	 * 
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random double floating point with an exponential distribution.
	 * @throws IllegalArgumentException
	 *             If scale is not larger than 0.
	 */
	public default double nextExponentialDouble(double scale) {
		double uniform;
		do {
			uniform = nextUniformDouble();
		} while (uniform == 0);
		return Math.log(uniform) * scale;
	}

	/**
	 * Get a random double floating point with a gamma distribution.
	 * 
	 * @param shape
	 *            Shape parameter of the distribution.
	 * @param scale
	 *            Scale parameter of the distribution.
	 * @return A random double floating point with a gamma distribution.
	 * @throws IllegalArgumentException
	 *             If shape is not larger than 0 or scale is not larger than 0.
	 */
	public default double nextGammaDouble(double shape, double scale) {
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
				normal = nextNormalDouble(0, 1);
				v = (1 + c * normal);
				v = v * v * v;
				dv = d * v;
				// Generate uniform in (0, 1]
				do {
					uniform = nextUniformDouble();
				} while (uniform == 0);
			} while (v <= 0 || Math.log(uniform) >= 0.5 * normal * normal + d - dv + d * Math.log(v));
			return dv * scale;
		} else if (shape == 1) {
			return nextExponentialDouble(scale);
		} else {
			double uniform;
			do {
				uniform = nextUniformDouble();
			} while (uniform == 0);
			return nextGammaDouble(shape + 1, scale) * Math.pow(uniform, 1 / shape);
		}
	}

	/**
	 * Get a random double floating point with a beta distribution.
	 * 
	 * @param shapeAlpha
	 *            Alpha shape parameter of the distribution.
	 * @param shapeBeta
	 *            Beta shape parameter of the distribution.
	 * @return A random double floating point with a beta distribution.
	 * @throws IllegalArgumentException
	 *             If shapeAlpha is not larger than 0 or shapeBeta is not larger
	 *             than 0.
	 */
	public default double nextBetaDouble(double shapeAlpha, double shapeBeta) {
		double gamma = nextGammaDouble(shapeAlpha, 1.0);
		if (gamma == 0) {
			return 0;
		} else {
			return gamma / (gamma + nextGammaDouble(shapeBeta, 1.0));
		}
	}

	// -----------------------------------------------------------------------------
	// Character methods

	/**
	 * Gets a random character from the given String.
	 * 
	 * @param alphabet
	 *            A string of characters this method will pick from.
	 * @return A random character from the given String.
	 */
	public default char nextCharacter(String alphabet) {
		return alphabet.charAt(nextUniformInteger(alphabet.length()));
	}

	// -----------------------------------------------------------------------------
	// String methods

	/**
	 * Gets a random String from characters picked from the given String.
	 * 
	 * @param alphabet
	 *            A string of characters this method will pick from.
	 * @return A random String from characters picked from the given String.
	 */
	public default String nextString(String alphabet, int lenght) {
		StringBuilder stringBuilder = new StringBuilder(lenght);
		for (int i = 0; i < lenght; ++i) {
			stringBuilder.append(nextCharacter(alphabet));
		}
		return stringBuilder.toString();
	}

	// -----------------------------------------------------------------------------
	// Array methods

	/**
	 * Get a random element from an array.
	 * 
	 * @param elements
	 *            An array of elements.
	 * @return A random element from an array.
	 */
	public default <T> T nextElement(T[] elements) {
		int index = nextUniformInteger(elements.length);
		return elements[index];
	}

	/**
	 * Shuffle the elements in an array randomly.
	 * 
	 * @param elements
	 *            An array of elements.
	 * @return A shuffled array.
	 */
	public default <T> T[] shuffle(T[] elements) {
		for (int i = 0; i < elements.length; ++i) {
			int j = nextUniformInteger(elements.length);
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
	 *            A list of elements.
	 * @return A random element from a list.
	 */
	public default <T> T nextElement(List<T> elements) {
		int index = nextUniformInteger(elements.size());
		return elements.get(index);
	}

	/**
	 * Shuffle the elements in a list randomly.
	 * 
	 * @param elements
	 *            A list of elements.
	 * @return A shuffled list.
	 */
	public default <T> List<T> shuffle(List<T> elements) {
		for (int i = 0; i < elements.size(); ++i) {
			int j = nextUniformInteger(elements.size());
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
	 *            A set of elements.
	 * @return A random element from a set.
	 */
	public default <T> T nextElement(Set<T> elements) {
		int index = nextUniformInteger(elements.size());
		Iterator<T> iterator = elements.iterator();
		while (index > 0) {
			iterator.next();
			--index;
		}
		return iterator.next();
	}

}
