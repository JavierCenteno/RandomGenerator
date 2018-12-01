package generators;

import api.RandomGenerator64;
import util.ByteConverter;

/**
 * Implementation of a Mersenne Twister 19937 PRNG with a state of 64 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class MersenneTwister19937Generator implements RandomGenerator64 {

	// -----------------------------------------------------------------------------
	// Class fields

	/**
	 * Size of this generator's state in longs.
	 */
	public static final int STATE_SIZE_LONGS = 312;
	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = STATE_SIZE_LONGS * Long.BYTES;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = STATE_SIZE;
	/**
	 * Size of this generator's state, including the index, in bytes.
	 */
	public static final int FULL_STATE_SIZE = Integer.BYTES + STATE_SIZE;
	private static final int SHIFT_SIZE = 156;
	private static final long UPPER_MASK = 0xFF_FF_FF_FF_80_00_00_00L;
	private static final long LOWER_MASK = 0x00_00_00_00_7F_FF_FF_FFL;
	private static final long XOR_MASK = 0xB5_02_6F_5A_A9_66_19_E9L;

	// -----------------------------------------------------------------------------
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
	private long[] state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public MersenneTwister19937Generator() {
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
	public MersenneTwister19937Generator(byte[] seed) {
		setSeed(seed);
	}

	// -----------------------------------------------------------------------------
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
		this.state = ByteConverter.bytesToLongs(seed);
	}

	@Override
	public byte[] getState() {
		byte[] indexBytes = ByteConverter.integerToBytes(index);
		byte[] stateBytes = ByteConverter.longsToBytes(this.state);
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
		this.state = ByteConverter.bytesToLongs(stateBytes);
	}

	@Override
	public long generateUniformLong() {
		if (index == STATE_SIZE_LONGS) {
			int i = 0;
			while (i < SHIFT_SIZE) {
				long x = (state[i] & UPPER_MASK) | (state[i + 1] & LOWER_MASK);
				state[i] = state[i + SHIFT_SIZE] ^ (x >>> 1);
				if (x % 2 == 0) {
					state[i] ^= XOR_MASK;
				}
				++i;
			}
			while (i < STATE_SIZE_LONGS - 1) {
				long x = (state[i] & UPPER_MASK) | (state[i + 1] & LOWER_MASK);
				state[i] = state[i + SHIFT_SIZE - STATE_SIZE_LONGS] ^ (x >>> 1);
				if (x % 2 == 0) {
					state[i] ^= XOR_MASK;
				}
				++i;
			}
			long x = (state[STATE_SIZE_LONGS - 1] & UPPER_MASK) | (state[0] & LOWER_MASK);
			state[STATE_SIZE_LONGS - 1] = state[SHIFT_SIZE - 1] ^ (x >>> 1);
			if (x % 2 == 0) {
				state[STATE_SIZE_LONGS - 1] ^= XOR_MASK;
			}
			index = 0;
		}
		long x = state[index];
		x ^= (x >> 29) & 0x5555555555555555L;
		x ^= (x << 17) & 0x71D67FFFEDA60000L;
		x ^= (x << 37) & 0xFFF7EEE000000000L;
		x ^= (x >> 43);
		++index;
		return x;
	}

}
