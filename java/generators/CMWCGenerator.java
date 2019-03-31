package generators;

import api.RandomGenerator;
import api.RandomGenerator32;
import util.ByteConverter;

/**
 * Implementation of a complementary-multiply-with-carry PRNG with a state of
 * 131072 bits.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public class CMWCGenerator implements RandomGenerator32 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in integers.
	 */
	public static final int STATE_SIZE_INTEGERS = 4096;
	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = CMWCGenerator.STATE_SIZE_INTEGERS * Integer.BYTES;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = Integer.BYTES + CMWCGenerator.STATE_SIZE;
	/**
	 * Size of this generator's state, including the index and the carry, in bytes.
	 */
	public static final int FULL_STATE_SIZE = (2 * Integer.BYTES) + CMWCGenerator.STATE_SIZE;
	/**
	 * Maximum value the carry can have.
	 */
	public static final int MAX_CARRY = 809430660;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Index that points at state elements.
	 */
	private int index;
	/**
	 * Value of the carry.
	 */
	private int carry;
	/**
	 * State of this generator.
	 */
	private int[] state;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public CMWCGenerator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(CMWCGenerator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public CMWCGenerator(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return CMWCGenerator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return CMWCGenerator.FULL_STATE_SIZE;
	}

	@Override
	public void setSeed(final byte[] seed) {
		if (seed.length < CMWCGenerator.SEED_SIZE) {
			throw new IllegalArgumentException();
		}
		this.index = CMWCGenerator.STATE_SIZE_INTEGERS - 1;
		final byte[] carryBytes = new byte[Integer.BYTES];
		final byte[] stateBytes = new byte[CMWCGenerator.STATE_SIZE];
		System.arraycopy(seed, 0, carryBytes, 0, carryBytes.length);
		System.arraycopy(seed, carryBytes.length, stateBytes, 0, stateBytes.length);
		this.carry = Math.abs(ByteConverter.bytesToInteger(carryBytes)) % CMWCGenerator.MAX_CARRY;
		this.state = ByteConverter.bytesToIntegers(stateBytes);
	}

	@Override
	public byte[] getState() {
		final byte[] indexBytes = ByteConverter.integerToBytes(this.index);
		final byte[] carryBytes = ByteConverter.integerToBytes(this.carry);
		final byte[] stateBytes = ByteConverter.integersToBytes(this.state);
		final byte[] fullState = new byte[indexBytes.length + carryBytes.length + stateBytes.length];
		System.arraycopy(indexBytes, 0, fullState, 0, indexBytes.length);
		System.arraycopy(carryBytes, 0, fullState, indexBytes.length, carryBytes.length);
		System.arraycopy(stateBytes, 0, fullState, indexBytes.length + carryBytes.length, stateBytes.length);
		return fullState;
	}

	@Override
	public void setState(final byte[] state) {
		if (state.length < CMWCGenerator.FULL_STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		final byte[] indexBytes = new byte[Integer.BYTES];
		final byte[] cBytes = new byte[Integer.BYTES];
		final byte[] stateBytes = new byte[CMWCGenerator.STATE_SIZE];
		System.arraycopy(state, 0, indexBytes, 0, indexBytes.length);
		System.arraycopy(state, indexBytes.length, cBytes, 0, cBytes.length);
		System.arraycopy(state, indexBytes.length + cBytes.length, stateBytes, 0, stateBytes.length);
		this.index = ByteConverter.bytesToInteger(indexBytes);
		this.carry = ByteConverter.bytesToInteger(cBytes);
		this.state = ByteConverter.bytesToIntegers(stateBytes);
	}

	@Override
	public int generateUniformInteger() {
		this.index = (this.index + 1) % CMWCGenerator.STATE_SIZE_INTEGERS;
		final long t = (18782L * this.state[this.index]) + this.carry;
		this.carry = (int) (t >>> 32);
		int x = (int) (t + this.carry);
		if (x < this.carry) {
			x++;
			this.carry++;
		}
		return this.state[this.index] = 0xfffffffe - x;
	}

}
