package generators;

import java.math.BigInteger;
import java.security.SecureRandom;

import api.RandomGenerator;

/**
 * Implementation of a blum blum shub PRNG.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class BlumBlumShubGenerator implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Class fields

	/**
	 * A prime.
	 */
	private static final BigInteger PRIME_1 = BigInteger.valueOf(999999999707L);
	/**
	 * A prime.
	 */
	private static final BigInteger PRIME_2 = BigInteger.valueOf(999999999517L);
	/**
	 * The product of the two primes.
	 */
	private static final BigInteger MODULUS = PRIME_1.multiply(PRIME_2);
	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = MODULUS.toByteArray().length;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = STATE_SIZE;

	// -----------------------------------------------------------------------------
	// Instance fields

	/**
	 * State of this generator. Despite being a BigInteger, it is never over 8 bytes
	 * long while numbers aren't being generated.
	 */
	private BigInteger state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public BlumBlumShubGenerator() {
		byte[] seed;
		BigInteger bigIntegerSeed;
		// Make another seed if it's 0, 1 or coprime with M
		do {
			seed = DEFAULT_SEED_GENERATOR.generateBytes(SEED_SIZE);
			bigIntegerSeed = new BigInteger(seed).abs().mod(MODULUS);
		} while (bigIntegerSeed.compareTo(BigInteger.ONE) <= 0 || bigIntegerSeed.equals(PRIME_1)
				|| bigIntegerSeed.equals(PRIME_2));
		setSeed(seed);
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short or has an
	 *                                      illegal value (0, 1, P, Q, negative or
	 *                                      bigger than M).
	 */
	public BlumBlumShubGenerator(byte[] seed) {
		BigInteger bigIntegerSeed = new BigInteger(seed);
		if (bigIntegerSeed.compareTo(BigInteger.ONE) <= 0 || bigIntegerSeed.equals(PRIME_1)
				|| bigIntegerSeed.equals(PRIME_2) || bigIntegerSeed.compareTo(MODULUS) > 0) {
			throw new IllegalArgumentException();
		}
		setSeed(seed);
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public int getSeedSize() {
		return SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return STATE_SIZE;
	}

	@Override
	public byte[] getState() {
		return state.toByteArray();
	}

	@Override
	public void setState(byte[] state) {
		if (state.length < STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = new BigInteger(state);
	}

	/**
	 * Generates a single bit.
	 * 
	 * @return A byte that is equal to either 0 or 1.
	 */
	public byte generateBit() {
		state = state.pow(2).mod(MODULUS);
		return (byte) (state.intValue() & 1);
	}

	@Override
	public byte generateByteBits(int bits) {
		if (bits < 0 || Byte.SIZE < bits) {
			throw new IllegalArgumentException();
		}
		byte result = 0;
		for (int i = 0; i < bits; ++i) {
			result |= generateBit();
		}
		return result;
	}

	@Override
	public short generateShortBits(int bits) {
		if (bits < 0 || Short.SIZE < bits) {
			throw new IllegalArgumentException();
		}
		short result = 0;
		for (int i = 0; i < bits; ++i) {
			result |= generateBit();
		}
		return result;
	}

	@Override
	public int generateIntegerBits(int bits) {
		if (bits < 0 || Integer.SIZE < bits) {
			throw new IllegalArgumentException();
		}
		int result = 0;
		for (int i = 0; i < bits; ++i) {
			result |= generateBit();
		}
		return result;
	}

	@Override
	public long generateLongBits(int bits) {
		if (bits < 0 || Long.SIZE < bits) {
			throw new IllegalArgumentException();
		}
		long result = 0;
		for (int i = 0; i < bits; ++i) {
			result |= generateBit();
		}
		return result;
	}

	@Override
	public boolean generateBoolean() {
		return generateBit() > 0;
	}

	@Override
	public byte generateUniformByte() {
		return (byte) ((generateBit() << 8) | (generateBit() << 7) | (generateBit() << 6) | (generateBit() << 5)
				| (generateBit() << 4) | (generateBit() << 3) | (generateBit() << 2) | (generateBit() << 1)
				| generateBit());
	}

	@Override
	public short generateUniformShort() {
		int byte0 = generateUniformByte() & 0x000000FF;
		int byte1 = generateUniformByte() & 0x000000FF;
		return (short) (byte0 << 8 | byte1);
	}

	@Override
	public int generateUniformInteger() {
		int byte0 = generateUniformByte() & 0x000000FF;
		int byte1 = generateUniformByte() & 0x000000FF;
		int byte2 = generateUniformByte() & 0x000000FF;
		int byte3 = generateUniformByte() & 0x000000FF;
		return byte0 << 24 | byte1 << 16 | byte2 << 8 | byte3;
	}

	@Override
	public long generateUniformLong() {
		long byte0 = generateUniformByte() & 0x00000000000000FFL;
		long byte1 = generateUniformByte() & 0x00000000000000FFL;
		long byte2 = generateUniformByte() & 0x00000000000000FFL;
		long byte3 = generateUniformByte() & 0x00000000000000FFL;
		long byte4 = generateUniformByte() & 0x00000000000000FFL;
		long byte5 = generateUniformByte() & 0x00000000000000FFL;
		long byte6 = generateUniformByte() & 0x00000000000000FFL;
		long byte7 = generateUniformByte() & 0x00000000000000FFL;
		return byte0 << 56 | byte1 << 48 | byte2 << 40 | byte3 << 32 | byte4 << 24 | byte5 << 16 | byte6 << 8 | byte7;
	}

}
