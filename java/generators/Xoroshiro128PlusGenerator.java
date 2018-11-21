package generators;

import java.security.SecureRandom;

import api.RandomGenerator;
import util.ByteConverter;

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
		setState(seed);
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
