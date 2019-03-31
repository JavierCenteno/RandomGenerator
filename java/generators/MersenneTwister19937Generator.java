package generators;

import api.RandomGenerator;
import api.RandomGenerator64;
import util.ByteConverter;

/**
 * Implementation of a Mersenne Twister 19937 PRNG with a state of 64 bits.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 *
 */
public class MersenneTwister19937Generator implements RandomGenerator64 {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Size of this generator's state in longs.
	 */
	public static final int STATE_SIZE_LONGS = 312;
	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = MersenneTwister19937Generator.STATE_SIZE_LONGS * Long.BYTES;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = MersenneTwister19937Generator.STATE_SIZE;
	/**
	 * Size of this generator's state, including the index, in bytes.
	 */
	public static final int FULL_STATE_SIZE = Integer.BYTES + MersenneTwister19937Generator.STATE_SIZE;
	private static final int SHIFT_SIZE = 156;
	private static final long UPPER_MASK = 0xFF_FF_FF_FF_80_00_00_00L;
	private static final long LOWER_MASK = 0x00_00_00_00_7F_FF_FF_FFL;
	private static final long XOR_MASK = 0xB5_02_6F_5A_A9_66_19_E9L;

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
	public MersenneTwister19937Generator() {
		this(RandomGenerator.DEFAULT_SEED_GENERATOR.generateBytes(MersenneTwister19937Generator.SEED_SIZE));
	}

	/**
	 * Constructs a generator with the given seed.
	 *
	 * @param seed
	 *                 A seed.
	 * @throws IllegalArgumentException
	 *                                      If the seed is too short.
	 */
	public MersenneTwister19937Generator(final byte[] seed) {
		this.setSeed(seed);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int getSeedSize() {
		return MersenneTwister19937Generator.SEED_SIZE;
	}

	@Override
	public int getStateSize() {
		return MersenneTwister19937Generator.FULL_STATE_SIZE;
	}

	@Override
	public void setSeed(final byte[] seed) {
		if (seed.length < MersenneTwister19937Generator.SEED_SIZE) {
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
		if (state.length < MersenneTwister19937Generator.FULL_STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		final byte[] indexBytes = new byte[Integer.BYTES];
		final byte[] stateBytes = new byte[MersenneTwister19937Generator.STATE_SIZE];
		System.arraycopy(state, 0, indexBytes, 0, indexBytes.length);
		System.arraycopy(state, indexBytes.length, stateBytes, 0, stateBytes.length);
		this.index = ByteConverter.bytesToInteger(indexBytes);
		this.state = ByteConverter.bytesToLongs(stateBytes);
	}

	@Override
	public long generateUniformLong() {
		if (this.index == MersenneTwister19937Generator.STATE_SIZE_LONGS) {
			int i = 0;
			while (i < MersenneTwister19937Generator.SHIFT_SIZE) {
				final long x = (this.state[i] & MersenneTwister19937Generator.UPPER_MASK)
						| (this.state[i + 1] & MersenneTwister19937Generator.LOWER_MASK);
				this.state[i] = this.state[i + MersenneTwister19937Generator.SHIFT_SIZE] ^ (x >>> 1);
				if ((x % 2) == 0) {
					this.state[i] ^= MersenneTwister19937Generator.XOR_MASK;
				}
				++i;
			}
			while (i < (MersenneTwister19937Generator.STATE_SIZE_LONGS - 1)) {
				final long x = (this.state[i] & MersenneTwister19937Generator.UPPER_MASK)
						| (this.state[i + 1] & MersenneTwister19937Generator.LOWER_MASK);
				this.state[i] = this.state[(i + MersenneTwister19937Generator.SHIFT_SIZE)
						- MersenneTwister19937Generator.STATE_SIZE_LONGS] ^ (x >>> 1);
				if ((x % 2) == 0) {
					this.state[i] ^= MersenneTwister19937Generator.XOR_MASK;
				}
				++i;
			}
			final long x = (this.state[MersenneTwister19937Generator.STATE_SIZE_LONGS - 1]
					& MersenneTwister19937Generator.UPPER_MASK)
					| (this.state[0] & MersenneTwister19937Generator.LOWER_MASK);
			this.state[MersenneTwister19937Generator.STATE_SIZE_LONGS
					- 1] = this.state[MersenneTwister19937Generator.SHIFT_SIZE - 1] ^ (x >>> 1);
			if ((x % 2) == 0) {
				this.state[MersenneTwister19937Generator.STATE_SIZE_LONGS
						- 1] ^= MersenneTwister19937Generator.XOR_MASK;
			}
			this.index = 0;
		}
		long x = this.state[this.index];
		x ^= (x >> 29) & 0x5555555555555555L;
		x ^= (x << 17) & 0x71D67FFFEDA60000L;
		x ^= (x << 37) & 0xFFF7EEE000000000L;
		x ^= (x >> 43);
		++this.index;
		return x;
	}

}
