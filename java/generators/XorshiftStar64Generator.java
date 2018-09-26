package generators;

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
public class XorshiftStar64Generator extends Xorshift64Generator {

	// -----------------------------------------------------------------------------
	// Instance fields

	private final long multiplier;
	private final long increment;

	// -----------------------------------------------------------------------------
	// Instance initializers

	public XorshiftStar64Generator(byte[] seed, int[] coefficients, long multiplier, long increment) {
		super(seed, coefficients);
		this.multiplier = multiplier;
		this.increment = increment;
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public long getRandomUniformLong() {
		super.getRandomUniformLong();
		this.state *= multiplier;
		this.state += increment;
		return this.state;
	}

}
