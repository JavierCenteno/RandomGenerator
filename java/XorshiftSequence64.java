package random;

/**
 * Implementation of a xorshift PRNG with a state of 64 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see random.RandomSequence
 * @since 1.0
 * 
 */
public class XorshiftSequence64 implements RandomSequence {

	// -----------------------------------------------------------------------------
	// Instance fields

	protected long current;
	protected final int[] coefficients;

	// -----------------------------------------------------------------------------
	// Instance initializers

	public XorshiftSequence64(long seed, int[] coefficients) {
		this.current = seed;
		this.coefficients = coefficients;
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public void setSeed(long seed) {
		this.current = seed;
	}

	@Override
	public long[] getState() {
		return new long[] { current };
	}

	@Override
	public void setState(long[] state) {
		this.current = state[0];
	}

	@Override
	public long nextUniformLong() {
		for (int i = 0; i < coefficients.length; ++i) {
			if (coefficients[i] > 0) {
				current ^= current >>> coefficients[i];
			} else {
				current ^= current << -coefficients[i];
			}
		}
		return current;
	}

}
