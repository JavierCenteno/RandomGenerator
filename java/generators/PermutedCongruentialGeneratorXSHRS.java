package generators;

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
	public static final int SEED_SIZE = STATE_SIZE;

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
		this(DEFAULT_SEED_GENERATOR.generateBytes(SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public PermutedCongruentialGeneratorXSHRS(byte[] seed) {
		setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return STATE_SIZE;
	}

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
		if (state.length < STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = ByteConverter.bytesToLong(state);
	}

	@Override
	public int generateUniformInteger() {
		long a = state;
		int count = (int) (a >>> 61);
		state = a * 6364136223846793005L;
		a ^= a >>> 22;
		return (int) (a >>> (22 + count));
	}

}
