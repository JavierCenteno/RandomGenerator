package generators;

import api.RandomGenerator;
import util.ByteConverter;

/**
 * Implementation of a complementary multiply with carry PRNG with a state of
 * 131072 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class CMWCGenerator implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Class fields

	/**
	 * Size of this generator's state in integers.
	 */
	public static final int STATE_SIZE_INTEGERS = 4096;
	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = STATE_SIZE_INTEGERS * Integer.BYTES;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = Integer.BYTES + STATE_SIZE;
	/**
	 * Size of this generator's state, including the index and the carry, in bytes.
	 */
	public static final int FULL_STATE_SIZE = 2 * Integer.BYTES + STATE_SIZE;
	/**
	 * Maximum value the carry can have.
	 */
	public static final int MAX_CARRY = 809430660;

	// -----------------------------------------------------------------------------
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

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public CMWCGenerator() {
		this(DEFAULT_SEED_GENERATOR.generateBytes(SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public CMWCGenerator(byte[] seed) {
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
		if (seed.length < SEED_SIZE) {
			throw new IllegalArgumentException();
		}
		this.index = STATE_SIZE_INTEGERS - 1;
		byte[] carryBytes = new byte[Integer.BYTES];
		byte[] stateBytes = new byte[STATE_SIZE];
		System.arraycopy(seed, 0, carryBytes, 0, carryBytes.length);
		System.arraycopy(seed, carryBytes.length, stateBytes, 0, stateBytes.length);
		this.carry = Math.abs(ByteConverter.bytesToInteger(carryBytes)) % MAX_CARRY;
		this.state = ByteConverter.bytesToIntegers(stateBytes);
	}

	@Override
	public byte[] getState() {
		byte[] indexBytes = ByteConverter.integerToBytes(index);
		byte[] carryBytes = ByteConverter.integerToBytes(carry);
		byte[] stateBytes = ByteConverter.integersToBytes(this.state);
		byte[] fullState = new byte[indexBytes.length + carryBytes.length + stateBytes.length];
		System.arraycopy(indexBytes, 0, fullState, 0, indexBytes.length);
		System.arraycopy(carryBytes, 0, fullState, indexBytes.length, carryBytes.length);
		System.arraycopy(stateBytes, 0, fullState, indexBytes.length + carryBytes.length, stateBytes.length);
		return fullState;
	}

	@Override
	public void setState(byte[] state) {
		if (state.length < FULL_STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		byte[] indexBytes = new byte[Integer.BYTES];
		byte[] cBytes = new byte[Integer.BYTES];
		byte[] stateBytes = new byte[STATE_SIZE];
		System.arraycopy(state, 0, indexBytes, 0, indexBytes.length);
		System.arraycopy(state, indexBytes.length, cBytes, 0, cBytes.length);
		System.arraycopy(state, indexBytes.length + cBytes.length, stateBytes, 0, stateBytes.length);
		this.index = ByteConverter.bytesToInteger(indexBytes);
		this.carry = ByteConverter.bytesToInteger(cBytes);
		this.state = ByteConverter.bytesToIntegers(stateBytes);
	}

	@Override
	public int generateUniformInteger() {
		index = (index + 1) % STATE_SIZE_INTEGERS;
		long t = 18782L * state[index] + carry;
		carry = (int) (t >>> 32);
		int x = (int) (t + carry);
		if (x < carry) {
			x++;
			carry++;
		}
		return state[index] = 0xfffffffe - x;
	}

	@Override
	public long generateUniformLong() {
		long int0 = generateUniformInteger() & 0x00000000FFFFFFFFL;
		long int1 = generateUniformInteger() & 0x00000000FFFFFFFFL;
		return int0 << 32 | int1;
	}

}
