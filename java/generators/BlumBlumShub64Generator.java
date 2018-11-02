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
	 * m is the product of the primes 67280421310721 and 274177, and equal to 2^64 +
	 * 1
	 */
	private BigInteger m = BigInteger.valueOf(67280421310721L).multiply(BigInteger.valueOf(274177L));
	/**
	 * longRange is equal to 2^64
	 */
	private BigInteger longRange = BigInteger.valueOf(1L).shiftLeft(64);

	// -----------------------------------------------------------------------------
	// Instance fields

	private BigInteger current;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public BlumBlumShub64Generator() {
		setSeed(SecureRandom.getSeed(8));
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 */
	public BlumBlumShub64Generator(byte[] seed) {
		setSeed(seed);
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public void setSeed(byte[] seed) {
		setState(seed);
	}

	@Override
	public byte[] getState() {
		return current.toByteArray();
	}

	@Override
	public void setState(byte[] state) {
		this.current = new BigInteger(state);
	}

	@Override
	public long generateUniformLong() {
		/*
		 * This generator generates numbers in [0, 2^64 + 1). We generate numbers until
		 * we have a number that isn't 2^64, that is, we have a number in [0, 2^64),
		 * which is the unsigned long range of values.
		 */
		do {
			current = current.pow(2).mod(m);
		} while (current.compareTo(longRange) == 0);
		return current.longValue();
	}

}
