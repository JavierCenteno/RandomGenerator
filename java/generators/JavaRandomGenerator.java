package generators;

import java.util.Random;

import api.RandomGenerator;
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
public class JavaRandomGenerator implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance fields

	private Random random;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator using Random's no argument constructor.
	 * 
	 * @see java.util.Random
	 */
	public JavaRandomGenerator() {
		random = new Random();
	}

	/**
	 * Constructs a generator using Random's seed-based constructor.
	 * 
	 * @see java.util.Random
	 */
	public JavaRandomGenerator(byte[] seed) {
		random = new Random(ByteConverter.bytesToLong(seed));
	}

	/**
	 * Constructs a generator using the given Random.
	 * 
	 * @see java.util.Random
	 */
	public JavaRandomGenerator(Random random) {
		this.random = random;
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public Random getRandom() {
		return random;
	}

	@Override
	public void setSeed(byte[] seed) {
		random.setSeed(ByteConverter.bytesToLong(seed));
	}

	@Override
	public int generateUniformInteger() {
		return random.nextInt();
	}

	@Override
	public long generateUniformLong() {
		return random.nextLong();
	}

}
