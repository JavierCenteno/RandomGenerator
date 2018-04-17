/*
 * Original Mersenne Twister 19937 copyright notice:
 * 
 * Copyright (C) 1997 - 2002, Makoto Matsumoto and Takuji Nishimura, All rights
 * reserved. Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. The names of its contributors may not be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package random;

/**
 * Implementation of a Mersenne Twister 19937 PRNG with a state of 64 bits.
 * 
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 1.0
 * @see random.RandomSequence
 * @since 1.0
 * 
 */
public class MersenneTwister19937Sequence64 implements RandomSequence {

	// -----------------------------------------------------------------------------
	// Class fields

	private static final int STATE_SIZE = 312;
	private static final int SHIFT_SIZE = 156;
	private static final long UPPER_MASK = 0xFF_FF_FF_FF_80_00_00_00L;
	private static final long LOWER_MASK = 0x00_00_00_00_7F_FF_FF_FFL;
	private static final long XOR_MASK = 0xB5_02_6F_5A_A9_66_19_E9L;

	// -----------------------------------------------------------------------------
	// Instance fields

	private int index;
	private long[] state;

	// -----------------------------------------------------------------------------
	// Instance initializers

	public MersenneTwister19937Sequence64(long seed) {
		setSeed(seed);
	}

	// -----------------------------------------------------------------------------
	// Instance methods

	@Override
	public void setSeed(long seed) {
		state = new long[STATE_SIZE];
		index = 0;
		state[index] = seed;
		while (index < STATE_SIZE - 1) {
			long state_i = state[index];
			state_i ^= state_i >>> 12;
			state_i ^= state_i << 25;
			state_i ^= state_i >>> 27;
			state_i *= 6364136223846793005L;
			state_i += 1442695040888963407L;
			++index;
			state[index] = state_i;
		}
		index = 0;
	}

	@Override
	public long[] getState() {
		return state;
	}

	@Override
	public void setState(long[] state) {
		this.state = state;
	}

	@Override
	public long nextUniformLong() {
		if (index == STATE_SIZE) {
			int i = 0;
			while (i < SHIFT_SIZE) {
				long x = (state[i] & UPPER_MASK) | (state[i + 1] & LOWER_MASK);
				state[i] = state[i + SHIFT_SIZE] ^ (x >>> 1);
				if (x % 2 == 0) {
					state[i] ^= XOR_MASK;
				}
				++i;
			}
			while (i < STATE_SIZE - 1) {
				long x = (state[i] & UPPER_MASK) | (state[i + 1] & LOWER_MASK);
				state[i] = state[i + SHIFT_SIZE - STATE_SIZE] ^ (x >>> 1);
				if (x % 2 == 0) {
					state[i] ^= XOR_MASK;
				}
				++i;
			}
			long x = (state[STATE_SIZE - 1] & UPPER_MASK) | (state[0] & LOWER_MASK);
			state[STATE_SIZE - 1] = state[SHIFT_SIZE - 1] ^ (x >>> 1);
			if (x % 2 == 0) {
				state[STATE_SIZE - 1] ^= XOR_MASK;
			}
			index = 0;
		}
		long x = state[index];
		x ^= (x >> 29) & 0x5555555555555555L;
		x ^= (x << 17) & 0x71D67FFFEDA60000L;
		x ^= (x << 37) & 0xFFF7EEE000000000L;
		x ^= (x >> 43);
		++index;
		return x;
	}

}
