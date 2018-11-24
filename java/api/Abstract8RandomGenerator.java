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
	// Class fields

	/**
	 * Size of this generator's state in bytes.
	 */
	public static final int STATE_SIZE = 1;
	/**
	 * Size of this generator's seed in bytes.
	 */
	public static final int SEED_SIZE = STATE_SIZE;

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
	public Abstract8RandomGenerator(byte[] seed) {
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
		return STATE_SIZE;
	}

	@Override
	public byte[] getState() {
		return new byte[] { (byte) state };
	}

	@Override
	public void setState(byte[] state) {
		if (state.length < STATE_SIZE) {
			throw new IllegalArgumentException();
		}
		this.state = state[0];
	}

	@Override
	public abstract byte generateUniformByte();

	@Override
	public short generateUniformShort() {
		int byte0 = generateUniformByte() & 0x000000FF;
		int byte1 = generateUniformByte() & 0x000000FF;
		return (short) (byte0 << 8 | byte1);
	}

	@Override
	public int generateUniformInteger() {
		int byte0 = generateUniformByte() & 0x000000FF;
		int byte1 = generateUniformByte() & 0x000000FF;
		int byte2 = generateUniformByte() & 0x000000FF;
		int byte3 = generateUniformByte() & 0x000000FF;
		return byte0 << 24 | byte1 << 16 | byte2 << 8 | byte3;
	}

	@Override
	public long generateUniformLong() {
		long byte0 = generateUniformByte() & 0x00000000000000FFL;
		long byte1 = generateUniformByte() & 0x00000000000000FFL;
		long byte2 = generateUniformByte() & 0x00000000000000FFL;
		long byte3 = generateUniformByte() & 0x00000000000000FFL;
		long byte4 = generateUniformByte() & 0x00000000000000FFL;
		long byte5 = generateUniformByte() & 0x00000000000000FFL;
		long byte6 = generateUniformByte() & 0x00000000000000FFL;
		long byte7 = generateUniformByte() & 0x00000000000000FFL;
		return byte0 << 56 | byte1 << 48 | byte2 << 40 | byte3 << 32 | byte4 << 24 | byte5 << 16 | byte6 << 8 | byte7;
	}

}
