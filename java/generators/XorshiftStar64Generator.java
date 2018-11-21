package generators;

import java.security.SecureRandom;

import api.Abstract64RandomGenerator;

/**
 * Implementation of a xorshift* PRNG with a state of 64 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @see generators.Xorshift64Generator
 * @since 1.0
 * 
 */
public class XorshiftStar64Generator extends Abstract64RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public XorshiftStar64Generator() {
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
	public XorshiftStar64Generator(byte[] seed) {
		super(seed);
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public long generateUniformLong() {
		this.state ^= this.state >>> 12;
		this.state ^= this.state << 25;
		this.state ^= this.state >>> 27;
		return this.state * 0x2545F4914F6CDD1DL;
	}

}
