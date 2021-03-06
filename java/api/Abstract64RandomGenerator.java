package api;

import util.ByteConverter;

/**
 * This class offers a partial implementation of RandomGenerator for a generator
 * with 64 bits of state.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator64
 * @since 1.0
 *
 */
public abstract class Abstract64RandomGenerator implements RandomGenerator64 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 8;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = Abstract64RandomGenerator.STATE_SIZE;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * State of this generator.
	 */
	protected long state;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public Abstract64RandomGenerator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(Abstract64RandomGenerator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public Abstract64RandomGenerator(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return Abstract64RandomGenerator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return Abstract64RandomGenerator.STATE_SIZE;
	}

	@Override
	public byte[] getState() {
		return ByteConverter.longToBytes(this.state);
	}

	@Override
	public void setState(final byte[] state) {
		if (state.length < Abstract64RandomGenerator.STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = ByteConverter.bytesToLong(state);
	}

}
