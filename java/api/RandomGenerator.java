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
			ByteConverter.longsToBytes(
					new long[] { System.nanoTime(), System.nanoTime() * 6364136223846793005L + 1442695040888963407L }));

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

			public RandomWrapper(RandomGenerator source) {
				this.source = source;
			}

			////////////////////////////////////////////////////////////////////////////////
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
	public default void setSeed(byte[] seed) {
		setState(seed);
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
	public default void setState(byte[] state) {
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

	////////////////////////////////////////////////////////////////////////////////
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
	 * Fills an array with the results of generateBoolean().
	 * 
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBoolean().
	 * @see generateBoolean()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default boolean[] generateBooleans(int size) {
		boolean[] result = new boolean[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBoolean();
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
	public default boolean generateBoolean(float probability) {
		return generateUniformFloat() < probability;
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
	public default boolean[] generateBooleans(float probability, int size) {
		boolean[] result = new boolean[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBoolean(probability);
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
	public default boolean generateBoolean(double probability) {
		return generateUniformDouble() < probability;
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
	public default boolean[] generateBooleans(double probability, int size) {
		boolean[] result = new boolean[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBoolean(probability);
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Byte methods

	/**
	 * @see RandomGenerator#generateUniformByte()
	 */
	public default byte generateByte() {
		return generateUniformByte();
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
	public default byte[] generateBytes(int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateByte();
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformByte(byte)
	 */
	public default byte generateByte(byte max) {
		return generateUniformByte(max);
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
	public default byte[] generateBytes(byte max, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateByte(max);
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformByte(byte, byte)
	 */
	public default byte generateByte(byte min, byte max) {
		return generateUniformByte(min, max);
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
	public default byte[] generateBytes(byte min, byte max, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateByte(min, max);
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
	public default byte[] generateUniformBytes(int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformByte();
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
	public default byte generateUniformByte(byte max) {
		return (byte) generateUniformShort(max);
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
	public default byte[] generateUniformBytes(byte max, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformByte(max);
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
	public default byte generateUniformByte(byte min, byte max) {
		return (byte) generateUniformShort(min, max);
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
	public default byte[] generateUniformBytes(byte min, byte max, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformByte(min, max);
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
	public default byte generateBatesByte(byte min, byte max, int number) {
		return (byte) generateBatesShort(min, max, number);
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
	public default byte[] generateBatesBytes(byte min, byte max, int number, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBatesByte(min, max, number);
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
	public default byte generateNormalByte(double mean, double standardDeviation) {
		return (byte) generateNormalShort(mean, standardDeviation);
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
	public default byte[] generateNormalBytes(double mean, double standardDeviation, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateNormalByte(mean, standardDeviation);
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
	public default byte generateLognormalByte(double logMean, double logStandardDeviation) {
		return (byte) generateLognormalShort(logMean, logStandardDeviation);
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
	public default byte[] generateLognormalBytes(double logMean, double logStandardDeviation, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateLognormalByte(logMean, logStandardDeviation);
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
	public default byte generateTriangularByte(byte min, byte max, byte mode) {
		return (byte) generateTriangularShort(min, max, mode);
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
	public default byte[] generateTriangularBytes(byte min, byte max, byte mode, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateTriangularByte(min, max, mode);
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
	public default byte generateParetoByte(double shape, double scale) {
		return (byte) generateParetoShort(shape, scale);
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
	public default byte[] generateParetoBytes(double shape, double scale, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateParetoByte(shape, scale);
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
	public default byte generateWeibullByte(double shape, double scale) {
		return (byte) generateWeibullShort(shape, scale);
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
	public default byte[] generateWeibullBytes(double shape, double scale, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateWeibullByte(shape, scale);
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
	public default byte generateExponentialByte(double scale) {
		return (byte) generateExponentialShort(scale);
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
	public default byte[] generateExponentialBytes(double scale, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateExponentialByte(scale);
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
	public default byte generateGammaByte(double shape, double scale) {
		return (byte) generateGammaShort(shape, scale);
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
	public default byte[] generateGammaBytes(double shape, double scale, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateGammaByte(shape, scale);
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
	public default byte generateBetaByte(double shapeAlpha, double shapeBeta) {
		return (byte) generateBetaShort(shapeAlpha, shapeBeta);
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
	public default byte[] generateBetaBytes(double shapeAlpha, double shapeBeta, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBetaByte(shapeAlpha, shapeBeta);
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
	public default byte generatePoissonByte(double rate) {
		return (byte) generatePoissonShort(rate);
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
	public default byte[] generatePoissonBytes(double rate, int size) {
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = generatePoissonByte(rate);
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Short integer methods

	/**
	 * @see RandomGenerator#generateUniformShort()
	 */
	public default short generateShort() {
		return generateUniformShort();
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
	public default short[] generateShorts(int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateShort();
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformShort(short)
	 */
	public default short generateShort(short max) {
		return generateUniformShort(max);
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
	public default short[] generateShorts(short max, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateShort(max);
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformShort(short, short)
	 */
	public default short generateShort(short min, short max) {
		return generateUniformShort(min, max);
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
	public default short[] generateShorts(short min, short max, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateShort(min, max);
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
	public default short[] generateUniformShorts(int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformShort();
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
	public default short generateUniformShort(short max) {
		return (short) generateUniformInteger(max);
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
	public default short[] generateUniformShorts(short max, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformShort(max);
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
	public default short generateUniformShort(short min, short max) {
		return (short) generateUniformInteger(min, max);
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
	public default short[] generateUniformShorts(short min, short max, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformShort(min, max);
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
	public default short generateBatesShort(short min, short max, int number) {
		return (short) generateBatesInteger(min, max, number);
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
	public default short[] generateBatesShorts(short min, short max, int number, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBatesShort(min, max, number);
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
	public default short generateNormalShort(double mean, double standardDeviation) {
		return (short) generateNormalInteger(mean, standardDeviation);
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
	public default short[] generateNormalShorts(double mean, double standardDeviation, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateNormalShort(mean, standardDeviation);
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
	public default short generateLognormalShort(double logMean, double logStandardDeviation) {
		return (short) generateLognormalInteger(logMean, logStandardDeviation);
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
	public default short[] generateLognormalShorts(double mean, double standardDeviation, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateLognormalShort(mean, standardDeviation);
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
	public default short generateTriangularShort(short min, short max, short mode) {
		return (short) generateTriangularInteger(min, max, mode);
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
	public default short[] generateTriangularShorts(short min, short max, short mode, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateTriangularShort(min, max, mode);
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
	public default short generateParetoShort(double shape, double scale) {
		return (short) generateParetoInteger(shape, scale);
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
	public default short[] generateParetoShorts(double shape, double scale, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateParetoShort(shape, scale);
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
	public default short generateWeibullShort(double shape, double scale) {
		return (short) generateWeibullInteger(shape, scale);
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
	public default short[] generateWeibullShorts(double shape, double scale, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateWeibullShort(shape, scale);
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
	public default short generateExponentialShort(double scale) {
		return (short) generateExponentialInteger(scale);
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
	public default short[] generateExponentialShorts(double scale, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateExponentialShort(scale);
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
	public default short generateGammaShort(double shape, double scale) {
		return (short) generateGammaInteger(shape, scale);
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
	public default short[] generateGammaShorts(double shape, double scale, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateGammaShort(shape, scale);
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
	public default short generateBetaShort(double shapeAlpha, double shapeBeta) {
		return (short) generateBetaInteger(shapeAlpha, shapeBeta);
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
	public default short[] generateBetaShorts(double shapeAlpha, double shapeBeta, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBetaShort(shapeAlpha, shapeBeta);
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
	public default short generatePoissonShort(double rate) {
		return (short) generatePoissonInteger(rate);
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
	public default short[] generatePoissonShorts(double rate, int size) {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = generatePoissonShort(rate);
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Integer methods

	/**
	 * @see RandomGenerator#generateUniformInteger()
	 */
	public default int generateInteger() {
		return generateUniformInteger();
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
	public default int[] generateIntegers(int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateInteger();
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformInteger(int)
	 */
	public default int generateInteger(int max) {
		return generateUniformInteger(max);
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
	public default int[] generateIntegers(int max, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateInteger(max);
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformInteger(int, int)
	 */
	public default int generateInteger(int min, int max) {
		return generateUniformInteger(min, max);
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
	public default int[] generateIntegers(int min, int max, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateInteger(min, max);
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
	public default int[] generateUniformIntegers(int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformInteger();
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
	public default int generateUniformInteger(int max) {
		return (int) generateUniformLong(max);
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
	public default int[] generateUniformIntegers(int max, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformInteger(max);
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
	public default int generateUniformInteger(int min, int max) {
		return (int) generateUniformLong(min, max);
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
	public default int[] generateUniformIntegers(int min, int max, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformInteger(min, max);
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
	public default int generateBatesInteger(int min, int max, int number) {
		return (int) generateBatesLong(min, max, number);
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
	public default int[] generateBatesIntegers(int min, int max, int number, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBatesInteger(min, number, max);
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
	public default int generateNormalInteger(double mean, double standardDeviation) {
		return (int) generateNormalLong(mean, standardDeviation);
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
	public default int[] generateNormalIntegers(double mean, double standardDeviation, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateNormalInteger(mean, standardDeviation);
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
	public default int generateLognormalInteger(double logMean, double logStandardDeviation) {
		return (int) generateLognormalLong(logMean, logStandardDeviation);
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
	public default int[] generateLognormalIntegers(double logMean, double logStandardDeviation, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateLognormalInteger(logMean, logStandardDeviation);
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
	public default int generateTriangularInteger(int min, int max, int mode) {
		return (int) generateTriangularLong(min, max, mode);
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
	public default int[] generateTriangularIntegers(int min, int max, int mode, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateTriangularInteger(min, max, mode);
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
	public default int generateParetoInteger(double shape, double scale) {
		return (int) generateParetoLong(shape, scale);
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
	public default int[] generateParetoIntegers(double shape, double scale, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateParetoInteger(shape, scale);
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
	public default int generateWeibullInteger(double shape, double scale) {
		return (int) generateWeibullLong(shape, scale);
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
	public default int[] generateWeibullIntegers(double shape, double scale, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateWeibullInteger(shape, scale);
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
	public default int generateExponentialInteger(double scale) {
		return (int) generateExponentialLong(scale);
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
	public default int[] generateExponentialIntegers(double scale, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateExponentialInteger(scale);
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
	public default int generateGammaInteger(double shape, double scale) {
		return (int) generateGammaLong(shape, scale);
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
	public default int[] generateGammaIntegers(double shape, double scale, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateGammaInteger(shape, scale);
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
	public default int generateBetaInteger(double shapeAlpha, double shapeBeta) {
		return (int) generateBetaLong(shapeAlpha, shapeBeta);
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
	public default int[] generateBetaIntegers(double shapeAlpha, double shapeBeta, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBetaInteger(shapeAlpha, shapeBeta);
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
	public default int generatePoissonInteger(double rate) {
		return (int) generatePoissonLong(rate);
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
	public default int[] generatePoissonIntegers(double rate, int size) {
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[i] = generatePoissonInteger(rate);
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Long integer methods

	/**
	 * @see RandomGenerator#generateUniformLong()
	 */
	public default long generateLong() {
		return generateUniformLong();
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
	public default long[] generateLongs(int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateLong();
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformLong(long)
	 */
	public default long generateLong(long max) {
		return generateUniformLong(max);
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
	public default long[] generateLongs(long max, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateLong(max);
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformLong(long, long)
	 */
	public default long generateLong(long min, long max) {
		return generateUniformLong(min, max);
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
	public default long[] generateLongs(long min, long max, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateLong(min, max);
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
	public default long[] generateUniformLongs(int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformLong();
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
	 * Fills an array with the results of generateUniformLong(long).
	 * 
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformLong(long).
	 * @see generateUniformLong(long)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default long[] generateUniformLongs(long max, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformLong(max);
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
	public default long generateUniformLong(long min, long max) {
		if (min >= max) {
			throw new IllegalArgumentException();
		}
		return generateUniformLong(max - min) + min;
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
	public default long[] generateUniformLongs(long min, long max, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformLong(min, max);
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
	public default long[] generateBatesLongs(long min, long max, int number, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBatesLong(min, max, number);
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
	public default long generateNormalLong(double mean, double standardDeviation) {
		return Math.round(generateNormalDouble(mean, standardDeviation));
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
	public default long[] generateNormalLongs(double mean, double standardDeviation, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateNormalLong(mean, standardDeviation);
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
	public default long generateLognormalLong(double logMean, double logStandardDeviation) {
		return (long) generateLognormalDouble(logMean, logStandardDeviation);
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
	public default long[] generateLognormalLongs(double logMean, double logStandardDeviation, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateLognormalLong(logMean, logStandardDeviation);
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
	public default long generateTriangularLong(long min, long max, long mode) {
		double doubleMin = (double) min;
		double doubleMax = (double) max;
		double doubleMode = (double) mode;
		// Improve the distribution when switching between long and double
		doubleMode = doubleMode * (doubleMax / (doubleMax - 1));
		return (long) (generateTriangularDouble(doubleMin, doubleMax, doubleMode));
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
	public default long[] generateTriangularLongs(long min, long max, long mode, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateTriangularLong(min, max, mode);
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
	public default long generateParetoLong(double shape, double scale) {
		return (long) generateParetoDouble(shape, scale);
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
	public default long[] generateParetoLongs(double shape, double scale, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateParetoLong(shape, scale);
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
	public default long generateWeibullLong(double shape, double scale) {
		return (long) generateWeibullDouble(shape, scale);
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
	public default long[] generateWeibullLongs(double shape, double scale, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateWeibullLong(shape, scale);
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
	public default long generateExponentialLong(double scale) {
		return (long) generateExponentialDouble(scale);
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
	public default long[] generateExponentialLongs(double scale, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateExponentialLong(scale);
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
	public default long generateGammaLong(double shape, double scale) {
		return (long) generateGammaDouble(shape, scale);
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
	public default long[] generateGammaLongs(double shape, double scale, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateGammaLong(shape, scale);
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
	public default long generateBetaLong(double shapeAlpha, double shapeBeta) {
		return (long) generateBetaDouble(shapeAlpha, shapeBeta);
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
	public default long[] generateBetaLongs(double shapeAlpha, double shapeBeta, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBetaLong(shapeAlpha, shapeBeta);
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
	public default long[] generatePoissonLongs(double rate, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = generatePoissonLong(rate);
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Floating point methods

	/**
	 * @see RandomGenerator#generateUniformFloat()
	 */
	public default float generateFloat() {
		return generateUniformFloat();
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
	public default float[] generateFloats(int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateFloat();
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformFloat(float)
	 */
	public default float generateFloat(float max) {
		return generateUniformFloat(max);
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
	public default float[] generateFloats(float max, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateFloat(max);
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformFloat(float, float)
	 */
	public default float generateFloat(float min, float max) {
		return generateUniformFloat(min, max);
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
	public default float[] generateFloats(float min, float max, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateFloat(min, max);
		}
		return result;
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
	 * Fills an array with the results of generateUniformFloat().
	 * 
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformFloat().
	 * @see generateUniformFloat()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateUniformFloats(int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformFloat();
		}
		return result;
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
	 * Fills an array with the results of generateUniformFloat(float).
	 * 
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformFloat(float).
	 * @see generateUniformFloat(float)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateUniformFloats(float max, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformFloat(max);
		}
		return result;
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
	public default float[] generateUniformFloats(float min, float max, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformFloat(min, max);
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
	public default float generateBatesFloat(int number) {
		float res = 0;
		for (int i = 0; i < number; ++i) {
			res += generateUniformFloat();
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
	public default float[] generateBatesFloats(int number, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBatesFloat(number);
		}
		return result;
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
	 * Fills an array with the results of generateBatesFloat(float, int).
	 * 
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBatesFloat(float, int).
	 * @see generateBatesFloat(float, int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default float[] generateBatesFloats(float max, int number, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBatesFloat(max, number);
		}
		return result;
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
	public default float[] generateBatesFloats(float min, float max, int number, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBatesFloat(min, max, number);
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
	public default float generateNormalFloat(double mean, double standardDeviation) {
		return (float) generateNormalDouble(mean, standardDeviation);
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
	public default float[] generateNormalFloats(double mean, double standardDeviation, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateNormalFloat(mean, standardDeviation);
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
	public default float generateLognormalFloat(double logMean, double logStandardDeviation) {
		return (float) generateLognormalDouble(logMean, logStandardDeviation);
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
	public default float[] generateLognormalFloats(double logMean, double logStandardDeviation, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateLognormalFloat(logMean, logStandardDeviation);
		}
		return result;
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
	public default float[] generateTriangularFloats(float min, float max, float mode, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateTriangularFloat(min, max, mode);
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
	public default float generateParetoFloat(double shape, double scale) {
		return (float) generateParetoDouble(shape, scale);
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
	public default float[] generateParetoFloats(double shape, double scale, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateParetoFloat(shape, scale);
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
	public default float generateWeibullFloat(double shape, double scale) {
		return (float) generateWeibullDouble(shape, scale);
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
	public default float[] generateWeibullFloats(double shape, double scale, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateWeibullFloat(shape, scale);
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
	public default float generateExponentialFloat(double scale) {
		return (float) generateExponentialDouble(scale);
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
	public default float[] generateExponentialFloats(double scale, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateExponentialFloat(scale);
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
	public default float generateGammaFloat(double shape, double scale) {
		return (float) generateGammaDouble(shape, scale);
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
	public default float[] generateGammaFloats(double shape, double scale, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateGammaFloat(shape, scale);
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
	public default float generateBetaFloat(double shapeAlpha, double shapeBeta) {
		return (float) generateBetaDouble(shapeAlpha, shapeBeta);
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
	public default float[] generateBetaFloats(double shapeAlpha, double shapeBeta, int size) {
		float[] result = new float[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBetaFloat(shapeAlpha, shapeBeta);
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Double floating point methods

	/**
	 * @see RandomGenerator#generateUniformDouble()
	 */
	public default double generateDouble() {
		return generateUniformDouble();
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
	public default double[] generateDoubles(int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateDouble();
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformDouble(double)
	 */
	public default double generateDouble(double max) {
		return generateUniformDouble(max);
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
	public default double[] generateDoubles(double max, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateDouble(max);
		}
		return result;
	}

	/**
	 * @see RandomGenerator#generateUniformDouble(double, double)
	 */
	public default double generateDouble(double min, double max) {
		return generateUniformDouble(min, max);
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
	public default double[] generateDoubles(double min, double max, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateDouble(min, max);
		}
		return result;
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
	 * Fills an array with the results of generateUniformDouble().
	 * 
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformDouble().
	 * @see generateUniformDouble()
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateUniformDoubles(int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformDouble();
		}
		return result;
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
	 * Fills an array with the results of generateUniformDouble(double).
	 * 
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateUniformDouble(double).
	 * @see generateUniformDouble(double)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateUniformDoubles(double max, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformDouble(max);
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
	public default double[] generateUniformDoubles(double min, double max, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateUniformDouble(min, max);
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
	public default double generateBatesDouble(int number) {
		double res = 0;
		for (int i = 0; i < number; ++i) {
			res += generateUniformDouble();
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
	public default double[] generateBatesDoubles(int number, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBatesDouble(number);
		}
		return result;
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
	 * Fills an array with the results of generateBatesDouble(double, int).
	 * 
	 * @param size
	 *                 The size of the array.
	 * @return An array filled with the results of generateBatesDouble(double, int).
	 * @see generateBatesDouble(double, int)
	 * @throws NegativeArraySizeException
	 *                                        If size is negative.
	 */
	public default double[] generateBatesDoubles(double max, int number, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBatesDouble(max, number);
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
	public default double[] generateBatesDoubles(double min, double max, int number, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBatesDouble(min, max, number);
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
	public default double[] generateNormalDoubles(double mean, double standardDeviation, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateNormalDouble(mean, standardDeviation);
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
	public default double generateLognormalDouble(double logMean, double logStandardDeviation) {
		return Math.exp(generateNormalDouble(logMean, logStandardDeviation));
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
	public default double[] generateLognormalDoubles(double logMean, double logStandardDeviation, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateLognormalDouble(logMean, logStandardDeviation);
		}
		return result;
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
	public default double[] generateTriangularDoubles(double min, double max, double mode, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateTriangularDouble(min, max, mode);
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
	public default double[] generateParetoDoubles(double shape, double scale, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateParetoDouble(shape, scale);
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
	public default double[] generateWeibullDoubles(double shape, double scale, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateWeibullDouble(shape, scale);
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
	public default double generateExponentialDouble(double scale) {
		double uniform;
		do {
			uniform = generateUniformDouble();
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
	public default double[] generateExponentialDoubles(double scale, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateExponentialDouble(scale);
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
	public default double[] generateGammaDoubles(double shape, double scale, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateGammaDouble(shape, scale);
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
	public default double generateBetaDouble(double shapeAlpha, double shapeBeta) {
		double gamma = generateGammaDouble(shapeAlpha, 1.0);
		if (gamma == 0) {
			return 0;
		} else {
			return gamma / (gamma + generateGammaDouble(shapeBeta, 1.0));
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
	public default double[] generateBetaDouble(double shapeAlpha, double shapeBeta, int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateBetaDouble(shapeAlpha, shapeBeta);
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
	public default char generateCharacter(String alphabet) {
		return alphabet.charAt(generateUniformInteger(alphabet.length()));
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
	public default char[] generateCharacters(String alphabet, int size) {
		char[] result = new char[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateCharacter(alphabet);
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
	public default String generateString(String alphabet, int lenght) {
		StringBuilder stringBuilder = new StringBuilder(lenght);
		for (int i = 0; i < lenght; ++i) {
			stringBuilder.append(generateCharacter(alphabet));
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

	////////////////////////////////////////////////////////////////////////////////
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
