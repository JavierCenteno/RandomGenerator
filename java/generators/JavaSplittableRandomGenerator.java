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
	// Instance fields

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
	 * @see java.util.SplittableRandom
	 */
	public JavaSplittableRandomGenerator(byte[] seed) {
		this.splittableRandom = new SplittableRandom(ByteConverter.bytesToLong(seed));
	}

	/**
	 * Constructs a generator using the given SplittableRandom.
	 * 
	 * @see java.util.SplittableRandom
	 */
	public JavaSplittableRandomGenerator(SplittableRandom splittableRandom) {
		this.splittableRandom = splittableRandom;
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public void setSeed(byte[] seed) {
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
