package api;

/**
 * This class offers a partial implementation of RandomGenerator for a generator
 * with 8 bits of state.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator8
 * @since 1.0
 *
 */
public abstract class Abstract8RandomGenerator implements RandomGenerator8 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 1;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = Abstract8RandomGenerator.STATE_SIZE;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * State of this generator.
	 */
	protected byte state;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public Abstract8RandomGenerator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(Abstract8RandomGenerator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public Abstract8RandomGenerator(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return Abstract8RandomGenerator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return Abstract8RandomGenerator.STATE_SIZE;
	}

	@Override
	public byte[] getState() {
		return new byte[] { (byte) this.state };
	}

	@Override
	public void setState(final byte[] state) {
		if (state.length < Abstract8RandomGenerator.STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = state[0];
	}

}
