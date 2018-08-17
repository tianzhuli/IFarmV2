package com.ifarm.util;

public class ByteUtil {

	public static boolean checkByteEqual(byte[] arr, String formatter, int pos, int count) {
		byte[] compareByte = ByteConvert.convertTobyte(formatter, true);
		if (!(count == compareByte.length || arr.length == compareByte.length)) {
			return false;
		}
		for (int i = 0; i < compareByte.length; i++) {
			if (compareByte[i] != arr[pos + i]) {
				return false;
			}
		}
		return true;
	}
}
