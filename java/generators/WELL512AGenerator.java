package generators;

import api.RandomGenerator;
import api.RandomGenerator32;
import util.ByteConverter;

/**
 * Implementation of a well equidistributed long-period linear 512 A PRNG with a
 * state of 512 bits.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public class WELL512AGenerator implements RandomGenerator32 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in integers.
	 */
	public static final int STATE_SIZE_INTEGERS = 16;
	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = WELL512AGenerator.STATE_SIZE_INTEGERS * Integer.BYTES;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = WELL512AGenerator.STATE_SIZE;
	/**
	 * Size of this generator's state, including the index, in bytes.
	 */
	public static final int FULL_STATE_SIZE = Integer.BYTES + WELL512AGenerator.STATE_SIZE;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Index that points at state elements.
	 */
	private int index;
	/**
	 * State of this generator. This field is simply used for the generation of
	 * values returned by the generator and doesn't include other generator
	 * parameters that may affect the returned result, so this may not be equivalent
	 * to what the method getState() returns.
	 */
	private int[] state;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public WELL512AGenerator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(WELL512AGenerator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public WELL512AGenerator(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return WELL512AGenerator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return WELL512AGenerator.FULL_STATE_SIZE;
	}

	@Override
	public void setSeed(final byte[] seed) {
		if (seed.length < WELL512AGenerator.SEED_SIZE) {
			throw new IllegalArgumentException();
		}
		this.index = 0;
		this.state = ByteConverter.bytesToIntegers(seed);
	}

	@Override
	public byte[] getState() {
		final byte[] indexBytes = ByteConverter.integerToBytes(this.index);
		final byte[] stateBytes = ByteConverter.integersToBytes(this.state);
		final byte[] fullState = new byte[indexBytes.length + stateBytes.length];
		System.arraycopy(indexBytes, 0, fullState, 0, indexBytes.length);
		System.arraycopy(stateBytes, 0, fullState, indexBytes.length, stateBytes.length);
		return fullState;
	}

	@Override
	public void setState(final byte[] state) {
		if (state.length < WELL512AGenerator.FULL_STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		final byte[] indexBytes = new byte[Integer.BYTES];
		final byte[] stateBytes = new byte[WELL512AGenerator.STATE_SIZE];
		System.arraycopy(state, 0, indexBytes, 0, indexBytes.length);
		System.arraycopy(state, indexBytes.length, stateBytes, 0, stateBytes.length);
		this.index = ByteConverter.bytesToInteger(indexBytes);
		this.state = ByteConverter.bytesToIntegers(stateBytes);
	}

	@Override
	public int generateUniformInteger() {
		final int index0 = this.index;
		final int index9 = (this.index + 9) % WELL512AGenerator.STATE_SIZE_INTEGERS;
		final int index13 = (this.index + 13) % WELL512AGenerator.STATE_SIZE_INTEGERS;
		final int index15 = (this.index + 15) % WELL512AGenerator.STATE_SIZE_INTEGERS;
		final int z0 = this.state[index15];
		final int z1 = (this.state[index0] ^ (this.state[index0] << 16))
				^ (this.state[index13] ^ (this.state[index13] << 15));
		final int z2 = this.state[index9] ^ (this.state[index9] >>> 11);
		this.state[index0] = z1 ^ z2;
		this.state[index15] = (z0 ^ (z0 << 2)) ^ (z1 ^ (z1 << 18)) ^ (z2 << 28)
				^ (this.state[index0] ^ ((this.state[index0] << 5) & 0xDA442D24));
		this.index = index15;
		return this.state[this.index];
	}

}
