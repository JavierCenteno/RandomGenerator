package generators;

import java.security.SecureRandom;

import api.RandomGenerator;
import util.ByteConverter;

/**
 * Implementation of a WELL 512 a PRNG with a state of 512 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class WELL512AGenerator implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 64;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = STATE_SIZE;
	/**
	 * Size of this generator's state, including the index, in bytes.
	 */
	public static final int FULL_STATE_SIZE = Integer.BYTES + STATE_SIZE;

	// -----------------------------------------------------------------------------
	// Instance fields

	/**
	 * Index used to access elements of this generator.
	 */
	private int index;
	/**
	 * State of this generator. This field is simply used for the generation of
	 * values returned by the generator and doesn't include other generator
	 * parameters that may affect the returned result, so this may not be equivalent
	 * to what the method getState() returns.
	 */
	private int[] state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public WELL512AGenerator() {
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
	public WELL512AGenerator(byte[] seed) {
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
		return FULL_STATE_SIZE;
	}

	@Override
	public void setSeed(byte[] seed) {
		this.index = 0;
		this.state = ByteConverter.bytesToIntegers(seed);
	}

	@Override
	public byte[] getState() {
		byte[] indexBytes = ByteConverter.integerToBytes(index);
		byte[] stateBytes = ByteConverter.integersToBytes(this.state);
		byte[] fullState = new byte[indexBytes.length + stateBytes.length];
		System.arraycopy(indexBytes, 0, fullState, 0, indexBytes.length);
		System.arraycopy(stateBytes, 0, fullState, indexBytes.length, stateBytes.length);
		return fullState;
	}

	@Override
	public void setState(byte[] state) {
		if (state.length < FULL_STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		byte[] indexBytes = new byte[Integer.BYTES];
		byte[] stateBytes = new byte[STATE_SIZE];
		System.arraycopy(state, 0, indexBytes, 0, indexBytes.length);
		System.arraycopy(state, indexBytes.length, stateBytes, 0, stateBytes.length);
		this.index = ByteConverter.bytesToInteger(indexBytes);
		this.state = ByteConverter.bytesToIntegers(stateBytes);
	}

	@Override
	public int generateUniformInteger() {
		int index0 = index;
		int index9 = (index + 9) % 16;
		int index13 = (index + 13) % 16;
		int index15 = (index + 15) % 16;
		int z0 = state[index15];
		int z1 = (state[index0] ^ (state[index0] << 16)) ^ (state[index13] ^ (state[index13] << 15));
		int z2 = state[index9] ^ (state[index9] >>> 11);
		state[index0] = z1 ^ z2;
		state[index15] = (z0 ^ (z0 << 2)) ^ (z1 ^ (z1 << 18)) ^ (z2 << 28)
				^ (state[index0] ^ ((state[index0] << 5) & 0xDA442D24));
		index = index15;
		return state[index];
	}

	@Override
	public long generateUniformLong() {
		long int0 = generateUniformInteger() & 0x00000000FFFFFFFFL;
		long int1 = generateUniformInteger() & 0x00000000FFFFFFFFL;
		return int0 << 32 | int1;
	}

}