package generators;

import java.security.SecureRandom;

import api.RandomGenerator;
import util.ByteConverter;

/**
 * Implementation of a permuted congruential generator XSH-RR PRNG. This
 * generator has a state of 64 bits and produces 32 random bits at a time. This
 * is the recommended permuted congruential generator recommended for most
 * cases. It is slightly slower than its counterpart XSL-RS but produces better
 * results.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @see generators.PermutedCongruentialGeneratorXSHRS
 * @since 1.0
 * 
 */
public class PermutedCongruentialGeneratorXSHRR implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Class fields

	private static final long MULTIPLIER = 6364136223846793005L;
	private static final long INCREMENT = 1442695040888963407L;

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
	public PermutedCongruentialGeneratorXSHRR() {
		setSeed(SecureRandom.getSeed(8));
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 */
	public PermutedCongruentialGeneratorXSHRR(byte[] seed) {
		setSeed(seed);
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public void setSeed(byte[] seed) {
		setState(seed);
		this.state += INCREMENT;
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
		int count = (int) (a >>> 59);
		state = a * MULTIPLIER + INCREMENT;
		a ^= a >>> 18;
		int b = (int) (a >>> 27);
		return (b >>> count) | (b << (-count & 0b11111));
	}

	@Override
	public long generateUniformLong() {
		long int0 = generateUniformInteger() & 0x00000000FFFFFFFFL;
		long int1 = generateUniformInteger() & 0x00000000FFFFFFFFL;
		return int0 << 32 | int1;
	}

}
