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
 * generator that generates 64 bits at a time.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see api.RandomGenerator
 * @since 1.0
 * 
 */
public interface RandomGenerator64 extends RandomGenerator {

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public default byte generateUniformByte() {
		return (byte) generateUniformLong();
	}

	@Override
	public default short generateUniformShort() {
		return (short) generateUniformLong();
	}

	@Override
	public default int generateUniformInteger() {
		return (int) generateUniformLong();
	}

	@Override
	public long generateUniformLong();

}
