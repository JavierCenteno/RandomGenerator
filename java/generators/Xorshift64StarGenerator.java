package generators;

import api.Abstract64RandomGenerator;

/**
 * Implementation of a xorshift64* PRNG with a state of 64 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @see generators.Xorshift64Generator
 * @since 1.0
 * 
 */
public class Xorshift64StarGenerator extends Abstract64RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public Xorshift64StarGenerator() {
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
	public Xorshift64StarGenerator(byte[] seed) {
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
