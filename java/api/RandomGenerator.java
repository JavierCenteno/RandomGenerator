package api;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import generators.Xoroshiro128StarStarGenerator;
import util.ByteConverter;

/**
 * This interface offers methods to generate random data with different
 * distributions.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @since 1.0
 *
 */
public interface RandomGenerator {

	////////////////////////////////////////////////////////////////////////////////
	// Seed generator

	/**
	 * Default generator of seeds.
	 */
	public static final RandomGenerator DEFAULT_SEED_GENERATOR = new Xoroshiro128StarStarGenerator(
			ByteConverter.longsToBytes(new long[] { System.nanoTime(),
					(System.nanoTime() * 6364136223846793005L) + 1442695040888963407L }));

	////////////////////////////////////////////////////////////////////////////////
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

			////////////////////////////////////////////////////////////////////////////////
			// Class fields

			private static final long serialVersionUID = 1L;

			////////////////////////////////////////////////////////////////////////////////
			// Instance fields

			RandomGenerator source;

			////////////////////////////////////////////////////////////////////////////////
			// Instance initializers

			public RandomWrapper(final RandomGenerator source) {
				this.source = source;
			}

			////////////////////////////////////////////////////////////////////////////////
			// Instance methods

			@Override
			public void setSeed(final long seed) {
				// Ignore the seed that the Random() constructor will try to impose on us.
			}

			@Override
			public int next(final int bits) {
				return this.source.generateIntegerBits(bits);
			}

			@Override
			public void nextBytes(final byte[] bytes) {
				for (int i = 0; i < bytes.length; ++i) {
					bytes[i] = this.source.generateUniformByte();
				}
			}

			@Override
			public int nextInt() {
				return this.source.generateUniformInteger();
			}

			@Override
			public int nextInt(final int n) {
				return this.source.generateUniformInteger(n);
			}

			@Override
			public long nextLong() {
				return this.source.generateUniformLong();
			}

			@Override
			public boolean nextBoolean() {
				return this.source.generateBoolean();
			}

			@Override
			public float nextFloat() {
				float result;
				do {
					result = this.source.generateUniformFloat();
				} while (result == 1.0f);
				return result;
			}

			@Override
			public double nextDouble() {
				double result;
				do {
					result = this.source.generateUniformDouble();
				} while (result == 1.0d);
				return result;
			}

