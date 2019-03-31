package generators;

import api.RandomGenerator;
import api.RandomGenerator32;
import util.ByteConverter;

/**
 * Implementation of a Xorwow PRNG with a state of 160 bits.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public class XorwowGenerator implements RandomGenerator32 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in integers.
	 */
	public static final int STATE_SIZE_INTEGERS = 5;
	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = XorwowGenerator.STATE_SIZE_INTEGERS * Integer.BYTES;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = XorwowGenerator.STATE_SIZE;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

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
	public XorwowGenerator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(XorwowGenerator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public XorwowGenerator(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return XorwowGenerator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return XorwowGenerator.STATE_SIZE;
	}

	@Override
	public byte[] getState() {
		return ByteConverter.integersToBytes(this.state);
	}

	@Override
	public void setState(final byte[] state) {
		if (state.length < XorwowGenerator.STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = ByteConverter.bytesToIntegers(state);
	}

	@Override
	public int generateUniformInteger() {
		int t = this.state[3];
		t ^= t >>> 2;
		t ^= t << 1;
		this.state[3] = this.state[2];
		this.state[2] = this.state[1];
		final int s = this.state[0];
		this.state[1] = s;
		t ^= s;
		t ^= s << 4;
		this.state[0] = t;
		this.state[4] += 362437;
		return t + this.state[4];
	}

}
