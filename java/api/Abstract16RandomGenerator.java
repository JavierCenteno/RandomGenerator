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
 * with 16 bits of state.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @since 1.0
 * 
 */
public abstract class Abstract16RandomGenerator implements RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance fields

	/**
	 * State of this generator.
	 */
	protected short state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	/**
	 * Constructs a generator with a randomly chosen seed as given by SecureRandom.
	 * 
	 * @see SecureRandom
	 */
	public Abstract16RandomGenerator() {
		this(SecureRandom.getSeed(2));
	}

	/**
	 * Constructs a generator with the given seed.
	 * 
	 * @param seed
	 *                 A seed.
	 */
	public Abstract16RandomGenerator(byte[] seed) {
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
		byte _0 = (byte) (state >>> 8);
		byte _1 = (byte) (state);
		return new byte[] { _0, _1 };
	}

	@Override
	public void setState(byte[] state) {
		short _0 = (short) (state[0] << 8);
		short _1 = (short) (state[1]);
		this.state = (short) (_0 | _1);
	}

	@Override
	public byte generateUniformByte() {
		return (byte) generateUniformShort();
	}

	@Override
	public abstract short generateUniformShort();

	@Override
	public int generateUniformInteger() {
		int _0 = generateUniformShort();
		int _1 = generateUniformShort();
		return _0 << 16 | _1;
	}

	@Override
	public long generateUniformLong() {
		long _0 = generateUniformShort();
		long _1 = generateUniformShort();
		long _2 = generateUniformShort();
		long _3 = generateUniformShort();
		return _0 << 48 | _1 << 32 | _2 << 16 | _3;
	}

}
