package generators;

import java.security.SecureRandom;

import api.Abstract32RandomGenerator;

/**
 * Implementation of a linear congruential PRNG with a state of 64 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class LinearCongruential32Generator extends Abstract32RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public LinearCongruential32Generator() {
		super();
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 */
	public LinearCongruential32Generator(byte[] seed) {
		super(seed);
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public int getRandomUniformInteger() {
		return this.state = this.state * 134775813 + 1;
	}

}
