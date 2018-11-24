package generators;

import java.util.Arrays;

import api.RandomGenerator;
import util.ByteConverter;

/**
 * Implementation of a Xoshiro256+ PRNG with a state of 256 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class Xoshiro256PlusGenerator implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 32;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = STATE_SIZE;
	private static final long[] JUMP = { 0x180EC6D33CFD0ABAL, 0xD5A61266F0C9392CL, 0xA9582618E03FC9AAL,
			0x39ABDC4529B1661CL };
	private static final long[] LONG_JUMP = { 0x76E15D3EFEFDCBBFL, 0xC5004E441C522FB3L, 0x77710069854EE241L,
			0x39109BB02ACBE635L };

	// -----------------------------------------------------------------------------
	// Instance fields

	/**
	 * State of this generator.
	 */
	private long[] state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public Xoshiro256PlusGenerator() {
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
	public Xoshiro256PlusGenerator(byte[] seed) {
		setSeed(seed);
	}

	/**
	 * Constructs a copy of this generator.
	 * 
	 * @param generator
	 *                      A generator.
	 */
	public Xoshiro256PlusGenerator(Xoshiro256PlusGenerator generator) {
		this.state = Arrays.copyOf(generator.state, generator.state.length);
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public int getSeedSize() {
		return SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return STATE_SIZE;
	}

	@Override
	public byte[] getState() {
		return ByteConverter.longsToBytes(this.state);
	}

	@Override
	public void setState(byte[] state) {
		if (state.length < STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = ByteConverter.bytesToLongs(state);
	}

	@Override
	public Xoshiro256PlusGenerator split() {
		Xoshiro256PlusGenerator generator = new Xoshiro256PlusGenerator(this);
		generator.jump();
		return generator;
	}

	@Override
	public long generateUniformLong() {
		long result = state[0] + state[3];
		long t = state[1] << 17;
		state[2] ^= state[0];
		state[3] ^= state[1];
		state[1] ^= state[2];
		state[0] ^= state[3];
		state[2] ^= t;
		state[3] = (state[3] << 45) | (state[3] >>> 19);
		return result;
	}

	/**
	 * Equivalent to 2^128 calls to generateUniformLong(). Can be used to generate
	 * 2^128 non-overlapping sequences.
	 */
	public void jump() {
		long state0 = 0;
		long state1 = 0;
		long state2 = 0;
		long state3 = 0;
		for (int i = 0; i < JUMP.length; ++i) {
			for (int j = 0; j < 64; ++j) {
				if ((JUMP[i] & (1L << j)) != 0) {
					state0 ^= state[0];
					state1 ^= state[1];
					state2 ^= state[2];
					state3 ^= state[3];
				}
				generateUniformLong();
			}
		}
		state[0] = state0;
		state[1] = state1;
		state[2] = state2;
		state[3] = state3;
	}

	/**
	 * Equivalent to 2^192 calls to generateUniformLong(). Can be used to generate
	 * 2^64 starting points from each of which jump() can generate 2^64
	 * non-overlapping sequences.
	 */
	public void longJump() {
		long state0 = 0;
		long state1 = 0;
		long state2 = 0;
		long state3 = 0;
		for (int i = 0; i < LONG_JUMP.length; ++i) {
			for (int j = 0; j < 64; ++j) {
				if ((LONG_JUMP[i] & (1L << j)) != 0) {
					state0 ^= state[0];
					state1 ^= state[1];
					state2 ^= state[2];
					state3 ^= state[3];
				}
				generateUniformLong();
			}
		}
		state[0] = state0;
		state[1] = state1;
		state[2] = state2;
		state[3] = state3;
	}

}
