package generators;

import java.math.BigInteger;

import api.RandomGenerator;
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

	////////////////////////////////////////////////////////////////////////////////
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
	private static final BigInteger MODULUS = BlumBlumShubGenerator.PRIME_1.multiply(BlumBlumShubGenerator.PRIME_2);
	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = BlumBlumShubGenerator.MODULUS.toByteArray().length;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = BlumBlumShubGenerator.STATE_SIZE;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * State of this generator. Despite being a BigInteger, it is never over 8 bytes
	 * long while numbers aren't being generated.
	 */
	private BigInteger state;

	////////////////////////////////////////////////////////////////////////////////
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
			seed = RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(BlumBlumShubGenerator.SEED_SIZE);
			bigIntegerSeed = new BigInteger(seed).abs().mod(BlumBlumShubGenerator.MODULUS);
		} while ((bigIntegerSeed.compareTo(BigInteger.ONE) <= 0) || bigIntegerSeed.equals(BlumBlumShubGenerator.PRIME_1)
				|| bigIntegerSeed.equals(BlumBlumShubGenerator.PRIME_2));
		this.setSeed(seed);
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
	public BlumBlumShubGenerator(final byte[] seed) {
		final BigInteger bigIntegerSeed = new BigInteger(seed);
		if ((bigIntegerSeed.compareTo(BigInteger.ONE) <= 0) || bigIntegerSeed.equals(BlumBlumShubGenerator.PRIME_1)
				|| bigIntegerSeed.equals(BlumBlumShubGenerator.PRIME_2)
				|| (bigIntegerSeed.compareTo(BlumBlumShubGenerator.MODULUS) > 0)) {
			throw new IllegalArgumentException();
		}
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return BlumBlumShubGenerator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return BlumBlumShubGenerator.STATE_SIZE;
	}

	@Override
	public byte[] getState() {
		return this.state.toByteArray();
	}

	@Override
	public void setState(final byte[] state) {
		if (state.length < BlumBlumShubGenerator.STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = new BigInteger(state);
	}

	@Override
	public byte generateBit() {
		this.state = this.state.pow(2).mod(BlumBlumShubGenerator.MODULUS);
		return (byte) (this.state.intValue() & 1);
	}

}
