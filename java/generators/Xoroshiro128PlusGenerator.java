package generators;

import java.security.SecureRandom;

import api.RandomGenerator;

/**
 * Implementation of a Xoroshiro+ PRNG with a state of 128 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class Xoroshiro128PlusGenerator implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Class fields

	private static final int STATE_SIZE = 2;
	private static final long[] JUMP = { 0xDF900294D8F554A5L, 0x170865DF4B3201FCL };
	private static final long[] LONG_JUMP = { 0xD2A98B26625EEE7BL, 0xDDDF9B1090AA7AC1L };

	// -----------------------------------------------------------------------------
	// Instance fields

	private long[] state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public Xoroshiro128PlusGenerator() {
		setSeed(SecureRandom.getSeed(16));
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 */
	public Xoroshiro128PlusGenerator(byte[] seed) {
		setSeed(seed);
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public void setSeed(byte[] seed) {
		state = new long[STATE_SIZE];
		for (int i = 0; i < state.length; ++i) {
			long seed0 = ((long) seed[i * 8 + 0]) << 56;
			long seed1 = ((long) seed[i * 8 + 1]) << 48;
			long seed2 = ((long) seed[i * 8 + 2]) << 40;
			long seed3 = ((long) seed[i * 8 + 3]) << 32;
			long seed4 = ((long) seed[i * 8 + 4]) << 24;
			long seed5 = ((long) seed[i * 8 + 5]) << 16;
			long seed6 = ((long) seed[i * 8 + 6]) << 8;
			long seed7 = (long) seed[i * 8 + 7];
			state[i] = (seed0 | seed1 | seed2 | seed3 | seed4 | seed5 | seed6 | seed7);
		}
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
		long state0 = state[0];
		long state1 = state[1];
		long addition = state0 + state1;
		state1 ^= state0;
		state[0] = (state0 << 24) | (state0 >>> (64 - 24)) ^ state1 ^ (state1 << 16);
		state[1] = (state1 << 37) | (state1 >>> (64 - 37));
		return addition;
	}

	/**
	 * Equivalent to 2^64 calls to generateUniformLong(). Can be used to generate
	 * 2^64 non-overlapping sequences.
	 */
	public void jump() {
		long state0 = 0;
		long state1 = 0;
		for (int i = 0; i < JUMP.length; ++i) {
			for (int j = 0; j < 64; ++j) {
				if ((JUMP[i] & (1L << j)) != 0) {
					state0 ^= state[0];
					state1 ^= state[1];
				}
				generateUniformLong();
			}
		}
		state[0] = state0;
		state[1] = state1;
	}

	/**
	 * Equivalent to 2^96 calls to generateUniformLong(). Can be used to generate
	 * 2^32 starting points from each of which jump() can generate 2^32
	 * non-overlapping sequences.
	 */
	public void bigJump() {
		long state0 = 0;
		long state1 = 0;
		for (int i = 0; i < LONG_JUMP.length; ++i) {
			for (int j = 0; j < 64; ++j) {
				if ((LONG_JUMP[i] & (1L << j)) != 0) {
					state0 ^= state[0];
					state1 ^= state[1];
				}
				generateUniformLong();
			}
		}
		state[0] = state0;
		state[1] = state1;
	}

}
