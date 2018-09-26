package generators;

/**
 * Implementation of a xorshift+ PRNG with a state of 64 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @see generators.Xorshift64Generator
 * @since 1.0
 * 
 */
public class XorshiftPlus64Generator extends Xorshift64Generator {

	// -----------------------------------------------------------------------------
	// Instance fields

	protected final long increment;

	// -----------------------------------------------------------------------------
	// Instance initializers

	public XorshiftPlus64Generator(byte[] seed, int[] coefficients, long increment) {
		super(seed, coefficients);
		this.increment = increment;
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public long getRandomUniformLong() {
		super.getRandomUniformLong();
		this.state += increment;
		return this.state;
	}

}
