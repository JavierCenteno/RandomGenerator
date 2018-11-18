package generators;

import java.security.SecureRandom;

import api.RandomGenerator;

/**
 * Implementation of a permuted congruential generator XSH-RS PRNG. This
 * generator has a state of 64 bits and produces 32 random bits at a time. It is
 * slightly faster than its counterpart XSL-RR but produces worse results.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @see generators.PermutedCongruentialGeneratorXSHRR
 * @since 1.0
 * 
 */
public class PermutedCongruentialGeneratorXSHRS implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Class fields

	private static final long MULTIPLIER = 6364136223846793005L;

	// -----------------------------------------------------------------------------
	// Instance fields

	private long state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public PermutedCongruentialGeneratorXSHRS() {
		setSeed(SecureRandom.getSeed(8));
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 */
	public PermutedCongruentialGeneratorXSHRS(byte[] seed) {
		setSeed(seed);
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public void setSeed(byte[] seed) {
		setState(seed);
		this.state = 2 * this.state + 1;
		generateUniformInteger();
	}

	@Override
	public byte[] getState() {
		byte _0 = (byte) (state >>> 56);
		byte _1 = (byte) (state >>> 48);
		byte _2 = (byte) (state >>> 40);
		byte _3 = (byte) (state >>> 32);
		byte _4 = (byte) (state >>> 24);
		byte _5 = (byte) (state >>> 16);
		byte _6 = (byte) (state >>> 8);
		byte _7 = (byte) (state);
		return new byte[] { _0, _1, _2, _3, _4, _5, _6, _7 };
	}

	@Override
	public void setState(byte[] state) {
		long _0 = ((long) state[0]) << 56;
		long _1 = ((long) state[1]) << 48;
		long _2 = ((long) state[2]) << 40;
		long _3 = ((long) state[3]) << 32;
		long _4 = ((long) state[4]) << 24;
		long _5 = ((long) state[5]) << 16;
		long _6 = ((long) state[6]) << 8;
		long _7 = (long) state[7];
		this.state = (_0 | _1 | _2 | _3 | _4 | _5 | _6 | _7);
	}

	@Override
	public int generateUniformInteger() {
		long a = state;
		int count = (int) (a >>> 61);
		state = a * MULTIPLIER;
		a ^= a >>> 22;
		return (int) (a >>> (22 + count));
	}

	@Override
	public long generateUniformLong() {
		long _0 = generateUniformInteger();
		long _1 = generateUniformInteger();
		return _0 << 32 | _1;
	}

}
