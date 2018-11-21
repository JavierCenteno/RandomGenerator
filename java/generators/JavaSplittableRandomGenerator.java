package generators;

import java.util.SplittableRandom;

import api.RandomGenerator;
import util.ByteConverter;

/**
 * A wrapper for Java's SplittableRandom PRNG.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @see java.util.SplittableRandom
 * @since 1.0
 * 
 */
public class JavaSplittableRandomGenerator implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Class fields

	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = 8;

	// -----------------------------------------------------------------------------
	// Instance fields

	/**
	 * Underlying SplittableRandom object of this generator.
	 * 
	 * @see java.util.SplittableRandom
	 */
	private SplittableRandom splittableRandom;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator using SplittableRandom's no argument constructor.
	 * 
	 * @see java.util.SplittableRandom
	 */
	public JavaSplittableRandomGenerator() {
		this.splittableRandom = new SplittableRandom();
	}

	/**
	 * Constructs a generator using SplittableRandom's seed-based constructor.
	 * 
	 * @param seed
	 *                 A seed.
	 * @see java.util.SplittableRandom
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public JavaSplittableRandomGenerator(byte[] seed) {
		if (seed.length < SEED_SIZE) {
			throw new IllegalArgumentException();
		}
		this.splittableRandom = new SplittableRandom(ByteConverter.bytesToLong(seed));
	}

	/**
	 * Constructs a generator using the given SplittableRandom.
	 * 
	 * @param splittableRandom
	 *                             A SplittableRandom object.
	 * @see java.util.SplittableRandom
	 */
	public JavaSplittableRandomGenerator(SplittableRandom splittableRandom) {
		this.splittableRandom = splittableRandom;
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public int getSeedSize() {
		return SEED_SIZE;
	}

	public JavaSplittableRandomGenerator split() {
		return new JavaSplittableRandomGenerator(this.splittableRandom.split());
	}

	@Override
	public void setSeed(byte[] seed) {
		if (seed.length < SEED_SIZE) {
			throw new IllegalArgumentException();
		}
		this.splittableRandom = new SplittableRandom(ByteConverter.bytesToLong(seed));
	}

	@Override
	public int generateUniformInteger() {
		return this.splittableRandom.nextInt();
	}

	@Override
	public long generateUniformLong() {
		return this.splittableRandom.nextLong();
	}

}
