/*
 * This is an API for random number generation.
 * Copyright (C) 2018 Javier Centeno Vega
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package api;

import java.security.SecureRandom;

/**
 * This class offers a partial implementation of RandomGenerator for a generator
 * with 64 bits of state.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @since 1.0
 * 
 */
public abstract class Abstract64RandomGenerator implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance fields

	/**
	 * State of this generator.
	 */
	protected long state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public Abstract64RandomGenerator() {
		this(SecureRandom.getSeed(8));
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 */
	public Abstract64RandomGenerator(byte[] seed) {
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
		byte _0 = (byte) (state >>> 56);
		byte _1 = (byte) (state >>> 48);
		byte _2 = (byte) (state >>> 40);
		byte _3 = (byte) (state >>> 32);
		byte _4 = (byte) (state >>> 24);
		byte _5 = (byte) (state >>> 16);
		byte _6 = (byte) (state >>> 8);
		byte _7 = (byte) (state);
		return new byte[] { _0, _1, _2, _3, _4, _5, _6, _7 };
	}

	@Override
	public void setState(byte[] state) {
		long _0 = ((long) state[0]) << 56;
		long _1 = ((long) state[1]) << 48;
		long _2 = ((long) state[2]) << 40;
		long _3 = ((long) state[3]) << 32;
		long _4 = ((long) state[4]) << 24;
		long _5 = ((long) state[5]) << 16;
		long _6 = ((long) state[6]) << 8;
		long _7 = (long) state[7];
		this.state = (_0 | _1 | _2 | _3 | _4 | _5 | _6 | _7);
	}

	@Override
	public byte getRandomUniformByte() {
		return (byte) getRandomUniformLong();
	}

	@Override
	public short getRandomUniformShort() {
		return (short) getRandomUniformLong();
	}

	@Override
	public int getRandomUniformInteger() {
		return (int) getRandomUniformLong();
	}

	@Override
	public abstract long getRandomUniformLong();

}
