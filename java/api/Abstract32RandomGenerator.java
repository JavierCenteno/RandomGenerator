package api;

import util.ByteConverter;

/**
 * This class offers a partial implementation of RandomGenerator for a generator
 * with 32 bits of state.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator32
 * @since 1.0
 *
 */
public abstract class Abstract32RandomGenerator implements RandomGenerator32 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 4;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = Abstract32RandomGenerator.STATE_SIZE;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * State of this generator.
	 */
	protected int state;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public Abstract32RandomGenerator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(Abstract32RandomGenerator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public Abstract32RandomGenerator(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return Abstract32RandomGenerator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return Abstract32RandomGenerator.STATE_SIZE;
	}

	@Override
	public byte[] getState() {
		return ByteConverter.integerToBytes(this.state);
	}

	@Override
	public void setState(final byte[] state) {
		if (state.length < Abstract32RandomGenerator.STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = ByteConverter.bytesToInteger(state);
	}

}
