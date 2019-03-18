package api;

import util.ByteConverter;

/**
 * This class offers a partial implementation of RandomGenerator for a generator
 * with 16 bits of state.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator16
 * @since 1.0
 * 
 */
public abstract class Abstract16RandomGenerator implements RandomGenerator16 {

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
	protected short state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public Abstract16RandomGenerator() {
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
	public Abstract16RandomGenerator(byte[] seed) {
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

}
