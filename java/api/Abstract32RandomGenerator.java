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
 * with 32 bits of state.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @since 1.0
 * 
 */
public abstract class Abstract32RandomGenerator implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance fields

	/**
	 * State of this generator.
	 */
	protected int state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public Abstract32RandomGenerator() {
		this(SecureRandom.getSeed(4));
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 */
	public Abstract32RandomGenerator(byte[] seed) {
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
		byte _0 = (byte) (state >>> 24);
		byte _1 = (byte) (state >>> 16);
		byte _2 = (byte) (state >>> 8);
		byte _3 = (byte) (state);
		return new byte[] { _0, _1, _2, _3 };
	}

	@Override
	public void setState(byte[] state) {
		int _0 = (int) (state[0] << 24);
		int _1 = (int) (state[1] << 16);
		int _2 = (int) (state[2] << 8);
		int _3 = (int) (state[3]);
		this.state = (_0 | _1 | _2 | _3);
	}

	@Override
	public byte getRandomUniformByte() {
		return (byte) getRandomUniformInteger();
	}

	@Override
	public short getRandomUniformShort() {
		return (short) getRandomUniformInteger();
	}

	@Override
	public abstract int getRandomUniformInteger();

	@Override
	public long getRandomUniformLong() {
		long _0 = getRandomUniformInteger();
		long _1 = getRandomUniformInteger();
		return _0 << 32 | _1;
	}

}
