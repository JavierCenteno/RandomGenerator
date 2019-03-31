package generators;

import java.util.Arrays;

import api.RandomGenerator;
import api.RandomGenerator64;
import util.ByteConverter;

/**
 * Implementation of a Xoroshiro128+ PRNG with a state of 128 bits.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public class Xoroshiro128PlusGenerator implements RandomGenerator64 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 16;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = Xoroshiro128PlusGenerator.STATE_SIZE;
	private static final long[] JUMP = { 0xDF900294D8F554A5L, 0x170865DF4B3201FCL };
	private static final long[] LONG_JUMP = { 0xD2A98B26625EEE7BL, 0xDDDF9B1090AA7AC1L };

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * State of this generator.
	 */
	private long[] state;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public Xoroshiro128PlusGenerator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(Xoroshiro128PlusGenerator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public Xoroshiro128PlusGenerator(final byte[] seed) {
		this.setSeed(seed);
	}

	/**
	 * Constructs a copy of this generator.
	 *
	 * @param generator
	 *                      A generator.
	 */
	public Xoroshiro128PlusGenerator(final Xoroshiro128PlusGenerator generator) {
		this.state = Arrays.copyOf(generator.state, generator.state.length);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return Xoroshiro128PlusGenerator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return Xoroshiro128PlusGenerator.STATE_SIZE;
	}

	@Override
	public byte[] getState() {
		return ByteConverter.longsToBytes(this.state);
	}

	@Override
	public void setState(final byte[] state) {
		if (state.length < Xoroshiro128PlusGenerator.STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = ByteConverter.bytesToLongs(state);
	}

	@Override
	public Xoroshiro128PlusGenerator split() {
		final Xoroshiro128PlusGenerator generator = new Xoroshiro128PlusGenerator(this);
		generator.jump();
		return generator;
	}

	@Override
	public long generateUniformLong() {
		final long state0 = this.state[0];
		long state1 = this.state[1];
		final long result = state0 + state1;
		state1 ^= state0;
		this.state[0] = ((state0 << 24) | (state0 >>> 40)) ^ state1 ^ (state1 << 16);
		this.state[1] = (state1 << 37) | (state1 >>> 27);
		return result;
	}

	/**
	 * Equivalent to 2^64 calls to generateUniformLong(). Can be used to generate
	 * 2^64 non-overlapping sequences.
	 */
	public void jump() {
		long state0 = 0;
		long state1 = 0;
		for (int i = 0; i < Xoroshiro128PlusGenerator.JUMP.length; ++i) {
			for (int j = 0; j < 64; ++j) {
				if ((Xoroshiro128PlusGenerator.JUMP[i] & (1L << j)) != 0) {
					state0 ^= this.state[0];
					state1 ^= this.state[1];
				}
				this.generateUniformLong();
			}
		}
		this.state[0] = state0;
		this.state[1] = state1;
	}

	/**
	 * Equivalent to 2^96 calls to generateUniformLong(). Can be used to generate
	 * 2^32 starting points from each of which jump() can generate 2^32
	 * non-overlapping sequences.
	 */
	public void longJump() {
		long state0 = 0;
		long state1 = 0;
		for (int i = 0; i < Xoroshiro128PlusGenerator.LONG_JUMP.length; ++i) {
			for (int j = 0; j < 64; ++j) {
				if ((Xoroshiro128PlusGenerator.LONG_JUMP[i] & (1L << j)) != 0) {
					state0 ^= this.state[0];
					state1 ^= this.state[1];
				}
				this.generateUniformLong();
			}
		}
		this.state[0] = state0;
		this.state[1] = state1;
	}

}
