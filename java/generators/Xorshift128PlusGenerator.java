package generators;

import api.RandomGenerator;
import api.RandomGenerator64;
import util.ByteConverter;

/**
 * Implementation of a xorshift128+ PRNG with a state of 128 bits.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public class Xorshift128PlusGenerator implements RandomGenerator64 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 16;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = Xorshift128PlusGenerator.STATE_SIZE;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * State of this generator.
	 */
	private long[] state;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public Xorshift128PlusGenerator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(Xorshift128PlusGenerator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public Xorshift128PlusGenerator(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return Xorshift128PlusGenerator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return Xorshift128PlusGenerator.STATE_SIZE;
	}

	@Override
	public byte[] getState() {
		return ByteConverter.longsToBytes(this.state);
	}

	@Override
	public void setState(final byte[] state) {
		if (state.length < Xorshift128PlusGenerator.STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = ByteConverter.bytesToLongs(state);
	}

	@Override
	public long generateUniformLong() {
		long state0 = this.state[0];
		final long state1 = this.state[1];
		this.state[0] = state1;
		state0 ^= state0 << 23;
		this.state[1] = state0 ^ state1 ^ (state0 >> 17) ^ (state1 >> 26);
		return this.state[1] + state1;
	}

}
