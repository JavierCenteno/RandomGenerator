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
 * with 8 bits of state.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @since 1.0
 * 
 */
public abstract class Abstract8RandomGenerator implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance fields

	/**
	 * State of this generator.
	 */
	protected byte state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public Abstract8RandomGenerator() {
		this(SecureRandom.getSeed(1));
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 */
	public Abstract8RandomGenerator(byte[] seed) {
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
		return new byte[] { (byte) state };
	}

	@Override
	public void setState(byte[] state) {
		this.state = state[0];
	}

	@Override
	public abstract byte getRandomUniformByte();

	@Override
	public short getRandomUniformShort() {
		short _0 = getRandomUniformByte();
		short _1 = getRandomUniformByte();
		return (short) (_0 << 8 | _1);
	}

	@Override
	public int getRandomUniformInteger() {
		int _0 = getRandomUniformByte();
		int _1 = getRandomUniformByte();
		int _2 = getRandomUniformByte();
		int _3 = getRandomUniformByte();
		return _0 << 24 | _1 << 16 | _2 << 8 | _3;
	}

	@Override
	public long getRandomUniformLong() {
		long _0 = getRandomUniformByte();
		long _1 = getRandomUniformByte();
		long _2 = getRandomUniformByte();
		long _3 = getRandomUniformByte();
		long _4 = getRandomUniformByte();
		long _5 = getRandomUniformByte();
		long _6 = getRandomUniformByte();
		long _7 = getRandomUniformByte();
		return _0 << 56 | _1 << 48 | _2 << 40 | _3 << 32 | _4 << 24 | _5 << 16 | _6 << 8 | _7;
	}

}
