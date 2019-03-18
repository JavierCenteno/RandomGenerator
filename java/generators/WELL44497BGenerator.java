package generators;

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
	public static final int STATE_SIZE = STATE_SIZE_INTEGERS * Integer.BYTES;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = STATE_SIZE;
	/**
	 * Size of this generator's state, including the index, in bytes.
	 */
	public static final int FULL_STATE_SIZE = Integer.BYTES + STATE_SIZE;

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
	public WELL44497BGenerator(byte[] seed) {
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
		return FULL_STATE_SIZE;
	}

	@Override
	public void setSeed(byte[] seed) {
		if (seed.length < SEED_SIZE) {
			throw new IllegalArgumentException();
		}
		this.index = 0;
		this.state = ByteConverter.bytesToIntegers(seed);
	}

	@Override
	public byte[] getState() {
		byte[] indexBytes = ByteConverter.integerToBytes(index);
		byte[] stateBytes = ByteConverter.integersToBytes(this.state);
		byte[] fullState = new byte[indexBytes.length + stateBytes.length];
		System.arraycopy(indexBytes, 0, fullState, 0, indexBytes.length);
		System.arraycopy(stateBytes, 0, fullState, indexBytes.length, stateBytes.length);
		return fullState;
	}

	@Override
	public void setState(byte[] state) {
		if (state.length < FULL_STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		byte[] indexBytes = new byte[Integer.BYTES];
		byte[] stateBytes = new byte[STATE_SIZE];
		System.arraycopy(state, 0, indexBytes, 0, indexBytes.length);
		System.arraycopy(state, indexBytes.length, stateBytes, 0, stateBytes.length);
		this.index = ByteConverter.bytesToInteger(indexBytes);
		this.state = ByteConverter.bytesToIntegers(stateBytes);
	}

	@Override
	public int generateUniformInteger() {
		int index0 = index;
		int index23 = (index + 23) % STATE_SIZE_INTEGERS;
		int index229 = (index + 229) % STATE_SIZE_INTEGERS;
		int index481 = (index + 481) % STATE_SIZE_INTEGERS;
		int index1389 = (index + 1389) % STATE_SIZE_INTEGERS;
		int index1390 = (index + 1390) % STATE_SIZE_INTEGERS;
		int z0 = (state[index1390] & 0xFFFF8000) | (state[index1389] & 0x00007FFF);
		int z1 = (state[index0] ^ (state[index0] << 24)) ^ (state[index23] ^ (state[index23] >>> 30));
		int z2 = (state[index481] ^ (state[index481] << 10)) ^ (state[index229] << 26);
		state[index0] = z1 ^ z2;
		state[index1390] = z0 ^ (z1 ^ (z1 >>> 20))
				^ ((z2 & 0x00020000) != 0 ? ((((z2 << 9) ^ (z2 >>> 23)) & 0xFBFFFFFF) ^ 0xB729FCEC)
						: (((z2 << 9) ^ (z2 >>> 23)) & 0xFBFFFFFF));
		index = index1390;
		int y = state[index] ^ ((state[index] << 7) & 0x93DD1400);
		y = y ^ ((y << 15) & 0xFA118000);
		return y;
	}

}
