package generators;

import api.RandomGenerator;
import api.RandomGenerator32;
import util.ByteConverter;

/**
 * Implementation of a xorshift128 PRNG with a state of 128 bits.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public class Xorshift128Generator implements RandomGenerator32 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 16;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = Xorshift128Generator.STATE_SIZE;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * State of this generator.
	 */
	private int[] state;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public Xorshift128Generator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(Xorshift128Generator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public Xorshift128Generator(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return Xorshift128Generator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return Xorshift128Generator.STATE_SIZE;
	}

	@Override
	public byte[] getState() {
		return ByteConverter.integersToBytes(this.state);
	}

	@Override
	public void setState(final byte[] state) {
		if (state.length < Xorshift128Generator.STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = ByteConverter.bytesToIntegers(state);
	}

	@Override
	public int generateUniformInteger() {
		int t = this.state[3];
		t ^= t << 11;
		t ^= t >>> 8;
		this.state[3] = this.state[2];
		this.state[2] = this.state[1];
		final int s = this.state[0];
		this.state[1] = s;
		t ^= s;
		t ^= s >>> 19;
		this.state[0] = t;
		return t;
	}

}
