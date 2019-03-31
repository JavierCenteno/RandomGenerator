package generators;

import api.RandomGenerator;
import api.RandomGenerator32;
import util.ByteConverter;

/**
 * Implementation of a permuted congruential generator XSH-RS PRNG. This
 * generator has a state of 64 bits and produces 32 random bits at a time. It is
 * slightly faster than its counterpart XSH-RR but produces worse results.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @see generators.PermutedCongruentialGeneratorXSHRR
 * @since 1.0
 *
 */
public class PermutedCongruentialGeneratorXSHRS implements RandomGenerator32 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 8;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = PermutedCongruentialGeneratorXSHRS.STATE_SIZE;

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
	public PermutedCongruentialGeneratorXSHRS() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(PermutedCongruentialGeneratorXSHRS.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public PermutedCongruentialGeneratorXSHRS(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return PermutedCongruentialGeneratorXSHRS.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return PermutedCongruentialGeneratorXSHRS.STATE_SIZE;
	}

	@Override
	public void setSeed(final byte[] seed) {
		this.setState(seed);
		this.state = (2 * this.state) + 1;
		this.generateUniformInteger();
	}

	@Override
	public byte[] getState() {
		return ByteConverter.longToBytes(this.state);
	}

	@Override
	public void setState(final byte[] state) {
		if (state.length < PermutedCongruentialGeneratorXSHRS.STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = ByteConverter.bytesToLong(state);
	}

	@Override
	public int generateUniformInteger() {
		long a = this.state;
		final int count = (int) (a >>> 61);
		this.state = a * 6364136223846793005L;
		a ^= a >>> 22;
		return (int) (a >>> (22 + count));
	}

}
