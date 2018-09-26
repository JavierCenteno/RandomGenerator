package generators;

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
	// Class fields

	private static final int N = 65537;
	private static final int G = 75;

	// -----------------------------------------------------------------------------
	// Instance initializers

	public Lehmer16Generator(byte[] seed) {
		super(seed);
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public short getRandomUniformShort() {
		int next = this.state;
		do {
			next = G * next % N;
		} while (next == 65536);
		return this.state = (short) next;
	}

}
