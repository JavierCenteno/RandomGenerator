package api;

/**
 * This class offers default implementations for the methods of a random
 * generator that generates 32 bits at a time.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public interface RandomGenerator32 extends RandomGenerator {

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public default byte generateUniformByte() {
		return (byte) this.generateUniformInteger();
	}

	@Override
	public default short generateUniformShort() {
		return (short) this.generateUniformInteger();
	}

	@Override
	public int generateUniformInteger();

	@Override
	public default long generateUniformLong() {
		final long int0 = this.generateUniformInteger() & 0x00000000FFFFFFFFL;
		final long int1 = this.generateUniformInteger() & 0x00000000FFFFFFFFL;
		return (int0 << 32) | int1;
	}

}
