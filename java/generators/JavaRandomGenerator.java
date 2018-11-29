package generators;

import java.util.Random;

import api.RandomGenerator32;
import util.ByteConverter;

/**
 * A wrapper for Java's Random PRNG.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @see java.util.Random
 * @since 1.0
 * 
 */
public class JavaRandomGenerator implements RandomGenerator32 {

	// -----------------------------------------------------------------------------
	// Class fields

	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = 8;

	// -----------------------------------------------------------------------------
	// Instance fields

	/**
	 * Underlying Random object of this generator.
	 * 
	 * @see java.util.Random
	 */
	private Random random;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator using Random's no argument constructor.
	 * 
	 * @see java.util.Random
	 */
	public JavaRandomGenerator() {
		this.random = new Random();
	}

	/**
	 * Constructs a generator using Random's seed-based constructor.
	 * 
	 * @param seed
	 *                 A seed.
	 * @see java.util.Random
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public JavaRandomGenerator(byte[] seed) {
		if (seed.length < SEED_SIZE) {
			throw new IllegalArgumentException();
		}
		this.random = new Random(ByteConverter.bytesToLong(seed));
	}

	/**
	 * Constructs a generator using the given Random.
	 * 
	 * @param random
	 *                   A Random object.
	 * @see java.util.Random
	 */
	public JavaRandomGenerator(Random random) {
		this.random = random;
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public int getSeedSize() {
		return SEED_SIZE;
	}

	@Override
	public Random getRandom() {
		return this.random;
	}

	@Override
	public void setSeed(byte[] seed) {
		if (seed.length < SEED_SIZE) {
			throw new IllegalArgumentException();
		}
		this.random.setSeed(ByteConverter.bytesToLong(seed));
	}

	@Override
	public int generateUniformInteger() {
		return this.random.nextInt();
	}

	@Override
	public long generateUniformLong() {
		return this.random.nextLong();
	}

}
