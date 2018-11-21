package generators;

import java.security.SecureRandom;

import api.RandomGenerator;
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
public class MersenneTwister19937Generator implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Class fields

	private static final int STATE_SIZE = 312;
	private static final int SHIFT_SIZE = 156;
	private static final long UPPER_MASK = 0xFF_FF_FF_FF_80_00_00_00L;
	private static final long LOWER_MASK = 0x00_00_00_00_7F_FF_FF_FFL;
	private static final long XOR_MASK = 0xB5_02_6F_5A_A9_66_19_E9L;

	// -----------------------------------------------------------------------------
	// Instance fields

	private int index;
	private long[] state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public MersenneTwister19937Generator() {
		setSeed(SecureRandom.getSeed(8));
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 */
	public MersenneTwister19937Generator(byte[] seed) {
		setSeed(seed);
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public void setSeed(byte[] seed) {
		state = new long[STATE_SIZE];
		index = 0;
		state[index] = ByteConverter.bytesToLong(seed);
		while (index < STATE_SIZE - 1) {
			long state_i = state[index];
			state_i ^= state_i >>> 12;
			state_i ^= state_i << 25;
			state_i ^= state_i >>> 27;
			state_i *= 6364136223846793005L;
			state_i += 1442695040888963407L;
			++index;
			state[index] = state_i;
		}
		index = 0;
	}

	@Override
	public byte[] getState() {
		return ByteConverter.longsToBytes(this.state);
	}

	@Override
	public void setState(byte[] state) {
		this.state = ByteConverter.bytesToLongs(state);
	}

	@Override
	public long generateUniformLong() {
		if (index == STATE_SIZE) {
			int i = 0;
			while (i < SHIFT_SIZE) {
				long x = (state[i] & UPPER_MASK) | (state[i + 1] & LOWER_MASK);
				state[i] = state[i + SHIFT_SIZE] ^ (x >>> 1);
				if (x % 2 == 0) {
					state[i] ^= XOR_MASK;
				}
				++i;
			}
			while (i < STATE_SIZE - 1) {
				long x = (state[i] & UPPER_MASK) | (state[i + 1] & LOWER_MASK);
				state[i] = state[i + SHIFT_SIZE - STATE_SIZE] ^ (x >>> 1);
				if (x % 2 == 0) {
					state[i] ^= XOR_MASK;
				}
				++i;
			}
			long x = (state[STATE_SIZE - 1] & UPPER_MASK) | (state[0] & LOWER_MASK);
			state[STATE_SIZE - 1] = state[SHIFT_SIZE - 1] ^ (x >>> 1);
			if (x % 2 == 0) {
				state[STATE_SIZE - 1] ^= XOR_MASK;
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
