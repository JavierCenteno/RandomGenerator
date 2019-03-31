package generators;

import api.RandomGenerator;
import api.RandomGenerator64;
import util.ByteConverter;

/**
 * Implementation of a xorshift1024* PRNG with a state of 1024 bits.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public class Xorshift1024StarGenerator implements RandomGenerator64 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in integers.
	 */
	public static final int STATE_SIZE_LONGS = 16;
	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = Xorshift1024StarGenerator.STATE_SIZE_LONGS * Long.BYTES;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = Xorshift1024StarGenerator.STATE_SIZE;
	/**
	 * Size of this generator's state, including the index, in bytes.
	 */
	public static final int FULL_STATE_SIZE = Integer.BYTES + Xorshift1024StarGenerator.STATE_SIZE;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Index that points at state elements.
	 */
	private int index;
	/**
	 * State of this generator. This field is simply used for the generation of
	 * values returned by the generator and doesn't include other generator
	 * parameters that may affect the returned result, so this may not be equivalent
	 * to what the method getState() returns.
	 */
	private long[] state;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed provided by the default
	 * seed generator.
	 */
	public Xorshift1024StarGenerator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(Xorshift1024StarGenerator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public Xorshift1024StarGenerator(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return Xorshift1024StarGenerator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return Xorshift1024StarGenerator.FULL_STATE_SIZE;
	}

	@Override
	public void setSeed(final byte[] seed) {
		if (seed.length < Xorshift1024StarGenerator.SEED_SIZE) {
			throw new IllegalArgumentException();
		}
		this.index = 0;
		this.state = ByteConverter.bytesToLongs(seed);
	}

	@Override
	public byte[] getState() {
		final byte[] indexBytes = ByteConverter.integerToBytes(this.index);
		final byte[] stateBytes = ByteConverter.longsToBytes(this.state);
		final byte[] fullState = new byte[indexBytes.length + stateBytes.length];
		System.arraycopy(indexBytes, 0, fullState, 0, indexBytes.length);
		System.arraycopy(stateBytes, 0, fullState, indexBytes.length, stateBytes.length);
		return fullState;
	}

	@Override
	public void setState(final byte[] state) {
		if (state.length < Xorshift1024StarGenerator.FULL_STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		final byte[] indexBytes = new byte[Integer.BYTES];
		final byte[] stateBytes = new byte[Xorshift1024StarGenerator.STATE_SIZE];
		System.arraycopy(state, 0, indexBytes, 0, indexBytes.length);
		System.arraycopy(state, indexBytes.length, stateBytes, 0, stateBytes.length);
		this.index = ByteConverter.bytesToInteger(indexBytes);
		this.state = ByteConverter.bytesToLongs(stateBytes);
	}

	@Override
	public long generateUniformLong() {
		final long state0 = this.state[this.index];
		this.index = (this.index + 1) % Xorshift1024StarGenerator.STATE_SIZE_LONGS;
		long state1 = this.state[this.index];
		state1 ^= state1 << 31;
		state1 ^= state1 >>> 11;
		state1 ^= state0 ^ (state0 >>> 30);
		this.state[this.index] = state1;
		return state1 * 1181783497276652981L;
	}

}
