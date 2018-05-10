package random;

/**
 * Implementation of a xorshift+ PRNG with a state of 64 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see random.RandomSequence
 * @see random.XorshiftSequence64
 * @since 1.0
 * 
 */
public class XorshiftPlusSequence64 extends XorshiftSequence64 {

	// -----------------------------------------------------------------------------
	// Instance fields

	protected final long increment;

	// -----------------------------------------------------------------------------
	// Instance initializers

	public XorshiftPlusSequence64(long seed, int[] coefficients, long increment) {
		super(seed, coefficients);
		this.increment = increment;
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public long nextUniformLong() {
		super.nextUniformLong();
		current += increment;
		return current;
	}

}
