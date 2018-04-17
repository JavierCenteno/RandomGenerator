package random;

/**
 * Implementation of a linear congruential PRNG with a state of 64 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see random.RandomSequence
 * @since 1.0
 * 
 */
public class LinearCongruentialSequence64 implements RandomSequence {

	// -----------------------------------------------------------------------------
	// Instance fields

	private long current;
	private final long multiplier;
	private final long increment;

	// -----------------------------------------------------------------------------
	// Instance initializers

	public LinearCongruentialSequence64(long seed, long multiplier, long increment) {
		this.current = seed;
		this.multiplier = multiplier;
		this.increment = increment;
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
		return current = current * multiplier + increment;
	}

}
