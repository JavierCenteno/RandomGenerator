package generators;

import api.Abstract64RandomGenerator;

/**
 * Implementation of a linear congruential PRNG with a state of 64 bits.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public class LinearCongruential64Generator extends Abstract64RandomGenerator {

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public LinearCongruential64Generator() {
		super();
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public LinearCongruential64Generator(final byte[] seed) {
		super(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public long generateUniformLong() {
		return this.state = (this.state * 6364136223846793005L) + 1442695040888963407L;
	}

}
