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

	// -----------------------------------------------------------------------------
	// Instance fields

	private final long multiplier;
	private final long increment;

	// -----------------------------------------------------------------------------
	// Instance initializers

	public LinearCongruential64Generator(byte[] seed, long multiplier, long increment) {
		super(seed);
		this.multiplier = multiplier;
		this.increment = increment;
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public long getRandomUniformLong() {
		return this.state = this.state * multiplier + increment;
	}

}
