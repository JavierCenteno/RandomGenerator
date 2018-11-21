package generators;

import java.math.BigInteger;
import java.security.SecureRandom;

import api.RandomGenerator;

/**
 * Implementation of a blum blum shub PRNG with a state of 64 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class BlumBlumShub64Generator implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 8;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = STATE_SIZE;
	/**
	 * m is the product of the primes 67280421310721 and 274177, and equal to 2^64 +
	 * 1
	 */
	private static final BigInteger M = BigInteger.valueOf(67280421310721L).multiply(BigInteger.valueOf(274177L));
	/**
	 * longRange is equal to 2^64
	 */
	private static final BigInteger LONG_RANGE = BigInteger.valueOf(1L).shiftLeft(64);

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
	public BlumBlumShub64Generator() {
		setSeed(SecureRandom.getSeed(SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public BlumBlumShub64Generator(byte[] seed) {
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
	public long generateUniformLong() {
		/*
		 * This generator generates numbers in [0, 2^64 + 1). We generate numbers until
		 * we have a number that isn't 2^64, that is, we have a number in [0, 2^64),
		 * which is the unsigned long range of values.
		 */
		do {
			state = state.pow(2).mod(M);
		} while (state.compareTo(LONG_RANGE) == 0);
		return state.longValue();
	}

}
