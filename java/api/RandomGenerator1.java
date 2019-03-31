package api;

/**
 * This class offers default implementations for the methods of a random
 * generator that generates 1 bit at a time.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public interface RandomGenerator1 extends RandomGenerator {

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Generates a single bit.
	 *
	 * @return A byte that is equal to either 0 or 1.
	 */
	public byte generateBit();

	@Override
	public default byte generateByteBits(final int bits) {
		if ((bits < 0) || (Byte.SIZE < bits)) {
			throw new IllegalArgumentException();
		}
		byte result = 0;
		for (int i = 0; i < bits; ++i) {
			result <<= 1;
			result |= this.generateBit();
		}
		return result;
	}

	@Override
	public default short generateShortBits(final int bits) {
		if ((bits < 0) || (Short.SIZE < bits)) {
			throw new IllegalArgumentException();
		}
		short result = 0;
		for (int i = 0; i < bits; ++i) {
			result <<= 1;
			result |= this.generateBit();
		}
		return result;
	}

	@Override
	public default int generateIntegerBits(final int bits) {
		if ((bits < 0) || (Integer.SIZE < bits)) {
			throw new IllegalArgumentException();
		}
		int result = 0;
		for (int i = 0; i < bits; ++i) {
			result <<= 1;
			result |= this.generateBit();
		}
		return result;
	}

	@Override
	public default long generateLongBits(final int bits) {
		if ((bits < 0) || (Long.SIZE < bits)) {
			throw new IllegalArgumentException();
		}
		long result = 0;
		for (int i = 0; i < bits; ++i) {
			result <<= 1;
			result |= this.generateBit();
		}
		return result;
	}

	@Override
	public default boolean generateBoolean() {
		return this.generateBit() > 0;
	}

	@Override
	public default byte generateUniformByte() {
		return (byte) ((this.generateBit() << 8) | (this.generateBit() << 7) | (this.generateBit() << 6)
				| (this.generateBit() << 5) | (this.generateBit() << 4) | (this.generateBit() << 3)
				| (this.generateBit() << 2) | (this.generateBit() << 1) | this.generateBit());
	}

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
