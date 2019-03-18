package util;

public class ByteConverter {

	////////////////////////////////////////////////////////////////////////////////
	// From byte array methods

	public static short bytesToShort(byte... from) {
		short to = 0;
		for (int i = 0; i < from.length || i < Short.BYTES; ++i) {
			to |= (((short) (from[i] & 0xFF)) << (8 * (Short.BYTES - 1 - (i % Short.BYTES))));
		}
		return to;
	}

	public static int bytesToInteger(byte... from) {
		int to = 0;
		for (int i = 0; i < from.length || i < Integer.BYTES; ++i) {
			to |= ((int) (from[i] & 0xFF)) << (8 * (Integer.BYTES - 1 - (i % Integer.BYTES)));
		}
		return to;
	}

	public static long bytesToLong(byte... from) {
		long to = 0;
		for (int i = 0; i < from.length || i < Long.BYTES; ++i) {
			to |= ((long) (from[i] & 0xFF)) << (8 * (Long.BYTES - 1 - (i % Long.BYTES)));
		}
		return to;
	}

	public static short[] bytesToShorts(byte... from) {
		short[] to = new short[(from.length + 1) / Short.BYTES];
		for (int i = 0; i < from.length; ++i) {
			to[i / Short.BYTES] |= (((short) (from[i] & 0xFF)) << (8 * (Short.BYTES - 1 - (i % Short.BYTES))));
		}
		return to;
	}

	public static int[] bytesToIntegers(byte... from) {
		int[] to = new int[(from.length + 1) / Integer.BYTES];
		for (int i = 0; i < from.length; ++i) {
			to[i / Integer.BYTES] |= ((int) (from[i] & 0xFF)) << (8 * (Integer.BYTES - 1 - (i % Integer.BYTES)));
		}
		return to;
	}

	public static long[] bytesToLongs(byte... from) {
		long[] to = new long[(from.length + 1) / Long.BYTES];
		for (int i = 0; i < from.length; ++i) {
			to[i / Long.BYTES] |= ((long) (from[i] & 0xFF)) << (8 * (Long.BYTES - 1 - (i % Long.BYTES)));
		}
		return to;
	}

	////////////////////////////////////////////////////////////////////////////////
	// To byte array methods

	public static byte[] shortToBytes(short from) {
		byte[] to = new byte[Short.BYTES];
		to[0] = (byte) (from >> 8);
		to[1] = (byte) from;
		return to;
	}

	public static byte[] integerToBytes(int from) {
		byte[] to = new byte[Integer.BYTES];
		to[0] = (byte) (from >> 24);
		to[1] = (byte) (from >> 16);
		to[2] = (byte) (from >> 8);
		to[3] = (byte) from;
		return to;
	}

	public static byte[] longToBytes(long from) {
		byte[] to = new byte[Long.BYTES];
		to[0] = (byte) (from >> 56);
		to[1] = (byte) (from >> 48);
		to[2] = (byte) (from >> 40);
		to[3] = (byte) (from >> 32);
		to[4] = (byte) (from >> 24);
		to[5] = (byte) (from >> 16);
		to[6] = (byte) (from >> 8);
		to[7] = (byte) from;
		return to;
	}

	public static byte[] shortsToBytes(short... from) {
		byte[] to = new byte[from.length * Short.BYTES];
		for (int i = 0, j = 0; i < from.length; i++) {
			to[j++] = (byte) (from[i] >> 8);
			to[j++] = (byte) from[i];
		}
		return to;
	}

	public static byte[] integersToBytes(int... from) {
		byte[] to = new byte[from.length * Integer.BYTES];
		for (int i = 0, j = 0; i < from.length; i++) {
			to[j++] = (byte) (from[i] >> 24);
			to[j++] = (byte) (from[i] >> 16);
			to[j++] = (byte) (from[i] >> 8);
			to[j++] = (byte) from[i];
		}
		return to;
	}

	public static byte[] longsToBytes(long... from) {
		byte[] to = new byte[from.length * Long.BYTES];
		for (int i = 0, j = 0; i < from.length; i++) {
			to[j++] = (byte) (from[i] >> 56);
			to[j++] = (byte) (from[i] >> 48);
			to[j++] = (byte) (from[i] >> 40);
			to[j++] = (byte) (from[i] >> 32);
			to[j++] = (byte) (from[i] >> 24);
			to[j++] = (byte) (from[i] >> 16);
			to[j++] = (byte) (from[i] >> 8);
			to[j++] = (byte) from[i];
		}
		return to;
	}

}
