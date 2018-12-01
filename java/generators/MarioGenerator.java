package generators;

import api.RandomGenerator16;
import util.ByteConverter;

/**
 * Implementation of the PRNG used in super mario 64. This generator has a state
 * of 16 bits and a period of 65114.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class MarioGenerator implements RandomGenerator16 {

	// -----------------------------------------------------------------------------
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 2;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = STATE_SIZE;

	// -----------------------------------------------------------------------------
	// Instance fields

	/**
	 * State of this generator.
	 */
	private short state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public MarioGenerator() {
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
	public MarioGenerator(byte[] seed) {
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
		return STATE_SIZE;
	}

	@Override
	public byte[] getState() {
		return ByteConverter.shortToBytes(this.state);
	}

	@Override
	public void setState(byte[] state) {
		if (state.length < STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = ByteConverter.bytesToShort(state);
	}

	@Override
	public short generateUniformShort() {
		int state32 = this.state & 0xFFFF;
		if (state32 == 0x560A) {
			state32 = 0;
		}
		int s0 = (state32 & 0x00FF) << 8;
		s0 ^= state32;
		state32 = ((s0 & 0x00FF) << 8) | ((s0 & 0xFF00) >>> 8);
		s0 = ((s0 & 0x00FF) << 1) ^ state32;
		int s1 = (s0 >>> 1) ^ 0xFF80;
		if ((s0 & 1) == 0) {
			if (s1 == 0xAA55) {
				state32 = 0;
			} else {
				state32 = s1 ^ 0x1FF4;
			}
		} else {
			state32 = s1 ^ 0x8180;
		}
		return this.state = (short) state32;
	}

}
