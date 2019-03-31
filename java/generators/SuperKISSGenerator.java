package generators;

import java.util.Arrays;

import api.RandomGenerator;
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

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	private static final int STATE_SIZE = 0x200000 * Long.BYTES;
	/**
	 * Size of this generator's state, including other variables used by the
	 * generator, in bytes.
	 */
	public static final int FULL_STATE_SIZE = SuperKISSGenerator.STATE_SIZE + Integer.BYTES + (3 * Long.BYTES);
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = Long.BYTES;

	////////////////////////////////////////////////////////////////////////////////
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

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public SuperKISSGenerator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(SuperKISSGenerator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public SuperKISSGenerator(final byte[] seed) {
		this.setSeed(seed);
	}

	/**
	 * Constructs a copy of this generator.
	 *
	 * @param generator
	 *                      A generator.
	 */
	public SuperKISSGenerator(final SuperKISSGenerator generator) {
		this.state = Arrays.copyOf(generator.state, generator.state.length);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return SuperKISSGenerator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return SuperKISSGenerator.STATE_SIZE;
	}

	@Override
	public void setSeed(final byte[] seed) {
		if (seed.length < SuperKISSGenerator.SEED_SIZE) {
			throw new IllegalArgumentException();
		}
		final long longSeed = ByteConverter.bytesToLong(seed);
		this.linearCongruential ^= longSeed;
		this.xorShift ^= this.linearCongruential;
		this.state = new long[SuperKISSGenerator.STATE_SIZE / Long.BYTES];
		for (int i = 0; i < this.state.length; ++i) {
			this.state[i] = this.linearCongruential + this.xorShift;
		}
	}

	@Override
	public byte[] getState() {
		final byte[] indexBytes = ByteConverter.integerToBytes(this.index);
		final byte[] carryBytes = ByteConverter.longToBytes(this.carry);
		final byte[] xorShiftBytes = ByteConverter.longToBytes(this.xorShift);
		final byte[] linearCongruentialBytes = ByteConverter.longToBytes(this.linearCongruential);
		final byte[] stateBytes = ByteConverter.longsToBytes(this.state);
		final byte[] fullState = new byte[indexBytes.length + carryBytes.length + xorShiftBytes.length
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
	public void setState(final byte[] state) {
		if (state.length < SuperKISSGenerator.STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		final byte[] indexBytes = new byte[Integer.BYTES];
		final byte[] carryBytes = new byte[Long.BYTES];
		final byte[] xorShiftBytes = new byte[Long.BYTES];
		final byte[] linearCongruentialBytes = new byte[Long.BYTES];
		final byte[] stateBytes = new byte[SuperKISSGenerator.STATE_SIZE];
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
		this.index = (this.index + 1) % this.state.length;
		final long x = this.state[this.index];
		final long t = (x << 28) + this.carry;
		this.carry = (x >>> 36) - (t < x ? 1 : 0);
		this.state[this.index] = t - x;
		this.linearCongruential = (6906969069L * this.linearCongruential) + 13579;
		this.xorShift ^= this.xorShift << 13;
		this.xorShift ^= this.xorShift >>> 17;
		this.xorShift ^= this.xorShift << 43;
		return this.state[this.index] + this.linearCongruential + this.xorShift;
	}

}
