package com.ifarm.util;

import java.nio.ByteBuffer;

public class ByteConvert {
	/**
	 * 按照约定将对应的String类型转成字节
	 * 
	 * @param controlDeviceId
	 *            ,flat是方向，true是正向，false是反向
	 * @return
	 */
	public static byte[] convertTobyte(String controlDeviceId, boolean flat) {
		if (controlDeviceId == null || controlDeviceId.equals("")) {
			return null;
		}
		int len = controlDeviceId.length() / 2;
		byte[] data = new byte[len];
		char[] chars = controlDeviceId.toUpperCase().toCharArray();
		if (flat) {
			for (int i = 0, j = 0; i < len; i++, j++) {
				int pos = i * 2;
				data[j] = (byte) (charToByte(chars[pos]) << 4 | charToByte(chars[pos + 1]));
			}
		} else {
			for (int i = data.length - 1, j = 0; i >= 0; i--, j++) {
				int pos = i * 2;
				data[j] = (byte) (charToByte(chars[pos]) << 4 | charToByte(chars[pos + 1]));
			}
		}
		return data;
	}

	public static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static byte[] unsignedShortToByte2(int s) {
		byte[] targets = new byte[2];
		targets[0] = (byte) (s >> 8 & 0xFF);
		targets[1] = (byte) (s & 0xFF);
		return targets;
	}

	public static byte[] intToByte4(int number) {
		byte[] arr = new byte[4];
		arr[0] = (byte) (number >> 24 & 0xff);
		arr[1] = (byte) (number >> 16 & 0xff);
		arr[2] = (byte) (number >> 8 & 0xff);
		arr[3] = (byte) (number & 0xff);
		return arr;
	}

	public static byte[] intToByte2(int number) {
		byte[] arr = new byte[2];
		arr[0] = (byte) (number & 0xff);
		arr[1] = (byte) (number >> 8 & 0xff);
		return arr;
	}

	public static byte[] intToByte1(int number) {
		byte[] arr = new byte[1];
		arr[0] = (byte) (number & 0xff);
		return arr;
	}

	public static byte[] longToByte4(long number) {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putLong(number);
		byte[] array = new byte[4];
		byte[] b = buf.array();
		for (int i = 0; i < array.length; i++) {
			array[i] = b[7 - i];
		}
		return array;
	}

	public static byte checekByte(byte[] byt, int start, int end) {
		int result = 0;
		for (int i = start; i <= end; i++) {
			result = result + byt[i] & 0xff;
		}
		return (byte) (result & 0xff);
	}

	public static byte[] terminalBitsConvert(int[] bits, int len) {
		byte[] arr = new byte[4];
		for (int i = 0; i < len; i++) {
			int sum = 0;
			for (int j = 0; j < 8; j++) {
				sum += bits[i * 8 + j] * Math.pow(2, j);
			}
			arr[i] = (byte) sum;
		}
		return arr;
	}

	public static void main(String[] arg0) {
		byte[] arr = convertTobyte("1757687", true);
		for (int i = 0; i < arr.length; i++) {
			System.out.println(Integer.toHexString(arr[i] & 0xff));
		}
	}
}
