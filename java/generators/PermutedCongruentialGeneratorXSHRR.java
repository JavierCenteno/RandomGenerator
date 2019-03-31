package generators;

import api.RandomGenerator;
import api.RandomGenerator32;
import util.ByteConverter;

/**
 * Implementation of a permuted congruential generator XSH-RR PRNG. This
 * generator has a state of 64 bits and produces 32 random bits at a time. This
 * is the recommended permuted congruential generator recommended for most
 * cases. It is slightly slower than its counterpart XSH-RS but produces better
 * results.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @see generators.PermutedCongruentialGeneratorXSHRS
 * @since 1.0
 *
 */
public class PermutedCongruentialGeneratorXSHRR implements RandomGenerator32 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 8;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = PermutedCongruentialGeneratorXSHRR.STATE_SIZE;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * State of this generator.
	 */
	private long state;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public PermutedCongruentialGeneratorXSHRR() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(PermutedCongruentialGeneratorXSHRR.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public PermutedCongruentialGeneratorXSHRR(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return PermutedCongruentialGeneratorXSHRR.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return PermutedCongruentialGeneratorXSHRR.STATE_SIZE;
	}

	@Override
	public void setSeed(final byte[] seed) {
		this.setState(seed);
		this.state += 1442695040888963407L;
		this.generateUniformInteger();
	}

	@Override
	public byte[] getState() {
		return ByteConverter.longToBytes(this.state);
	}

	@Override
	public void setState(final byte[] state) {
		if (state.length < PermutedCongruentialGeneratorXSHRR.STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = ByteConverter.bytesToLong(state);
	}

	@Override
	public int generateUniformInteger() {
		long a = this.state;
		final int count = (int) (a >>> 59);
		this.state = (a * 6364136223846793005L) + 1442695040888963407L;
		a ^= a >>> 18;
		final int b = (int) (a >>> 27);
		return (b >>> count) | (b << (-count & 0b11111));
	}

}
