package generators;

import java.security.SecureRandom;

import api.Abstract16RandomGenerator;

/**
 * Implementation of a Lehmer PRNG with a state of 16 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @see generators.Xorshift64Generator
 * @since 1.0
 * 
 */
public class Lehmer16Generator extends Abstract16RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public Lehmer16Generator() {
		super();
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 */
	public Lehmer16Generator(byte[] seed) {
		super(seed);
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public short generateUniformShort() {
		int next = this.state;
		do {
			next = 75 * next % 65537;
		} while (next == 65536);
		return this.state = (short) next;
	}

}
