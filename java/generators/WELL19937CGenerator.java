package generators;

import api.RandomGenerator;
import api.RandomGenerator32;
import util.ByteConverter;

/**
 * Implementation of a well equidistributed long-period linear 19937 C PRNG with
 * a state of 19968 bits.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public class WELL19937CGenerator implements RandomGenerator32 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in integers.
	 */
	public static final int STATE_SIZE_INTEGERS = 624;
	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = WELL19937CGenerator.STATE_SIZE_INTEGERS * Integer.BYTES;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = WELL19937CGenerator.STATE_SIZE;
	/**
	 * Size of this generator's state, including the index, in bytes.
	 */
	public static final int FULL_STATE_SIZE = Integer.BYTES + WELL19937CGenerator.STATE_SIZE;

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
	public WELL19937CGenerator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(WELL19937CGenerator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public WELL19937CGenerator(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return WELL19937CGenerator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return WELL19937CGenerator.FULL_STATE_SIZE;
	}

	@Override
	public void setSeed(final byte[] seed) {
		if (seed.length < WELL19937CGenerator.SEED_SIZE) {
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
		if (state.length < WELL19937CGenerator.FULL_STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		final byte[] indexBytes = new byte[Integer.BYTES];
		final byte[] stateBytes = new byte[WELL19937CGenerator.STATE_SIZE];
		System.arraycopy(state, 0, indexBytes, 0, indexBytes.length);
		System.arraycopy(state, indexBytes.length, stateBytes, 0, stateBytes.length);
		this.index = ByteConverter.bytesToInteger(indexBytes);
		this.state = ByteConverter.bytesToIntegers(stateBytes);
	}

	@Override
	public int generateUniformInteger() {
		final int index0 = this.index;
		final int index70 = (this.index + 70) % WELL19937CGenerator.STATE_SIZE_INTEGERS;
		final int index179 = (this.index + 179) % WELL19937CGenerator.STATE_SIZE_INTEGERS;
		final int index449 = (this.index + 449) % WELL19937CGenerator.STATE_SIZE_INTEGERS;
		final int index622 = (this.index + 622) % WELL19937CGenerator.STATE_SIZE_INTEGERS;
		final int index623 = (this.index + 623) % WELL19937CGenerator.STATE_SIZE_INTEGERS;
		final int z0 = (this.state[index623] & 0xFFFFFFFE) | (this.state[index622] & 0x00000001);
		final int z1 = (this.state[index0] ^ (this.state[index0] << 25))
				^ (this.state[index70] ^ (this.state[index70] >>> 27));
		final int z2 = (this.state[index179] >> 9) ^ (this.state[index449] ^ (this.state[index449] >>> 1));
		this.state[index0] = z1 ^ z2;
		this.state[index623] = z0 ^ (z1 ^ (z1 << 9)) ^ (z2 ^ (z2 << 21))
				^ (this.state[index0] ^ (this.state[index0] >>> 21));
		this.index = index623;
		int y = this.state[this.index] ^ ((this.state[this.index] << 7) & 0xE46E1700);
		y = y ^ ((y << 15) & 0x9B86800);
		return y;
	}

}