			@Override
			public double nextGaussian() {
				return this.source.generateNormalDouble(0.0d, 1.0d);
			}

		}
		return new RandomWrapper(this);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Seed methods

	/**
	 * Get the size of this generator's seed in bytes. This is the minimum number of
	 * bytes needed for the seed passed to the setSeed(byte[]) method in order for
	 * that method to execute without failing if such an operation is supported.
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
	public default void setSeed(final byte[] seed) {
		this.setState(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// State methods

	/**
	 * Get the size of this generator's state in bytes. This is the minimum number
	 * of bytes needed for the state passed to the setState(byte[]) method in order
	 * for that method to execute without failing if such an operation is supported.
	 *
	 * @throws UnsupportedOperationException
	 *                                           If this generator doesn't support
	 *                                           setting its state.
	 */
	public default int getStateSize() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get the current state of this generator. Generators implementing this
	 * operation must guarantee that instancing a new generator "newGenerator" of
	 * the same class and invoking newGenerator.setState(this.getState()) results in
	 * a generator with the same state that produces the same results.
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
	 * Set the state of this generator. Generators implementing this operation must
	 * guarantee that instancing a new generator "newGenerator" of the same class
	 * and invoking newGenerator.setState(this.getState()) results in a generator
	 * with the same state that produces the same results.
	 *
	 * @param state
	 *                  A new state for this generator.
	 * @throws IllegalArgumentException
	 *                                           If the state is too short.
	 * @throws UnsupportedOperationException
	 *                                           If this generator doesn't support
	 *                                           setting its state.
	 */
	public default void setState(final byte[] state) {
		throw new UnsupportedOperationException();
	}

	////////////////////////////////////////////////////////////////////////////////
	// Generator operation methods

	/**
	 * Obtain a new generator from this one.
	 *
	 * @throws UnsupportedOperationException
	 *                                           If this generator doesn't support
	 *                                           splitting.
	 */
	public default RandomGenerator split() {
		throw new UnsupportedOperationException();
	}

	////////////////////////////////////////////////////////////////////////////////
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
	public default byte generateByteBits(final int bits) {
		final int shift = Byte.SIZE - bits;
		if ((shift < 0) || (bits < 0)) {
			throw new IllegalArgumentException();
		}
		return (byte) (this.generateUniformByte() >>> shift);
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
	public default short generateShortBits(final int bits) {
		final int shift = Short.SIZE - bits;
		if ((shift < 0) || (bits < 0)) {
			throw new IllegalArgumentException();
		}
		return (short) (this.generateUniformShort() >>> shift);
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
	public default int generateIntegerBits(final int bits) {
		final int shift = Integer.SIZE - bits;
		if ((shift < 0) || (bits < 0)) {
			throw new IllegalArgumentException();
		}
		return this.generateUniformInteger() >>> shift;
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
	public default long generateLongBits(final int bits) {
		final int shift = Long.SIZE - bits;
		if ((shift < 0) || (bits < 0)) {
			throw new IllegalArgumentException();
		}
		return this.generateUniformLong() >>> shift;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Boolean methods

	/**
	 * Get a random boolean.
	 *
	 * @return A random boolean.
	 */
	public default boolean generateBoolean() {
		return this.generateUniformByte() < 0;
	}

	/**
	 * Fills an array with the results of generateBoolean().
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBoolean().
	 * @see generateBoolean()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default boolean[] generateBooleans(final int size) {
		final boolean[] result = new boolean[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBoolean();
		}
		return result;
	}

	/**
	 * Get a random boolean.
	 *
	 * @param probability
	 *                        Probability that this will return true.
	 * @return A random boolean.
	 */
	public default boolean generateBoolean(final float probability) {
		return this.generateUniformFloat() < probability;
	}

	/**
	 * Fills an array with the results of generateBoolean(float).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBoolean(float).
	 * @see generateBoolean(float)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default boolean[] generateBooleans(final float probability, final int size) {
		final boolean[] result = new boolean[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBoolean(probability);
		}
		return result;
	}

	/**
	 * Get a random boolean.
	 *
	 * @param probability
	 *                        Probability that this will return true.
	 * @return A random boolean.
	 */
	public default boolean generateBoolean(final double probability) {
		return this.generateUniformDouble() < probability;
	}

	/**
	 * Fills an array with the results of generateBoolean(double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBoolean(double).
	 * @see generateBoolean(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default boolean[] generateBooleans(final double probability, final int size) {
		final boolean[] result = new boolean[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBoolean(probability);
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Byte methods

	/**
	 * @see RandomGenerator#generateUniformByte()
	 */
	public default byte generateByte() {
		return this.generateUniformByte();
	}

	/**
	 * Fills an array with the results of generateByte().
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateByte().
	 * @see generateByte()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateBytes(final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateByte();
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformByte(byte)
	 */
	public default byte generateByte(final byte max) {
		return this.generateUniformByte(max);
	}

	/**
	 * Fills an array with the results of generateByte(byte).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateByte(byte).
	 * @see generateByte(byte)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateBytes(final byte max, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateByte(max);
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformByte(byte, byte)
	 */
	public default byte generateByte(final byte min, final byte max) {
		return this.generateUniformByte(min, max);
	}

	/**
	 * Fills an array with the results of generateByte(byte, byte).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateByte(byte, byte).
	 * @see generateByte(byte, byte)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateBytes(final byte min, final byte max, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateByte(min, max);
		}
		return result;
	}

	/**
	 * Get a random byte with a uniform distribution.
	 *
	 * @return A random byte with a uniform distribution.
	 */
	public byte generateUniformByte();

	/**
	 * Fills an array with the results of generateUniformByte().
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformByte().
	 * @see generateUniformByte()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateUniformBytes(final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformByte();
		}
		return result;
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
	public default byte generateUniformByte(final byte max) {
		return (byte) this.generateUniformShort(max);
	}

	/**
	 * Fills an array with the results of generateUniformByte(byte).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformByte(byte).
	 * @see generateUniformByte(byte)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateUniformBytes(final byte max, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformByte(max);
		}
		return result;
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
	public default byte generateUniformByte(final byte min, final byte max) {
		return (byte) this.generateUniformShort(min, max);
	}

	/**
	 * Fills an array with the results of generateUniformByte(byte, byte).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformByte(byte, byte).
	 * @see generateUniformByte(byte, byte)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateUniformBytes(final byte min, final byte max, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformByte(min, max);
		}
		return result;
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
	public default byte generateBatesByte(final byte min, final byte max, final int number) {
		return (byte) this.generateBatesShort(min, max, number);
	}

	/**
	 * Fills an array with the results of generateBatesByte(byte, byte, int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBatesByte(byte, byte,
	 *         int).
	 * @see generateBatesByte(byte, byte, int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateBatesBytes(final byte min, final byte max, final int number, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBatesByte(min, max, number);
		}
		return result;
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
	public default byte generateNormalByte(final double mean, final double standardDeviation) {
		return (byte) this.generateNormalShort(mean, standardDeviation);
	}

	/**
	 * Fills an array with the results of generateNormalByte(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateNormalByte(double,
	 *         double).
	 * @see generateNormalByte(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateNormalBytes(final double mean, final double standardDeviation, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateNormalByte(mean, standardDeviation);
		}
		return result;
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
	public default byte generateLognormalByte(final double logMean, final double logStandardDeviation) {
		return (byte) this.generateLognormalShort(logMean, logStandardDeviation);
	}

	/**
	 * Fills an array with the results of generateLognormalByte(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateLognormalByte(double,
	 *         double).
	 * @see generateLognormalByte(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateLognormalBytes(final double logMean, final double logStandardDeviation,
			final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateLognormalByte(logMean, logStandardDeviation);
		}
		return result;
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
	public default byte generateTriangularByte(final byte min, final byte max, final byte mode) {
		return (byte) this.generateTriangularShort(min, max, mode);
	}

	/**
	 * Fills an array with the results of generateTriangularByte(byte, byte, byte).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateTriangularByte(byte,
	 *         byte, byte).
	 * @see generateTriangularByte(byte, byte, byte)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateTriangularBytes(final byte min, final byte max, final byte mode, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateTriangularByte(min, max, mode);
		}
		return result;
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
	public default byte generateParetoByte(final double shape, final double scale) {
		return (byte) this.generateParetoShort(shape, scale);
	}

	/**
	 * Fills an array with the results of generateParetoByte(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateParetoByte(double,
	 *         double).
	 * @see generateParetoByte(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateParetoBytes(final double shape, final double scale, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateParetoByte(shape, scale);
		}
		return result;
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
	public default byte generateWeibullByte(final double shape, final double scale) {
		return (byte) this.generateWeibullShort(shape, scale);
	}

	/**
	 * Fills an array with the results of generateWeibullByte(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateWeibullByte(double,
	 *         double).
	 * @see generateWeibullByte(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateWeibullBytes(final double shape, final double scale, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateWeibullByte(shape, scale);
		}
		return result;
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
	public default byte generateExponentialByte(final double scale) {
		return (byte) this.generateExponentialShort(scale);
	}

	/**
	 * Fills an array with the results of generateExponentialByte(double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateExponentialByte(double).
	 * @see generateExponentialByte(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateExponentialBytes(final double scale, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateExponentialByte(scale);
		}
		return result;
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
	public default byte generateGammaByte(final double shape, final double scale) {
		return (byte) this.generateGammaShort(shape, scale);
	}

	/**
	 * Fills an array with the results of generateGammaByte(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateGammaByte(double,
	 *         double).
	 * @see generateGammaByte(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateGammaBytes(final double shape, final double scale, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateGammaByte(shape, scale);
		}
		return result;
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
	public default byte generateBetaByte(final double shapeAlpha, final double shapeBeta) {
		return (byte) this.generateBetaShort(shapeAlpha, shapeBeta);
	}

	/**
	 * Fills an array with the results of generateBetaByte(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBetaByte(double, double).
	 * @see generateBetaByte(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generateBetaBytes(final double shapeAlpha, final double shapeBeta, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBetaByte(shapeAlpha, shapeBeta);
		}
		return result;
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
	public default byte generatePoissonByte(final double rate) {
		return (byte) this.generatePoissonShort(rate);
	}

	/**
	 * Fills an array with the results of generatePoissonByte(double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generatePoissonByte(double).
	 * @see generatePoissonByte(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default byte[] generatePoissonBytes(final double rate, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generatePoissonByte(rate);
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Short integer methods

	/**
	 * @see RandomGenerator#generateUniformShort()
	 */
	public default short generateShort() {
		return this.generateUniformShort();
	}

	/**
	 * Fills an array with the results of generateShort().
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateShort().
	 * @see generateShort()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateShorts(final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateShort();
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformShort(short)
	 */
	public default short generateShort(final short max) {
		return this.generateUniformShort(max);
	}

	/**
	 * Fills an array with the results of generateShort(short).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateShort(short).
	 * @see generateShort(short)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateShorts(final short max, final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateShort(max);
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformShort(short, short)
	 */
	public default short generateShort(final short min, final short max) {
		return this.generateUniformShort(min, max);
	}

	/**
	 * Fills an array with the results of generateShort(short, short).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateShort(short, short).
	 * @see generateShort(short, short)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateShorts(final short min, final short max, final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateShort(min, max);
		}
		return result;
	}

	/**
	 * Get a random short integer with a uniform distribution.
	 *
	 * @return A random short integer with a uniform distribution.
	 */
	public short generateUniformShort();

	/**
	 * Fills an array with the results of generateUniformShort().
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformShort().
	 * @see generateUniformShort()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateUniformShorts(final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformShort();
		}
		return result;
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
	public default short generateUniformShort(final short max) {
		return (short) this.generateUniformInteger(max);
	}

	/**
	 * Fills an array with the results of generateUniformShort(short).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformShort(short).
	 * @see generateUniformShort(short)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateUniformShorts(final short max, final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformShort(max);
		}
		return result;
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
	public default short generateUniformShort(final short min, final short max) {
		return (short) this.generateUniformInteger(min, max);
	}

	/**
	 * Fills an array with the results of generateUniformShort(short, short).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformShort(short,
	 *         short).
	 * @see generateUniformShort(short, short)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateUniformShorts(final short min, final short max, final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformShort(min, max);
		}
		return result;
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
	public default short generateBatesShort(final short min, final short max, final int number) {
		return (short) this.generateBatesInteger(min, max, number);
	}

	/**
	 * Fills an array with the results of generateBatesShort(short, short, int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBatesShort(short, short,
	 *         int).
	 * @see generateBatesShort(short, short, int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateBatesShorts(final short min, final short max, final int number, final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBatesShort(min, max, number);
		}
		return result;
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
	public default short generateNormalShort(final double mean, final double standardDeviation) {
		return (short) this.generateNormalInteger(mean, standardDeviation);
	}

	/**
	 * Fills an array with the results of generateNormalShort(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateNormalShort(double,
	 *         double).
	 * @see generateNormalShort(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateNormalShorts(final double mean, final double standardDeviation, final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateNormalShort(mean, standardDeviation);
		}
		return result;
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
	public default short generateLognormalShort(final double logMean, final double logStandardDeviation) {
		return (short) this.generateLognormalInteger(logMean, logStandardDeviation);
	}

	/**
	 * Fills an array with the results of generateLognormalShort(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateLognormalShort(double,
	 *         double).
	 * @see generateLognormalShort(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateLognormalShorts(final double mean, final double standardDeviation, final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateLognormalShort(mean, standardDeviation);
		}
		return result;
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
	public default short generateTriangularShort(final short min, final short max, final short mode) {
		return (short) this.generateTriangularInteger(min, max, mode);
	}

	/**
	 * Fills an array with the results of generateTriangularShort(short, short,
	 * short).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateTriangularShort(short,
	 *         short, short).
	 * @see generateTriangularShort(short, short, short)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateTriangularShorts(final short min, final short max, final short mode,
			final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateTriangularShort(min, max, mode);
		}
		return result;
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
	public default short generateParetoShort(final double shape, final double scale) {
		return (short) this.generateParetoInteger(shape, scale);
	}

	/**
	 * Fills an array with the results of generateParetoShort(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateParetoShort(double,
	 *         double).
	 * @see generateParetoShort(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateParetoShorts(final double shape, final double scale, final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateParetoShort(shape, scale);
		}
		return result;
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
	public default short generateWeibullShort(final double shape, final double scale) {
		return (short) this.generateWeibullInteger(shape, scale);
	}

	/**
	 * Fills an array with the results of generateWeibullShort(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateWeibullShort(double,
	 *         double).
	 * @see generateWeibullShort(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateWeibullShorts(final double shape, final double scale, final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateWeibullShort(shape, scale);
		}
		return result;
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
	public default short generateExponentialShort(final double scale) {
		return (short) this.generateExponentialInteger(scale);
	}

	/**
	 * Fills an array with the results of generateExponentialShort(double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateExponentialShort(double).
	 * @see generateExponentialShort(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateExponentialShorts(final double scale, final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateExponentialShort(scale);
		}
		return result;
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
	public default short generateGammaShort(final double shape, final double scale) {
		return (short) this.generateGammaInteger(shape, scale);
	}

	/**
	 * Fills an array with the results of generateGammaShort(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateGammaShort(double,
	 *         double).
	 * @see generateGammaShort(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateGammaShorts(final double shape, final double scale, final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateGammaShort(shape, scale);
		}
		return result;
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
	public default short generateBetaShort(final double shapeAlpha, final double shapeBeta) {
		return (short) this.generateBetaInteger(shapeAlpha, shapeBeta);
	}

	/**
	 * Fills an array with the results of generateBetaShort(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBetaShort(double,
	 *         double).
	 * @see generateBetaShort(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generateBetaShorts(final double shapeAlpha, final double shapeBeta, final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBetaShort(shapeAlpha, shapeBeta);
		}
		return result;
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
	public default short generatePoissonShort(final double rate) {
		return (short) this.generatePoissonInteger(rate);
	}

	/**
	 * Fills an array with the results of generatePoissonShort(double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generatePoissonShort(double).
	 * @see generatePoissonShort(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default short[] generatePoissonShorts(final double rate, final int size) {
		final short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generatePoissonShort(rate);
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Integer methods

	/**
	 * @see RandomGenerator#generateUniformInteger()
	 */
	public default int generateInteger() {
		return this.generateUniformInteger();
	}

	/**
	 * Fills an array with the results of generateInteger().
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateInteger().
	 * @see generateInteger()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateIntegers(final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateInteger();
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformInteger(int)
	 */
	public default int generateInteger(final int max) {
		return this.generateUniformInteger(max);
	}

	/**
	 * Fills an array with the results of generateInteger(int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateInteger(int).
	 * @see generateInteger(int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateIntegers(final int max, final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateInteger(max);
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformInteger(int, int)
	 */
	public default int generateInteger(final int min, final int max) {
		return this.generateUniformInteger(min, max);
	}

	/**
	 * Fills an array with the results of generateInteger(int, int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateInteger(int, int).
	 * @see generateInteger(int, int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateIntegers(final int min, final int max, final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateInteger(min, max);
		}
		return result;
	}

	/**
	 * Get a random integer with a uniform distribution.
	 *
	 * @return A random integer with a uniform distribution.
	 */
	public int generateUniformInteger();

	/**
	 * Fills an array with the results of generateUniformInteger().
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformInteger().
	 * @see generateUniformInteger()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateUniformIntegers(final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformInteger();
		}
		return result;
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
	public default int generateUniformInteger(final int max) {
		return (int) this.generateUniformLong(max);
	}

	/**
	 * Fills an array with the results of generateUniformInteger(int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformInteger(int).
	 * @see generateUniformInteger(int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateUniformIntegers(final int max, final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformInteger(max);
		}
		return result;
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
	public default int generateUniformInteger(final int min, final int max) {
		return (int) this.generateUniformLong(min, max);
	}

	/**
	 * Fills an array with the results of generateUniformInteger(int, int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformInteger(int, int).
	 * @see generateUniformInteger(int, int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateUniformIntegers(final int min, final int max, final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformInteger(min, max);
		}
		return result;
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
	public default int generateBatesInteger(final int min, final int max, final int number) {
		return (int) this.generateBatesLong(min, max, number);
	}

	/**
	 * Fills an array with the results of generateBatesInteger(int, int, int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBatesInteger(int, int,
	 *         int).
	 * @see generateBatesInteger(int, int, int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateBatesIntegers(final int min, final int max, final int number, final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBatesInteger(min, number, max);
		}
		return result;
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
	public default int generateNormalInteger(final double mean, final double standardDeviation) {
		return (int) this.generateNormalLong(mean, standardDeviation);
	}

	/**
	 * Fills an array with the results of generateNormalInteger(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateNormalInteger(double,
	 *         double).
	 * @see generateNormalInteger(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateNormalIntegers(final double mean, final double standardDeviation, final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateNormalInteger(mean, standardDeviation);
		}
		return result;
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
	public default int generateLognormalInteger(final double logMean, final double logStandardDeviation) {
		return (int) this.generateLognormalLong(logMean, logStandardDeviation);
	}

	/**
	 * Fills an array with the results of generateLognormalInteger(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateLognormalInteger(double,
	 *         double).
	 * @see generateLognormalInteger(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateLognormalIntegers(final double logMean, final double logStandardDeviation,
			final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateLognormalInteger(logMean, logStandardDeviation);
		}
		return result;
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
	public default int generateTriangularInteger(final int min, final int max, final int mode) {
		return (int) this.generateTriangularLong(min, max, mode);
	}

	/**
	 * Fills an array with the results of generateTriangularInteger(int, int, int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateTriangularInteger(int,
	 *         int, int).
	 * @see generateTriangularInteger(int, int, int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateTriangularIntegers(final int min, final int max, final int mode, final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateTriangularInteger(min, max, mode);
		}
		return result;
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
	public default int generateParetoInteger(final double shape, final double scale) {
		return (int) this.generateParetoLong(shape, scale);
	}

	/**
	 * Fills an array with the results of generateParetoInteger(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateParetoInteger(double,
	 *         double).
	 * @see generateParetoInteger(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateParetoIntegers(final double shape, final double scale, final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateParetoInteger(shape, scale);
		}
		return result;
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
	public default int generateWeibullInteger(final double shape, final double scale) {
		return (int) this.generateWeibullLong(shape, scale);
	}

	/**
	 * Fills an array with the results of generateWeibullInteger(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateWeibullInteger(double,
	 *         double).
	 * @see generateWeibullInteger(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateWeibullIntegers(final double shape, final double scale, final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateWeibullInteger(shape, scale);
		}
		return result;
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
	public default int generateExponentialInteger(final double scale) {
		return (int) this.generateExponentialLong(scale);
	}

	/**
	 * Fills an array with the results of generateExponentialInteger(double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of
	 *         generateExponentialInteger(double).
	 * @see generateExponentialInteger(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateExponentialIntegers(final double scale, final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateExponentialInteger(scale);
		}
		return result;
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
	public default int generateGammaInteger(final double shape, final double scale) {
		return (int) this.generateGammaLong(shape, scale);
	}

	/**
	 * Fills an array with the results of generateGammaInteger(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateGammaInteger(double,
	 *         double).
	 * @see generateGammaInteger(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateGammaIntegers(final double shape, final double scale, final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateGammaInteger(shape, scale);
		}
		return result;
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
	public default int generateBetaInteger(final double shapeAlpha, final double shapeBeta) {
		return (int) this.generateBetaLong(shapeAlpha, shapeBeta);
	}

	/**
	 * Fills an array with the results of generateBetaInteger(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBetaInteger(double,
	 *         double).
	 * @see generateBetaInteger(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generateBetaIntegers(final double shapeAlpha, final double shapeBeta, final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBetaInteger(shapeAlpha, shapeBeta);
		}
		return result;
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
	public default int generatePoissonInteger(final double rate) {
		return (int) this.generatePoissonLong(rate);
	}

	/**
	 * Fills an array with the results of generatePoissonInteger(double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generatePoissonInteger(double).
	 * @see generatePoissonInteger(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default int[] generatePoissonIntegers(final double rate, final int size) {
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generatePoissonInteger(rate);
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Long integer methods

	/**
	 * @see RandomGenerator#generateUniformLong()
	 */
	public default long generateLong() {
		return this.generateUniformLong();
	}

	/**
	 * Fills an array with the results of generateLong().
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateLong().
	 * @see generateLong()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateLongs(final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateLong();
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformLong(long)
	 */
	public default long generateLong(final long max) {
		return this.generateUniformLong(max);
	}

	/**
	 * Fills an array with the results of generateLong(long).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateLong(long).
	 * @see generateLong(long)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateLongs(final long max, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateLong(max);
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformLong(long, long)
	 */
	public default long generateLong(final long min, final long max) {
		return this.generateUniformLong(min, max);
	}

	/**
	 * Fills an array with the results of generateLong(long, long).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateLong(long, long).
	 * @see generateLong(long, long)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateLongs(final long min, final long max, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateLong(min, max);
		}
		return result;
	}

	/**
	 * Get a random long integer with a uniform distribution.
	 *
	 * @return A random long integer with a uniform distribution.
	 */
	public long generateUniformLong();

	/**
	 * Fills an array with the results of generateUniformLong().
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformLong().
	 * @see generateUniformLong()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateUniformLongs(final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformLong();
		}
		return result;
	}

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
	public default long generateUniformLong(final long max) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		long result;
		// Distance from the highest multiple of max to the absolute maximum value of
		// long
		final long moduloBias = (Long.MIN_VALUE - max) % max;
		// Maximum value without modulo bias, exclusive
		final long unbiasedMaximum = Long.MIN_VALUE - moduloBias;
		do {
			result = this.generateUniformLong() & 0x7F_FF_FF_FF_FF_FF_FF_FFL;
		} while (result >= unbiasedMaximum);
		return result % max;
	}

	/**
	 * Fills an array with the results of generateUniformLong(long).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformLong(long).
	 * @see generateUniformLong(long)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateUniformLongs(final long max, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformLong(max);
		}
		return result;
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
	public default long generateUniformLong(final long min, final long max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		return this.generateUniformLong(max - min) + min;
	}

	/**
	 * Fills an array with the results of generateUniformLong(long, long).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformLong(long, long).
	 * @see generateUniformLong(long, long)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateUniformLongs(final long min, final long max, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformLong(min, max);
		}
		return result;
	}

	/**
	 * Get a random unsigned long integer with a uniform distribution between 0 and
	 * a number.
	 *
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random unsigned long integer with a uniform distribution between 0
	 *         and a number.
	 * @throws IllegalArgumentException
	 *                                      If max is 0.
	 */
	public default long generateUniformUnsignedLong(final long max) {
		if (0 == max) {
			throw new IllegalArgumentException();
		}
		long result;
		// Distance from the highest multiple of max to the long range
		final long moduloBias = Long.remainderUnsigned(-max, max);
		if (moduloBias == 0) {
			result = this.generateLong();
		} else {
			// Maximum value without modulo bias, exclusive
			// Note this would result in an infinite loop if moduloBias == 0
			final long unbiasedMaximum = 0 - moduloBias;
			do {
				result = this.generateLong();
			} while (Long.compareUnsigned(result, unbiasedMaximum) >= 0);// result >= unbiasedMaximum);
		}
		return Long.remainderUnsigned(result, max);
	}

	/**
	 * Fills an array with the results of generateUniformUnsignedLong(long).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of
	 *         generateUniformUnsignedLong(long).
	 * @see generateUniformUnsignedLong(long)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateUniformUnsignedLongs(final long max, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformUnsignedLong(max);
		}
		return result;
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
	 *                                      If min equals max.
	 */
	public default long generateUniformUnsignedLong(final long min, final long max) {
		final long range = max - min;
		return min + this.generateLong(range);
	}

	/**
	 * Fills an array with the results of generateUniformUnsignedLong(long).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of
	 *         generateUniformUnsignedLong(long).
	 * @see generateUniformUnsignedLong(long)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateUniformUnsignedLongs(final long min, final long max, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformUnsignedLong(min, max);
		}
		return result;
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
	public default long generateBatesLong(final long min, final long max, final int number) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		int res = 0;
		final long range = max - min;
		for (int i = 0; i < number; ++i) {
			res += this.generateUniformLong(range);
		}
		/*
		 * Improve the distribution of random numbers. Up until here, res is in [0,
		 * range * number - number]. By adding another random number in [0, number - 1],
		 * res will be in [0, range * number - 1]. After dividing res by number, res
		 * will be in [0, range - 1].
		 */
		res += this.generateUniformLong(number);
		res /= number;
		res += min;
		return res;
	}

	/**
	 * Fills an array with the results of generateBatesLong(long, long, int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBatesLong(long, long,
	 *         int).
	 * @see generateBatesLong(long, long, int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateBatesLongs(final long min, final long max, final int number, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBatesLong(min, max, number);
		}
		return result;
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
	public default long generateNormalLong(final double mean, final double standardDeviation) {
		return Math.round(this.generateNormalDouble(mean, standardDeviation));
	}

	/**
	 * Fills an array with the results of generateNormalLong(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateNormalLong(double,
	 *         double).
	 * @see generateNormalLong(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateNormalLongs(final double mean, final double standardDeviation, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateNormalLong(mean, standardDeviation);
		}
		return result;
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
	public default long generateLognormalLong(final double logMean, final double logStandardDeviation) {
		return (long) this.generateLognormalDouble(logMean, logStandardDeviation);
	}

	/**
	 * Fills an array with the results of generateLognormalLong(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateLognormalLong(double,
	 *         double).
	 * @see generateLognormalLong(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateLognormalLongs(final double logMean, final double logStandardDeviation,
			final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateLognormalLong(logMean, logStandardDeviation);
		}
		return result;
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
	public default long generateTriangularLong(final long min, final long max, final long mode) {
		final double doubleMin = (double) min;
		final double doubleMax = (double) max;
		double doubleMode = (double) mode;
		// Improve the distribution when switching between long and double
		doubleMode = doubleMode * (doubleMax / (doubleMax - 1));
		return (long) (this.generateTriangularDouble(doubleMin, doubleMax, doubleMode));
	}

	/**
	 * Fills an array with the results of generateTriangularLong(long, long, long).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateTriangularLong(long,
	 *         long, long).
	 * @see generateTriangularLong(long, long, long)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateTriangularLongs(final long min, final long max, final long mode, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateTriangularLong(min, max, mode);
		}
		return result;
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
	public default long generateParetoLong(final double shape, final double scale) {
		return (long) this.generateParetoDouble(shape, scale);
	}

	/**
	 * Fills an array with the results of generateParetoLong(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateParetoLong(double,
	 *         double).
	 * @see generateParetoLong(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateParetoLongs(final double shape, final double scale, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateParetoLong(shape, scale);
		}
		return result;
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
	public default long generateWeibullLong(final double shape, final double scale) {
		return (long) this.generateWeibullDouble(shape, scale);
	}

	/**
	 * Fills an array with the results of generateWeibullLong(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateWeibullLong(double,
	 *         double).
	 * @see generateWeibullLong(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateWeibullLongs(final double shape, final double scale, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateWeibullLong(shape, scale);
		}
		return result;
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
	public default long generateExponentialLong(final double scale) {
		return (long) this.generateExponentialDouble(scale);
	}

	/**
	 * Fills an array with the results of generateExponentialLong(double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateExponentialLong(double).
	 * @see generateExponentialLong(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateExponentialLongs(final double scale, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateExponentialLong(scale);
		}
		return result;
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
	public default long generateGammaLong(final double shape, final double scale) {
		return (long) this.generateGammaDouble(shape, scale);
	}

	/**
	 * Fills an array with the results of generateGammaLong(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateGammaLong(double,
	 *         double).
	 * @see generateGammaLong(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateGammaLongs(final double shape, final double scale, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateGammaLong(shape, scale);
		}
		return result;
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
	public default long generateBetaLong(final double shapeAlpha, final double shapeBeta) {
		return (long) this.generateBetaDouble(shapeAlpha, shapeBeta);
	}

	/**
	 * Fills an array with the results of generateBetaLong(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBetaLong(double, double).
	 * @see generateBetaLong(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateBetaLongs(final double shapeAlpha, final double shapeBeta, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBetaLong(shapeAlpha, shapeBeta);
		}
		return result;
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
	public default long generatePoissonLong(final double rate) {
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
				u = this.generateUniformDouble();
			} while ((u == 0.0) || (u == 1.0));
			p = p * u;
			while ((p < 1.0) && (l > 0.0)) {
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

	/**
	 * Fills an array with the results of generatePoissonLong(double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generatePoissonLong(double).
	 * @see generatePoissonLong(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generatePoissonLongs(final double rate, final int size) {
		final long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generatePoissonLong(rate);
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Floating point methods

	/**
	 * @see RandomGenerator#generateUniformFloat()
	 */
	public default float generateFloat() {
		return this.generateUniformFloat();
	}

	/**
	 * Fills an array with the results of generateFloat().
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateFloat().
	 * @see generateFloat()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateFloats(final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateFloat();
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformFloat(float)
	 */
	public default float generateFloat(final float max) {
		return this.generateUniformFloat(max);
	}

	/**
	 * Fills an array with the results of generateFloat(float).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateFloat(float).
	 * @see generateFloat(float)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateFloats(final float max, final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateFloat(max);
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformFloat(float, float)
	 */
	public default float generateFloat(final float min, final float max) {
		return this.generateUniformFloat(min, max);
	}

	/**
	 * Fills an array with the results of generateFloat(float, float).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateFloat(float, float).
	 * @see generateFloat(float, float)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateFloats(final float min, final float max, final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateFloat(min, max);
		}
		return result;
	}

	/**
	 * Get a random floating point with a uniform distribution in the range [0.0,
	 * 1.0).
	 *
	 * @return A random floating point with a uniform distribution in the range
	 *         [0.0, 1.0).
	 */
	public default float generateUniformFloat() {
		return this.generateUniformInteger((1 << 24)) / (float) (1L << 24);
	}

	/**
	 * Fills an array with the results of generateUniformFloat().
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformFloat().
	 * @see generateUniformFloat()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateUniformFloats(final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformFloat();
		}
		return result;
	}

	/**
	 * Get a random floating point with a uniform distribution between 0 and a
	 * number.
	 *
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random floating point with a uniform distribution between 0 and a
	 *         number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default float generateUniformFloat(final float max) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		return this.generateUniformFloat() * max;
	}

	/**
	 * Fills an array with the results of generateUniformFloat(float).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformFloat(float).
	 * @see generateUniformFloat(float)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateUniformFloats(final float max, final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformFloat(max);
		}
		return result;
	}

	/**
	 * Get a random floating point with a uniform distribution between two numbers.
	 *
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random floating point with a uniform distribution between two
	 *         numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default float generateUniformFloat(final float min, final float max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		return min + this.generateUniformFloat(max - min);
	}

	/**
	 * Fills an array with the results of generateUniformFloat(float, float).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformFloat(float,
	 *         float).
	 * @see generateUniformFloat(float, float)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateUniformFloats(final float min, final float max, final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformFloat(min, max);
		}
		return result;
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
	public default float generateBatesFloat(final int number) {
		float res = 0;
		for (int i = 0; i < number; ++i) {
			res += this.generateUniformFloat();
		}
		res /= number;
		return res;
	}

	/**
	 * Fills an array with the results of generateBatesFloat(int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBatesFloat(int).
	 * @see generateBatesFloat(int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateBatesFloats(final int number, final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBatesFloat(number);
		}
		return result;
	}

	/**
	 * Get a random floating point with a Bates distribution between 0 and a number.
	 *
	 * @param max
	 *                   Maximum value of the number, exclusive.
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random floating point with a Bates distribution between 0 and a
	 *         number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default float generateBatesFloat(final float max, final int number) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		float res = 0;
		for (int i = 0; i < number; ++i) {
			res += this.generateUniformFloat(max);
		}
		res /= number;
		return res;
	}

	/**
	 * Fills an array with the results of generateBatesFloat(float, int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBatesFloat(float, int).
	 * @see generateBatesFloat(float, int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateBatesFloats(final float max, final int number, final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBatesFloat(max, number);
		}
		return result;
	}

	/**
	 * Get a random floating point with a Bates distribution between two numbers.
	 *
	 * @param min
	 *                   Minimum value of the number, inclusive.
	 * @param max
	 *                   Maximum value of the number, exclusive.
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random floating point with a Bates distribution between two
	 *         numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default float generateBatesFloat(final float min, final float max, final int number) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		float res = 0;
		for (int i = 0; i < number; ++i) {
			res += this.generateUniformFloat(min, max);
		}
		res /= number;
		return res;
	}

	/**
	 * Fills an array with the results of generateBatesFloat(float, float, int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBatesFloat(float, float,
	 *         int).
	 * @see generateBatesFloat(float, float, int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateBatesFloats(final float min, final float max, final int number, final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBatesFloat(min, max, number);
		}
		return result;
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
	public default float generateNormalFloat(final double mean, final double standardDeviation) {
		return (float) this.generateNormalDouble(mean, standardDeviation);
	}

	/**
	 * Fills an array with the results of generateNormalFloat(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateNormalFloat(double,
	 *         double).
	 * @see generateNormalFloat(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateNormalFloats(final double mean, final double standardDeviation, final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateNormalFloat(mean, standardDeviation);
		}
		return result;
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
	public default float generateLognormalFloat(final double logMean, final double logStandardDeviation) {
		return (float) this.generateLognormalDouble(logMean, logStandardDeviation);
	}

	/**
	 * Fills an array with the results of generateLognormalFloat(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateLognormalFloat(double,
	 *         double).
	 * @see generateLognormalFloat(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateLognormalFloats(final double logMean, final double logStandardDeviation,
			final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateLognormalFloat(logMean, logStandardDeviation);
		}
		return result;
	}

	/**
	 * Get a random floating point with a triangular distribution.
	 *
	 * @param min
	 *                 Minimum value of the number, inclusive.
	 * @param max
	 *                 Maximum value of the number, exclusive.
	 * @param mode
	 *                 Most common value of the number.
	 * @return A random floating point with a triangular distribution.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than or equal to
	 *                                      mode and mode is not smaller than max.
	 */
	public default float generateTriangularFloat(final float min, final float max, final float mode) {
		if (!((min < mode) && (mode < max))) {
			throw new IllegalArgumentException();
		}
		final float uniformFloat = this.generateUniformFloat(min, max);
		if (uniformFloat < mode) {
			return (float) (min + Math.sqrt(uniformFloat * (mode - min)));
		} else {
			return max - (float) Math.sqrt((max - uniformFloat) * (max - mode));
		}
	}

	/**
	 * Fills an array with the results of generateTriangularFloat(float, float,
	 * float).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateTriangularFloat(float,
	 *         float, float).
	 * @see generateTriangularFloat(float, float, float)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateTriangularFloats(final float min, final float max, final float mode,
			final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateTriangularFloat(min, max, mode);
		}
		return result;
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
	public default float generateParetoFloat(final double shape, final double scale) {
		return (float) this.generateParetoDouble(shape, scale);
	}

	/**
	 * Fills an array with the results of generateParetoFloat(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateParetoFloat(double,
	 *         double).
	 * @see generateParetoFloat(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateParetoFloats(final double shape, final double scale, final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateParetoFloat(shape, scale);
		}
		return result;
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
	public default float generateWeibullFloat(final double shape, final double scale) {
		return (float) this.generateWeibullDouble(shape, scale);
	}

	/**
	 * Fills an array with the results of generateWeibullFloat(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateWeibullFloat(double,
	 *         double).
	 * @see generateWeibullFloat(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateWeibullFloats(final double shape, final double scale, final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateWeibullFloat(shape, scale);
		}
		return result;
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
	public default float generateExponentialFloat(final double scale) {
		return (float) this.generateExponentialDouble(scale);
	}

	/**
	 * Fills an array with the results of generateExponentialFloat(double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateExponentialFloat(double).
	 * @see generateExponentialFloat(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateExponentialFloats(final double scale, final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateExponentialFloat(scale);
		}
		return result;
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
	public default float generateGammaFloat(final double shape, final double scale) {
		return (float) this.generateGammaDouble(shape, scale);
	}

	/**
	 * Fills an array with the results of generateGammaFloat(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateGammaFloat(double,
	 *         double).
	 * @see generateGammaFloat(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateGammaFloats(final double shape, final double scale, final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateGammaFloat(shape, scale);
		}
		return result;
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
	public default float generateBetaFloat(final double shapeAlpha, final double shapeBeta) {
		return (float) this.generateBetaDouble(shapeAlpha, shapeBeta);
	}

	/**
	 * Fills an array with the results of generateBetaFloat(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBetaFloat(double,
	 *         double).
	 * @see generateBetaFloat(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateBetaFloats(final double shapeAlpha, final double shapeBeta, final int size) {
		final float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBetaFloat(shapeAlpha, shapeBeta);
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Double floating point methods

	/**
	 * @see RandomGenerator#generateUniformDouble()
	 */
	public default double generateDouble() {
		return this.generateUniformDouble();
	}

	/**
	 * Fills an array with the results of generateDouble().
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateDouble().
	 * @see generateDouble()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateDoubles(final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateDouble();
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformDouble(double)
	 */
	public default double generateDouble(final double max) {
		return this.generateUniformDouble(max);
	}

	/**
	 * Fills an array with the results of generateDouble(double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateDouble(double).
	 * @see generateDouble(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateDoubles(final double max, final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateDouble(max);
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformDouble(double, double)
	 */
	public default double generateDouble(final double min, final double max) {
		return this.generateUniformDouble(min, max);
	}

	/**
	 * Fills an array with the results of generateDouble(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateDouble(double, double).
	 * @see generateDouble(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateDoubles(final double min, final double max, final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateDouble(min, max);
		}
		return result;
	}

	/**
	 * Get a random double floating point with a uniform distribution in the range
	 * [0.0, 1.0).
	 *
	 * @return A random double floating point with a uniform distribution in the
	 *         range [0.0, 1.0).
	 */
	public default double generateUniformDouble() {
		return this.generateUniformLong((1L << 53)) / (double) (1L << 53);
	}

	/**
	 * Fills an array with the results of generateUniformDouble().
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformDouble().
	 * @see generateUniformDouble()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateUniformDoubles(final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformDouble();
		}
		return result;
	}

	/**
	 * Get a random double floating point with a uniform distribution between 0 and
	 * a number.
	 *
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random double floating point with a uniform distribution between 0
	 *         and a number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default double generateUniformDouble(final double max) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		return this.generateUniformDouble() * max;
	}

	/**
	 * Fills an array with the results of generateUniformDouble(double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformDouble(double).
	 * @see generateUniformDouble(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateUniformDoubles(final double max, final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformDouble(max);
		}
		return result;
	}

	/**
	 * Get a random double floating point with a uniform distribution between two
	 * numbers.
	 *
	 * @param min
	 *                Minimum value of the number, inclusive.
	 * @param max
	 *                Maximum value of the number, exclusive.
	 * @return A random double floating point with a uniform distribution between
	 *         two numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default double generateUniformDouble(final double min, final double max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		return min + this.generateUniformDouble(max - min);
	}

	/**
	 * Fills an array with the results of generateUniformDouble(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformDouble(double,
	 *         double).
	 * @see generateUniformDouble(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateUniformDoubles(final double min, final double max, final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateUniformDouble(min, max);
		}
		return result;
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
	public default double generateBatesDouble(final int number) {
		double res = 0;
		for (int i = 0; i < number; ++i) {
			res += this.generateUniformDouble();
		}
		res /= number;
		return res;
	}

	/**
	 * Fills an array with the results of generateBatesDouble(int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBatesDouble(int).
	 * @see generateBatesDouble(int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateBatesDoubles(final int number, final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBatesDouble(number);
		}
		return result;
	}

	/**
	 * Get a random double floating point with a Bates distribution between 0 and a
	 * number.
	 *
	 * @param max
	 *                   Maximum value of the number, exclusive.
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random double floating point with a Bates distribution between 0
	 *         and a number.
	 * @throws IllegalArgumentException
	 *                                      If max is not larger than 0.
	 */
	public default double generateBatesDouble(final double max, final int number) {
		if (0 >= max) {
			throw new IllegalArgumentException();
		}
		double res = 0;
		for (int i = 0; i < number; ++i) {
			res += this.generateUniformDouble(max);
		}
		res /= number;
		return res;
	}

	/**
	 * Fills an array with the results of generateBatesDouble(double, int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBatesDouble(double, int).
	 * @see generateBatesDouble(double, int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateBatesDoubles(final double max, final int number, final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBatesDouble(max, number);
		}
		return result;
	}

	/**
	 * Get a random double floating point with a Bates distribution between two
	 * numbers.
	 *
	 * @param min
	 *                   Minimum value of the number, inclusive.
	 * @param max
	 *                   Maximum value of the number, exclusive.
	 * @param number
	 *                   Number of random numbers generated to create this result.
	 * @return A random double floating point with a Bates distribution between two
	 *         numbers.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than max.
	 */
	public default double generateBatesDouble(final double min, final double max, final int number) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		double res = 0;
		for (int i = 0; i < number; ++i) {
			res += this.generateUniformDouble(min, max);
		}
		res /= number;
		return res;
	}

	/**
	 * Fills an array with the results of generateBatesDouble(double, double, int).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBatesDouble(double,
	 *         double, int).
	 * @see generateBatesDouble(double, double, int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateBatesDoubles(final double min, final double max, final int number, final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBatesDouble(min, max, number);
		}
		return result;
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
	public default double generateNormalDouble(final double mean, final double standardDeviation) {
		double random1;
		double random2;
		double squareSum;
		do {
			random1 = this.generateUniformDouble(-1, 1);
			random2 = this.generateUniformDouble(-1, 1);
			squareSum = (random1 * random1) + (random2 * random2);
		} while ((squareSum >= 1) || (squareSum == 0));
		return (standardDeviation * random1 * Math.sqrt((-2 * Math.log(squareSum)) / squareSum)) + mean;
	}

	/**
	 * Fills an array with the results of generateNormalDouble(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateNormalDouble(double,
	 *         double).
	 * @see generateNormalDouble(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateNormalDoubles(final double mean, final double standardDeviation, final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateNormalDouble(mean, standardDeviation);
		}
		return result;
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
	public default double generateLognormalDouble(final double logMean, final double logStandardDeviation) {
		return Math.exp(this.generateNormalDouble(logMean, logStandardDeviation));
	}

	/**
	 * Fills an array with the results of generateLognormalDouble(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateLognormalDouble(double,
	 *         double).
	 * @see generateLognormalDouble(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateLognormalDoubles(final double logMean, final double logStandardDeviation,
			final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateLognormalDouble(logMean, logStandardDeviation);
		}
		return result;
	}

	/**
	 * Get a random double floating point with a triangular distribution.
	 *
	 * @param min
	 *                 Minimum value of the number, inclusive.
	 * @param max
	 *                 Maximum value of the number, exclusive.
	 * @param mode
	 *                 Most common value of the number.
	 * @return A random double floating point with a triangular distribution.
	 * @throws IllegalArgumentException
	 *                                      If min is not smaller than or equal to
	 *                                      mode and mode is not smaller than max.
	 */
	public default double generateTriangularDouble(final double min, final double max, final double mode) {
		if (!((min <= mode) && (mode <= max))) {
			throw new IllegalArgumentException();
		}
		final double uniformDouble = this.generateUniformDouble(min, max);
		if (uniformDouble < mode) {
			return (min + Math.sqrt(uniformDouble * (mode - min)));
		} else {
			return max - Math.sqrt((max - uniformDouble) * (max - mode));
		}
	}

	/**
	 * Fills an array with the results of generateTriangularDouble(double, double,
	 * double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateTriangularDouble(double,
	 *         double, double).
	 * @see generateTriangularDouble(double, double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateTriangularDoubles(final double min, final double max, final double mode,
			final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateTriangularDouble(min, max, mode);
		}
		return result;
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
	public default double generateParetoDouble(final double shape, final double scale) {
		if ((0 >= shape) || (0 >= scale)) {
			throw new IllegalArgumentException();
		}
		double random;
		do {
			random = this.generateUniformDouble();
		} while (random == 0);
		return scale / Math.pow(random, 1 / shape);
	}

	/**
	 * Fills an array with the results of generateParetoDouble(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateParetoDouble(double,
	 *         double).
	 * @see generateParetoDouble(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateParetoDoubles(final double shape, final double scale, final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateParetoDouble(shape, scale);
		}
		return result;
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
	public default double generateWeibullDouble(final double shape, final double scale) {
		if ((0 >= shape) || (0 >= scale)) {
			throw new IllegalArgumentException();
		}
		double random;
		do {
			random = this.generateUniformDouble();
		} while (random == 0);
		return scale * Math.pow(-Math.log(random), 1 / shape);
	}

	/**
	 * Fills an array with the results of generateWeibullDouble(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateWeibullDouble(double,
	 *         double).
	 * @see generateWeibullDouble(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateWeibullDoubles(final double shape, final double scale, final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateWeibullDouble(shape, scale);
		}
		return result;
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
	public default double generateExponentialDouble(final double scale) {
		double uniform;
		do {
			uniform = this.generateUniformDouble();
		} while (uniform == 0);
		return Math.log(uniform) * scale;
	}

	/**
	 * Fills an array with the results of generateExponentialDouble(double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of
	 *         generateExponentialDouble(double).
	 * @see generateExponentialDouble(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateExponentialDoubles(final double scale, final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateExponentialDouble(scale);
		}
		return result;
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
	public default double generateGammaDouble(final double shape, final double scale) {
		if ((0 >= shape) || (0 >= scale)) {
			throw new IllegalArgumentException();
		}
		if (shape > 1) {
			final double d = shape - (1.0d / 3.0d);
			final double c = 1 / Math.sqrt(9 * d);
			double v;
			double dv;
			double uniform;
			double normal;
			do {
				normal = this.generateNormalDouble(0, 1);
				v = (1 + (c * normal));
				v = v * v * v;
				dv = d * v;
				// Generate uniform in (0, 1]
				do {
					uniform = this.generateUniformDouble();
				} while (uniform == 0);
			} while ((v <= 0) || (Math.log(uniform) >= ((((0.5 * normal * normal) + d) - dv) + (d * Math.log(v)))));
			return dv * scale;
		} else if (shape == 1) {
			return this.generateExponentialDouble(scale);
		} else {
			double uniform;
			do {
				uniform = this.generateUniformDouble();
			} while (uniform == 0);
			return this.generateGammaDouble(shape + 1, scale) * Math.pow(uniform, 1 / shape);
		}
	}

	/**
	 * Fills an array with the results of generateGammaDouble(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateGammaDouble(double,
	 *         double).
	 * @see generateGammaDouble(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateGammaDoubles(final double shape, final double scale, final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateGammaDouble(shape, scale);
		}
		return result;
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
	public default double generateBetaDouble(final double shapeAlpha, final double shapeBeta) {
		final double gamma = this.generateGammaDouble(shapeAlpha, 1.0);
		if (gamma == 0) {
			return 0;
		} else {
			return gamma / (gamma + this.generateGammaDouble(shapeBeta, 1.0));
		}
	}

	/**
	 * Fills an array with the results of generateBetaDouble(double, double).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBetaDouble(double,
	 *         double).
	 * @see generateBetaDouble(double, double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateBetaDouble(final double shapeAlpha, final double shapeBeta, final int size) {
		final double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateBetaDouble(shapeAlpha, shapeBeta);
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Character methods

	/**
	 * Gets a random character from the given String.
	 *
	 * @param alphabet
	 *                     A string of characters this method will pick from.
	 * @return A random character from the given String.
	 */
	public default char generateCharacter(final String alphabet) {
		return alphabet.charAt(this.generateUniformInteger(alphabet.length()));
	}

	/**
	 * Fills an array with the results of generateCharacter(String).
	 *
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateCharacter(String).
	 * @see generateCharacter(String)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default char[] generateCharacters(final String alphabet, final int size) {
		final char[] result = new char[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.generateCharacter(alphabet);
		}
		return result;
	}

	/**
	 * Gets a random String from characters picked from the given String.
	 *
	 * @param alphabet
	 *                     A string of characters this method will pick from.
	 * @return A random String from characters picked from the given String.
	 */
	public default String generateString(final String alphabet, final int lenght) {
		final StringBuilder stringBuilder = new StringBuilder(lenght);
		for (int i = 0; i < lenght; ++i) {
			stringBuilder.append(this.generateCharacter(alphabet));
		}
		return stringBuilder.toString();
	}

	////////////////////////////////////////////////////////////////////////////////
	// Array methods

	/**
	 * Get a random element from an array.
	 *
	 * @param elements
	 *                     An array of elements.
	 * @return A random element from an array.
	 */
	public default <T> T pick(final T[] elements) {
		final int index = this.generateUniformInteger(elements.length);
		return elements[index];
	}

	/**
	 * Shuffle the elements in an array randomly.
	 *
	 * @param elements
	 *                     An array of elements.
	 * @return A shuffled array.
	 */
	public default <T> T[] shuffle(final T[] elements) {
		for (int i = 0; i < elements.length; ++i) {
			final int j = this.generateUniformInteger(elements.length);
			final T temporary = elements[i];
			elements[i] = elements[j];
			elements[j] = temporary;
		}
		return elements;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Collection methods

	/**
	 * Get a random element from a list.
	 *
	 * @param elements
	 *                     A list of elements.
	 * @return A random element from a list.
	 */
	public default <T> T pick(final List<T> elements) {
		final int index = this.generateUniformInteger(elements.size());
		return elements.get(index);
	}

	/**
	 * Shuffle the elements in a list randomly.
	 *
	 * @param elements
	 *                     A list of elements.
	 * @return A shuffled list.
	 */
	public default <T> List<T> shuffle(final List<T> elements) {
		for (int i = 0; i < elements.size(); ++i) {
			final int j = this.generateUniformInteger(elements.size());
			final T temporary = elements.get(i);
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
	public default <T> T pick(final Set<T> elements) {
		int index = this.generateUniformInteger(elements.size());
		final Iterator<T> iterator = elements.iterator();
		while (index > 0) {
			iterator.next();
			--index;
		}
		return iterator.next();
	}

}
