package generators;

import java.util.Random;

import api.RandomGenerator;

/**
 * A wrapper for Java's Random.
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
		long _0 = ((long) seed[0]) << 56;
		long _1 = ((long) seed[1]) << 48;
		long _2 = ((long) seed[2]) << 40;
		long _3 = ((long) seed[3]) << 32;
		long _4 = ((long) seed[4]) << 24;
		long _5 = ((long) seed[5]) << 16;
		long _6 = ((long) seed[6]) << 8;
		long _7 = (long) seed[7];
		random = new Random(_0 | _1 | _2 | _3 | _4 | _5 | _6 | _7);
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
		long _0 = ((long) seed[0]) << 56;
		long _1 = ((long) seed[1]) << 48;
		long _2 = ((long) seed[2]) << 40;
		long _3 = ((long) seed[3]) << 32;
		long _4 = ((long) seed[4]) << 24;
		long _5 = ((long) seed[5]) << 16;
		long _6 = ((long) seed[6]) << 8;
		long _7 = (long) seed[7];
		random.setSeed(_0 | _1 | _2 | _3 | _4 | _5 | _6 | _7);
	}

	@Override
	public byte[] getState() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setState(byte[] state) {
		throw new UnsupportedOperationException();
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
