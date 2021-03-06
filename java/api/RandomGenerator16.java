package api;

/**
 * This class offers default implementations for the methods of a random
 * generator that generates 16 bits at a time.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public interface RandomGenerator16 extends RandomGenerator {

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public default byte generateUniformByte() {
		return (byte) this.generateUniformShort();
	}

	@Override
	public short generateUniformShort();

	@Override
	public default int generateUniformInteger() {
		final int short0 = this.generateUniformShort() & 0x0000FFFF;
		final int short1 = this.generateUniformShort() & 0x0000FFFF;
		return (short0 << 16) | short1;
	}

	@Override
	public default long generateUniformLong() {
		final long short0 = this.generateUniformShort() & 0x000000000000FFFFL;
		final long short1 = this.generateUniformShort() & 0x000000000000FFFFL;
		final long short2 = this.generateUniformShort() & 0x000000000000FFFFL;
		final long short3 = this.generateUniformShort() & 0x000000000000FFFFL;
		return (short0 << 48) | (short1 << 32) | (short2 << 16) | short3;
	}

}
