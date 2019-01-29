package generators;

import java.util.Arrays;

import api.RandomGenerator64;
import util.ByteConverter;

/**
 * Implementation of a SuperKISS PRNG with a state of 512 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public class SuperKISSGenerator implements RandomGenerator64 {

	// -----------------------------------------------------------------------------
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	private static final int STATE_SIZE = 0x200000 * Long.BYTES;
	/**
	 * Size of this generator's state, including other variables used by the
	 * generator, in bytes.
	 */
	public static final int FULL_STATE_SIZE = STATE_SIZE + Integer.BYTES + 3 * Long.BYTES;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = Long.BYTES;

	// -----------------------------------------------------------------------------
	// Instance fields

	/**
	 * State of this generator.
	 */
	private long[] state;
	/**
	 * Index that points at state elements.
	 */
	private int index;
	/**
	 * Carry.
	 */
	private long carry;
	/**
	 * State of the xorshift generator used by this generator.
	 */
	private long xorShift;
	/**
	 * State of the linear congruential generator used by this generator.
	 */
	private long linearCongruential;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public SuperKISSGenerator() {
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
	public SuperKISSGenerator(byte[] seed) {
		setSeed(seed);
	}

	/**
	 * Constructs a copy of this generator.
	 * 
	 * @param generator
	 *                      A generator.
	 */
	public SuperKISSGenerator(SuperKISSGenerator generator) {
		this.state = Arrays.copyOf(generator.state, generator.state.length);
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
	public void setSeed(byte[] seed) {
		if (seed.length < SEED_SIZE) {
			throw new IllegalArgumentException();
		}
		long longSeed = ByteConverter.bytesToLong(seed);
		this.linearCongruential ^= longSeed;
		this.xorShift ^= this.linearCongruential;
		this.state = new long[STATE_SIZE / Long.BYTES];
		for (int i = 0; i < state.length; ++i) {
			state[i] = linearCongruential + xorShift;
		}
	}

	@Override
	public byte[] getState() {
		byte[] indexBytes = ByteConverter.integerToBytes(index);
		byte[] carryBytes = ByteConverter.longToBytes(carry);
		byte[] xorShiftBytes = ByteConverter.longToBytes(xorShift);
		byte[] linearCongruentialBytes = ByteConverter.longToBytes(linearCongruential);
		byte[] stateBytes = ByteConverter.longsToBytes(this.state);
		byte[] fullState = new byte[indexBytes.length + carryBytes.length + xorShiftBytes.length
				+ linearCongruentialBytes.length + stateBytes.length];
		System.arraycopy(indexBytes, 0, fullState, 0, indexBytes.length);
		System.arraycopy(carryBytes, 0, fullState, indexBytes.length, carryBytes.length);
		System.arraycopy(xorShiftBytes, 0, fullState, indexBytes.length + carryBytes.length, xorShiftBytes.length);
		System.arraycopy(linearCongruentialBytes, 0, fullState,
				indexBytes.length + carryBytes.length + xorShiftBytes.length, linearCongruentialBytes.length);
		System.arraycopy(stateBytes, 0, fullState,
				indexBytes.length + carryBytes.length + xorShiftBytes.length + linearCongruentialBytes.length,
				stateBytes.length);
		return fullState;
	}

	@Override
	public void setState(byte[] state) {
		if (state.length < STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		byte[] indexBytes = new byte[Integer.BYTES];
		byte[] carryBytes = new byte[Long.BYTES];
		byte[] xorShiftBytes = new byte[Long.BYTES];
		byte[] linearCongruentialBytes = new byte[Long.BYTES];
		byte[] stateBytes = new byte[STATE_SIZE];
		System.arraycopy(state, 0, indexBytes, 0, indexBytes.length);
		System.arraycopy(state, indexBytes.length, carryBytes, 0, carryBytes.length);
		System.arraycopy(state, indexBytes.length + carryBytes.length, xorShiftBytes, 0, xorShiftBytes.length);
		System.arraycopy(state, indexBytes.length + carryBytes.length + xorShiftBytes.length, linearCongruentialBytes,
				0, linearCongruentialBytes.length);
		System.arraycopy(state,
				indexBytes.length + carryBytes.length + xorShiftBytes.length + linearCongruentialBytes.length,
				stateBytes, 0, stateBytes.length);
		this.index = ByteConverter.bytesToInteger(indexBytes);
		this.carry = ByteConverter.bytesToLong(carryBytes);
		this.xorShift = ByteConverter.bytesToLong(xorShiftBytes);
		this.linearCongruential = ByteConverter.bytesToLong(linearCongruentialBytes);
		this.state = ByteConverter.bytesToLongs(stateBytes);
	}

	@Override
	public long generateUniformLong() {
		index = (index + 1) % state.length;
		long x = state[index];
		long t = (x << 28) + carry;
		carry = (x >>> 36) - (t < x ? 1 : 0);
		state[index] = t - x;
		linearCongruential = 6906969069L * linearCongruential + 13579;
		xorShift ^= xorShift << 13;
		xorShift ^= xorShift >>> 17;
		xorShift ^= xorShift << 43;
		return state[index] + linearCongruential + xorShift;
	}

}
