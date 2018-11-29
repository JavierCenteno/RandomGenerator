package generators;

import java.math.BigInteger;

import api.RandomGenerator1;

/**
 * Implementation of a blum blum shub PRNG.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class BlumBlumShubGenerator implements RandomGenerator1 {

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
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
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

	@Override
	public byte generateBit() {
		state = state.pow(2).mod(MODULUS);
		return (byte) (state.intValue() & 1);
	}

}
