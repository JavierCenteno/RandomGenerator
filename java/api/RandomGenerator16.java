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

/**
 * This class offers default implementations for the methods of a random
 * generator that generates 16 bits at a time.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public interface RandomGenerator16 extends RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public default byte generateUniformByte() {
		return (byte) generateUniformShort();
	}

	@Override
	public short generateUniformShort();

	@Override
	public default int generateUniformInteger() {
		int short0 = generateUniformShort() & 0x0000FFFF;
		int short1 = generateUniformShort() & 0x0000FFFF;
		return short0 << 16 | short1;
	}

	@Override
	public default long generateUniformLong() {
		long short0 = generateUniformShort() & 0x000000000000FFFFL;
		long short1 = generateUniformShort() & 0x000000000000FFFFL;
		long short2 = generateUniformShort() & 0x000000000000FFFFL;
		long short3 = generateUniformShort() & 0x000000000000FFFFL;
		return short0 << 48 | short1 << 32 | short2 << 16 | short3;
	}

}
