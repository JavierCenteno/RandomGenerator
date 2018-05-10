package random;

/**
 * Implementation of a xorshift* PRNG with a state of 64 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see random.RandomSequence
 * @see random.XorshiftSequence64
 * @since 1.0
 * 
 */
public class XorshiftStarSequence64 extends XorshiftSequence64 {

	// -----------------------------------------------------------------------------
	// Instance fields

	private final long multiplier;
	private final long increment;

	// -----------------------------------------------------------------------------
	// Instance initializers

	public XorshiftStarSequence64(long seed, int[] coefficients, long multiplier, long increment) {
		super(seed, coefficients);
		this.multiplier = multiplier;
		this.increment = increment;
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public long nextUniformLong() {
		super.nextUniformLong();
		current *= multiplier;
		current += increment;
		return current;
	}

}
