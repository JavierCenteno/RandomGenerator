package generators;

import java.security.SecureRandom;

import api.RandomGenerator;

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
		// Turn seed into a long
		long longSeed;
		long seed0 = ((long) seed[0]) << 56;
		long seed1 = ((long) seed[1]) << 48;
		long seed2 = ((long) seed[2]) << 40;
		long seed3 = ((long) seed[3]) << 32;
		long seed4 = ((long) seed[4]) << 24;
		long seed5 = ((long) seed[5]) << 16;
		long seed6 = ((long) seed[6]) << 8;
		long seed7 = (long) seed[7];
		longSeed = (seed0 | seed1 | seed2 | seed3 | seed4 | seed5 | seed6 | seed7);
		// Initialize generator with the long seed
		state = new long[STATE_SIZE];
		index = 0;
		state[index] = longSeed;
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
		int i = 0;
		int j = 0;
		byte[] byteState = new byte[STATE_SIZE * 8];
		while (i < state.length) {
			byteState[j++] = (byte) (state[i] << 56);
			byteState[j++] = (byte) (state[i] << 48);
			byteState[j++] = (byte) (state[i] << 40);
			byteState[j++] = (byte) (state[i] << 32);
			byteState[j++] = (byte) (state[i] << 24);
			byteState[j++] = (byte) (state[i] << 16);
			byteState[j++] = (byte) (state[i] << 8);
			byteState[j++] = (byte) (state[i]);
			i++;
		}
		return byteState;
	}

	@Override
	public void setState(byte[] state) {
		int i = 0;
		int j = 0;
		long[] longState = new long[STATE_SIZE];
		while (i < longState.length) {
			long _0 = ((long) state[j++]) << 56;
			long _1 = ((long) state[j++]) << 48;
			long _2 = ((long) state[j++]) << 40;
			long _3 = ((long) state[j++]) << 32;
			long _4 = ((long) state[j++]) << 24;
			long _5 = ((long) state[j++]) << 16;
			long _6 = ((long) state[j++]) << 8;
			long _7 = (long) state[j++];
			longState[i++] = (_0 & _1 & _2 & _3 & _4 & _5 & _6 & _7);
		}
		this.state = longState;
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
