package random;

/**
 * Implementation of a Lehmer PRNG with a state of 16 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see random.RandomSequence
 * @see random.XorshiftSequence64
 * @since 1.0
 * 
 */
public class LehmerSequence16 implements RandomSequence {

	// -----------------------------------------------------------------------------
	// Class fields

	private static final int N = 65537;
	private static final int G = 75;

	// -----------------------------------------------------------------------------
	// Instance fields

	private short current;

	// -----------------------------------------------------------------------------
	// Instance initializers

	public LehmerSequence16(short seed) {
		this.current = seed;
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public void setSeed(long seed) {
		this.current = (short) seed;
	}

	@Override
	public long[] getState() {
		return new long[] { current };
	}

	@Override
	public void setState(long[] state) {
		this.current = (short) state[0];
	}

	@Override
	public short nextUniformShort() {
		int next = current;
		do {
			next = G * next % N;
		} while (next == 65536);
		return current = (short) next;
	}

	@Override
	public int nextUniformInteger() {
		int a = nextUniformShort();
		int b = nextUniformShort();
		return a << 16 & b;
	}

	@Override
	public long nextUniformLong() {
		long a = nextUniformInteger();
		long b = nextUniformInteger();
		return a << 32 & b;
	}

}
