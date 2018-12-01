package generators;

import api.RandomGenerator1;
import util.ByteConverter;

/**
 * Implementation of a fibonacci linear-feedback shift register PRNG with a
 * state of 64 bits and a period of 2^64 - 1 = 18446744073709551615.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class FLFSR64Generator implements RandomGenerator1 {

	// -----------------------------------------------------------------------------
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 8;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = STATE_SIZE;

	// -----------------------------------------------------------------------------
	// Instance fields

	/**
	 * State of this generator.
	 */
	private long state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public FLFSR64Generator() {
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
	public FLFSR64Generator(byte[] seed) {
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
		return ByteConverter.longToBytes(this.state);
	}

	@Override
	public void setState(byte[] state) {
		if (state.length < STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = ByteConverter.bytesToLong(state);
	}

	@Override
	public byte generateBit() {
		long bit = this.state >>> 0;
		bit ^= this.state >>> 1;
		bit ^= this.state >>> 3;
		bit ^= this.state >>> 4;
		bit &= 1;
		this.state = (this.state >>> 1) | (bit << 31);
		return (byte) bit;
	}

}
