package api;

/**
 * This class offers default implementations for the methods of a random
 * generator that generates 8 bits at a time.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public interface RandomGenerator8 extends RandomGenerator {

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public byte generateUniformByte();

	@Override
	public default short generateUniformShort() {
		final int byte0 = this.generateUniformByte() & 0x000000FF;
		final int byte1 = this.generateUniformByte() & 0x000000FF;
		return (short) ((byte0 << 8) | byte1);
	}

	@Override
	public default int generateUniformInteger() {
		final int byte0 = this.generateUniformByte() & 0x000000FF;
		final int byte1 = this.generateUniformByte() & 0x000000FF;
		final int byte2 = this.generateUniformByte() & 0x000000FF;
		final int byte3 = this.generateUniformByte() & 0x000000FF;
		return (byte0 << 24) | (byte1 << 16) | (byte2 << 8) | byte3;
	}

	@Override
	public default long generateUniformLong() {
		final long byte0 = this.generateUniformByte() & 0x00000000000000FFL;
		final long byte1 = this.generateUniformByte() & 0x00000000000000FFL;
		final long byte2 = this.generateUniformByte() & 0x00000000000000FFL;
		final long byte3 = this.generateUniformByte() & 0x00000000000000FFL;
		final long byte4 = this.generateUniformByte() & 0x00000000000000FFL;
		final long byte5 = this.generateUniformByte() & 0x00000000000000FFL;
		final long byte6 = this.generateUniformByte() & 0x00000000000000FFL;
		final long byte7 = this.generateUniformByte() & 0x00000000000000FFL;
		return (byte0 << 56) | (byte1 << 48) | (byte2 << 40) | (byte3 << 32) | (byte4 << 24) | (byte5 << 16)
				| (byte6 << 8) | byte7;
	}

}
