package generators;

import api.RandomGenerator;
import api.RandomGenerator32;
import util.ByteConverter;

/**
 * Implementation of a well equidistributed long-period linear 44497 B PRNG with
 * a state of 44512 bits.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public class WELL44497BGenerator implements RandomGenerator32 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in integers.
	 */
	public static final int STATE_SIZE_INTEGERS = 1391;
	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = WELL44497BGenerator.STATE_SIZE_INTEGERS * Integer.BYTES;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = WELL44497BGenerator.STATE_SIZE;
	/**
	 * Size of this generator's state, including the index, in bytes.
	 */
	public static final int FULL_STATE_SIZE = Integer.BYTES + WELL44497BGenerator.STATE_SIZE;

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
	public WELL44497BGenerator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(WELL44497BGenerator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public WELL44497BGenerator(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return WELL44497BGenerator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return WELL44497BGenerator.FULL_STATE_SIZE;
	}

	@Override
	public void setSeed(final byte[] seed) {
		if (seed.length < WELL44497BGenerator.SEED_SIZE) {
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
		if (state.length < WELL44497BGenerator.FULL_STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		final byte[] indexBytes = new byte[Integer.BYTES];
		final byte[] stateBytes = new byte[WELL44497BGenerator.STATE_SIZE];
		System.arraycopy(state, 0, indexBytes, 0, indexBytes.length);
		System.arraycopy(state, indexBytes.length, stateBytes, 0, stateBytes.length);
		this.index = ByteConverter.bytesToInteger(indexBytes);
		this.state = ByteConverter.bytesToIntegers(stateBytes);
	}

	@Override
	public int generateUniformInteger() {
		final int index0 = this.index;
		final int index23 = (this.index + 23) % WELL44497BGenerator.STATE_SIZE_INTEGERS;
		final int index229 = (this.index + 229) % WELL44497BGenerator.STATE_SIZE_INTEGERS;
		final int index481 = (this.index + 481) % WELL44497BGenerator.STATE_SIZE_INTEGERS;
		final int index1389 = (this.index + 1389) % WELL44497BGenerator.STATE_SIZE_INTEGERS;
		final int index1390 = (this.index + 1390) % WELL44497BGenerator.STATE_SIZE_INTEGERS;
		final int z0 = (this.state[index1390] & 0xFFFF8000) | (this.state[index1389] & 0x00007FFF);
		final int z1 = (this.state[index0] ^ (this.state[index0] << 24))
				^ (this.state[index23] ^ (this.state[index23] >>> 30));
		final int z2 = (this.state[index481] ^ (this.state[index481] << 10)) ^ (this.state[index229] << 26);
		this.state[index0] = z1 ^ z2;
		this.state[index1390] = z0 ^ (z1 ^ (z1 >>> 20))
				^ ((z2 & 0x00020000) != 0 ? ((((z2 << 9) ^ (z2 >>> 23)) & 0xFBFFFFFF) ^ 0xB729FCEC)
						: (((z2 << 9) ^ (z2 >>> 23)) & 0xFBFFFFFF));
		this.index = index1390;
		int y = this.state[this.index] ^ ((this.state[this.index] << 7) & 0x93DD1400);
		y = y ^ ((y << 15) & 0xFA118000);
		return y;
	}

}
