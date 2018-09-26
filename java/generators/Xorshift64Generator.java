package generators;

import api.Abstract64RandomGenerator;

/**
 * Implementation of a xorshift PRNG with a state of 64 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class Xorshift64Generator extends Abstract64RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance fields

	protected final int[] coefficients;

	// -----------------------------------------------------------------------------
	// Instance initializers

	public Xorshift64Generator(byte[] seed, int[] coefficients) {
		super(seed);
		this.coefficients = coefficients;
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public long getRandomUniformLong() {
		for (int i = 0; i < coefficients.length; ++i) {
			if (coefficients[i] > 0) {
				this.state ^= this.state >>> coefficients[i];
			} else {
				this.state ^= this.state << -coefficients[i];
			}
		}
		return this.state;
	}

}
