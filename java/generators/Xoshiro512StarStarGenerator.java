package generators;

import java.util.Arrays;

import api.RandomGenerator64;
import util.ByteConverter;

/**
 * Implementation of a Xoshiro512** PRNG with a state of 512 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class Xoshiro512StarStarGenerator implements RandomGenerator64 {

	// -----------------------------------------------------------------------------
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 64;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = STATE_SIZE;
	private static final long[] JUMP = { 0x33ED89B6E7A353F9L, 0x760083D7955323BEL, 0x2837F2FBB5F22FAEL,
			0x4B8C5674d309511CL, 0xB11AC47A7BA28C25L, 0xF1BE7667092BCC1CL, 0x53851EFDB6DF0AAFL, 0x1EBBC8B23EAF25DBL };

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
	public Xoshiro512StarStarGenerator() {
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
	public Xoshiro512StarStarGenerator(byte[] seed) {
		setSeed(seed);
	}

	/**
	 * Constructs a copy of this generator.
	 * 
	 * @param generator
	 *                      A generator.
	 */
	public Xoshiro512StarStarGenerator(Xoshiro512StarStarGenerator generator) {
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
	public Xoshiro512StarStarGenerator split() {
		Xoshiro512StarStarGenerator generator = new Xoshiro512StarStarGenerator(this);
		generator.jump();
		return generator;
	}

	@Override
	public long generateUniformLong() {
		long x = state[1] * 5L;
		long result = ((x << 7) | (x >>> 57)) * 9L;
		long t = state[1] << 11;
		state[2] ^= state[0];
		state[5] ^= state[1];
		state[1] ^= state[2];
		state[7] ^= state[3];
		state[3] ^= state[4];
		state[4] ^= state[5];
		state[0] ^= state[6];
		state[6] ^= state[7];
		state[6] ^= t;
		state[7] = (state[7] << 21) | (state[7] >>> 43);
		return result;
	}

	/**
	 * Equivalent to 2^256 calls to generateUniformLong(). Can be used to generate
	 * 2^256 non-overlapping sequences.
	 */
	public void jump() {
		long state0 = 0;
		long state1 = 0;
		long state2 = 0;
		long state3 = 0;
		long state4 = 0;
		long state5 = 0;
		long state6 = 0;
		long state7 = 0;
		for (int i = 0; i < JUMP.length; ++i) {
			for (int j = 0; j < 64; ++j) {
				if ((JUMP[i] & (1L << j)) != 0) {
					state0 ^= state[0];
					state1 ^= state[1];
					state2 ^= state[2];
					state3 ^= state[3];
					state4 ^= state[4];
					state5 ^= state[5];
					state6 ^= state[6];
					state7 ^= state[7];
				}
				generateUniformLong();
			}
		}
		state[0] = state0;
		state[1] = state1;
		state[2] = state2;
		state[3] = state3;
		state[4] = state4;
		state[5] = state5;
		state[6] = state6;
		state[7] = state7;
	}

}
