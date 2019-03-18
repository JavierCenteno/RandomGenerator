package api;

/**
 * This class offers default implementations for the methods of a random
 * generator that generates 64 bits at a time.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public interface RandomGenerator64 extends RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public default byte generateUniformByte() {
		return (byte) generateUniformLong();
	}

	@Override
	public default short generateUniformShort() {
		return (short) generateUniformLong();
	}

	@Override
	public default int generateUniformInteger() {
		return (int) generateUniformLong();
	}

	@Override
	public long generateUniformLong();

}
