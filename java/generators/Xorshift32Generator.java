package generators;

import java.security.SecureRandom;

import api.Abstract32RandomGenerator;

/**
 * Implementation of a xorshift PRNG with a state of 64 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class Xorshift32Generator extends Abstract32RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public Xorshift32Generator() {
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
	public Xorshift32Generator(byte[] seed) {
		super(seed);
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public int generateUniformInteger() {
		this.state ^= this.state << 13;
		this.state ^= this.state >>> 17;
		this.state ^= this.state << 5;
		return this.state;
	}

}
