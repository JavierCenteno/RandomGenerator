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
		return nextUniformLong() < 0;
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
	// Byte methods

	/**
	 * Get a random byte.
	 * 
	 * @return A random byte.
	 */
	public default byte nextUniformByte() {
		return (byte) nextUniformLong();
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
		return (byte) nextUniformLong(max);
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
		return (byte) nextUniformLong(min, max);
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
		return (byte) nextBatesLong(min, max, number);
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
	public default byte nextNormalByte(byte mean, byte standardDeviation) {
		return (byte) nextNormalLong(mean, standardDeviation);
	}

	/**
	 * Get a random byte with a lognormal distribution.
	 * 
	 * @param mean
	 *            Mean of the natural logarithm of the distribution.
	 * @param standardDeviation
	 *            Standard deviation of the natural logarithm of the distribution.
	 * @return A random byte with a lognormal distribution.
	 */
	public default byte nextLognormalInteger(byte naturalLogarithmMean, byte naturalLogarithmStandardDeviation) {
		return (byte) nextLognormalLong(naturalLogarithmMean, naturalLogarithmStandardDeviation);
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
		return (byte) nextTriangularLong(min, max, mode);
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
	public default byte nextParetoByte(byte shape, byte scale) {
		return (byte) nextParetoLong(shape, scale);
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
	public default byte nextWeibullByte(byte shape, byte scale) {
		return (byte) nextWeibullLong(shape, scale);
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
	public default byte nextExponentialByte(byte scale) {
		return (byte) nextExponentialLong(scale);
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
	public default byte nextGammaByte(byte shape, byte scale) {
		return (byte) nextGammaLong(shape, scale);
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
	public default byte nextBetaByte(byte shapeAlpha, byte shapeBeta) {
		return (byte) nextBetaLong(shapeAlpha, shapeBeta);
	}

	// -----------------------------------------------------------------------------
	// Short integer methods

	/**
	 * Get a random short integer.
	 * 
	 * @return A random short integer.
	 */
	public default short nextUniformShort() {
		return (short) nextUniformLong();
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
		return (short) nextUniformLong(max);
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
		return (short) nextUniformLong(min, max);
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
		return (short) nextBatesLong(min, max, number);
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
	public default short nextNormalInteger(short mean, short standardDeviation) {
		return (short) nextNormalLong(mean, standardDeviation);
	}

	/**
	 * Get a random short integer with a lognormal distribution.
	 * 
	 * @param mean
	 *            Mean of the natural logarithm of the distribution.
	 * @param standardDeviation
	 *            Standard deviation of the natural logarithm of the distribution.
	 * @return A random short integer with a lognormal distribution.
	 */
	public default short nextLognormalInteger(short naturalLogarithmMean, short naturalLogarithmStandardDeviation) {
		return (short) nextLognormalLong(naturalLogarithmMean, naturalLogarithmStandardDeviation);
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
		return (short) nextTriangularLong(min, max, mode);
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
	public default short nextParetoShort(short shape, short scale) {
		return (short) nextParetoLong(shape, scale);
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
	public default short nextWeibullShort(short shape, short scale) {
		return (short) nextWeibullLong(shape, scale);
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
	public default short nextExponentialShort(short scale) {
		return (short) nextExponentialLong(scale);
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
	public default short nextGammaShort(short shape, short scale) {
		return (short) nextGammaLong(shape, scale);
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
	public default short nextBetaShort(short shapeAlpha, short shapeBeta) {
		return (short) nextBetaLong(shapeAlpha, shapeBeta);
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
	public default int nextNormalInteger(int mean, int standardDeviation) {
		return (int) nextNormalLong(mean, standardDeviation);
	}

	/**
	 * Get a random integer with a lognormal distribution.
	 * 
	 * @param mean
	 *            Mean of the natural logarithm of the distribution.
	 * @param standardDeviation
	 *            Standard deviation of the natural logarithm of the distribution.
	 * @return A random integer with a lognormal distribution.
	 */
	public default int nextLognormalInteger(int naturalLogarithmMean, int naturalLogarithmStandardDeviation) {
		return (int) nextLognormalLong(naturalLogarithmMean, naturalLogarithmStandardDeviation);
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
	public default int nextParetoInteger(int shape, int scale) {
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
	public default int nextWeibullInteger(int shape, int scale) {
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
	public default int nextExponentialInteger(int scale) {
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
	public default int nextGammaInteger(int shape, int scale) {
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
	public default int nextBetaInteger(int shapeAlpha, int shapeBeta) {
		return (int) nextBetaLong(shapeAlpha, shapeBeta);
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
	public default long nextNormalLong(long mean, long standardDeviation) {
		double doubleMean = (double) mean;
		double doubleStandardDeviation = (double) standardDeviation;
		return Math.round(nextNormalDouble(doubleMean, doubleStandardDeviation));
	}

	/**
	 * Get a random long integer with a lognormal distribution.
	 * 
	 * @param mean
	 *            Mean of the natural logarithm of the distribution.
	 * @param standardDeviation
	 *            Standard deviation of the natural logarithm of the distribution.
	 * @return A random long integer with a lognormal distribution.
	 */
	public default long nextLognormalLong(long naturalLogarithmMean, long naturalLogarithmStandardDeviation) {
		return (long) nextLognormalDouble(naturalLogarithmMean, naturalLogarithmStandardDeviation);
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
	public default long nextParetoLong(long shape, long scale) {
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
	public default long nextWeibullLong(long shape, long scale) {
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
	public default long nextExponentialLong(long scale) {
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
	public default long nextGammaLong(long shape, long scale) {
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
	public default long nextBetaLong(long shapeAlpha, long shapeBeta) {
		return (long) nextBetaDouble(shapeAlpha, shapeBeta);
	}

	// -----------------------------------------------------------------------------
	// Floating point methods

	/**
	 * Get a random floating point in the range [0.0, 1.0].
	 * 
	 * @return A random floating point in the range [0.0, 1.0].
	 */
	public default float nextUniformFloat() {
		return nextUniformLong((1L << 24) + 1) / (float) (1L << 24);
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
	public default float nextNormalFloat(float mean, float standardDeviation) {
		return (float) nextNormalDouble(mean, standardDeviation);
	}

	/**
	 * Get a random floating point with a lognormal distribution.
	 * 
	 * @param mean
	 *            Mean of the natural logarithm of the distribution.
	 * @param standardDeviation
	 *            Standard deviation of the natural logarithm of the distribution.
	 * @return A random floating point with a lognormal distribution.
	 */
	public default float nextLognormalFloat(float naturalLogarithmMean, float naturalLogarithmStandardDeviation) {
		return (float) nextLognormalDouble(naturalLogarithmMean, naturalLogarithmStandardDeviation);
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
	public default float nextParetoFloat(float shape, float scale) {
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
	public default float nextWeibullFloat(float shape, float scale) {
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
	public default float nextExponentialFloat(float scale) {
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
	public default float nextGammaFloat(float shape, float scale) {
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
	public default float nextBetaFloat(float shapeAlpha, float shapeBeta) {
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
	 * @param mean
	 *            Mean of the natural logarithm of the distribution.
	 * @param standardDeviation
	 *            Standard deviation of the natural logarithm of the distribution.
	 * @return A random double floating point with a lognormal distribution.
	 */
	public default double nextLognormalDouble(double naturalLogarithmMean, double naturalLogarithmStandardDeviation) {
		return Math.exp(nextNormalDouble(naturalLogarithmMean, naturalLogarithmStandardDeviation));
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