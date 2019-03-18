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
		return (byte) generateUniformInteger();
	}

	@Override
	public default short generateUniformShort() {
		return (short) generateUniformInteger();
	}

	@Override
	public int generateUniformInteger();

	@Override
	public default long generateUniformLong() {
		long int0 = generateUniformInteger() & 0x00000000FFFFFFFFL;
		long int1 = generateUniformInteger() & 0x00000000FFFFFFFFL;
		return int0 << 32 | int1;
	}

}
