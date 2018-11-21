package generators;

import java.security.SecureRandom;

import api.RandomGenerator;
import util.ByteConverter;

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
		return ByteConverter.longToBytes(this.state);
	}

	@Override
	public void setState(byte[] state) {
		this.state = ByteConverter.bytesToLong(state);
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
		long int0 = generateUniformInteger() & 0x00000000FFFFFFFFL;
		long int1 = generateUniformInteger() & 0x00000000FFFFFFFFL;
		return int0 << 32 | int1;
	}

}
