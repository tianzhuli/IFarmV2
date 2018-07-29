package com.ifarm.util;

public class ConvertData {
	/**
	 * 解包1字节无符号整数
	 * 
	 * @param packedpackge
	 * @param pointer
	 * @return
	 */
	public int getdataType1(byte[] packedpackge, int pointer) {
		short shortValue = (short) ((packedpackge[pointer] & 0xff));
		return shortValue & 0xff;
	}

	/**
	 * 解包1字节带符号整数
	 * 
	 * @param packedpackge
	 * @param pointer
	 * @return
	 */
	public int getdataType2(byte[] packedpackge, int pointer) {
		short value = (short) ((packedpackge[pointer] & 0xff));
		return value;
	}

	/**
	 * 解包2字节无符号整数
	 * 
	 * @param packedpackge
	 * @param pointer
	 * @return
	 */
	public int getdataType3(byte[] packedpackge, int pointer) {
		int value = (short) ((packedpackge[pointer] & 0xff) | (((packedpackge[pointer + 1] & 0xff) << 8)));
		return value & 0xffff;
	}

	/**
	 * 解包2字节带符号整数
	 * 
	 * @param packedpackge
	 * @param pointer
	 * @return
	 */
	public int getdataType4(byte[] packedpackge, int pointer) {
		int value = (short) (packedpackge[pointer] & 0xff) | ((packedpackge[pointer + 1] & 0xff) << 8);
		return value;
	}

	/**
	 * 解包4字节无符号整数
	 * 
	 * @param packedpackge
	 * @param pointer
	 * @return
	 */
	public long getdataType5(byte[] packedpackge, int pointer) {// 四字节无符号整数
		int value = (packedpackge[pointer] & 0xff) | ((packedpackge[pointer + 1] & 0xff) << 8) | ((packedpackge[pointer + 2] & 0xff) << 16)
				| ((packedpackge[pointer + 3] & 0xff) << 24);
		return value & 0xffffffff;
	}

	/**
	 * 解包4字节带符号整数
	 * 
	 * @param packedpackge
	 * @param pointer
	 * @return
	 */
	public long getdataType6(byte[] packedpackge, int pointer) {
		int value = (packedpackge[pointer] & 0xff) | ((packedpackge[pointer + 1] & 0xff) << 8) | ((packedpackge[pointer + 2] & 0xff) << 16)
				| ((packedpackge[pointer + 3] & 0xff) << 24);
		return value;
	}

	/**
	 * 解包4字节浮点数
	 * 
	 * @param packedpackge
	 * @param pointer
	 * @return
	 */
	public float getdataType7(byte[] packedpackge, int pointer) {
		int value2 = (Integer) (packedpackge[pointer] & 0xff) | ((packedpackge[pointer + 1] & 0xff) << 8)
				| ((packedpackge[pointer + 2] & 0xff) << 16) | ((packedpackge[pointer + 3] & 0xff) << 24);
		float value3 = Float.intBitsToFloat(value2);
		return value3;
	}

	/**
	 * 解包8字节无符号整数
	 * 
	 * @param packedpackge
	 * @param pointer
	 * @return
	 */
	public long getdataType8(byte[] packedpackge, int pointer) {
		long value = ((long) packedpackge[0] & 0xff) | (((long) packedpackge[pointer + 1] & 0xff) << 8)
				| (((long) packedpackge[pointer + 2] & 0xff) << 16) | (((long) packedpackge[pointer + 3] & 0xff) << 24)
				| (((long) packedpackge[pointer + 4] & 0xff) << 32) | (((long) packedpackge[pointer + 5] & 0xff) << 40)
				| (((long) packedpackge[pointer + 6] & 0xff) << 48) | (((long) packedpackge[pointer + 7] & 0xff) << 56);

		return value & 0xffffffffffffffffL;
	}

	/**
	 * 解包8字节第带符号整数
	 * 
	 * @param packedpackge
	 * @param pointer
	 * @return
	 */
	public long getdataType9(byte[] packedpackge, int pointer) {
		long value = ((long) packedpackge[0] & 0xff) | (((long) packedpackge[pointer + 1] & 0xff) << 8)
				| (((long) packedpackge[pointer + 2] & 0xff) << 16) | (((long) packedpackge[pointer + 3] & 0xff) << 24)
				| (((long) packedpackge[pointer + 4] & 0xff) << 32) | (((long) packedpackge[pointer + 5] & 0xff) << 40)
				| (((long) packedpackge[pointer + 6] & 0xff) << 48) | (((long) packedpackge[pointer + 7] & 0xff) << 56);
		return value;
	}

	/**
	 * 解包8字节浮点数
	 * 
	 * @param packedpackge
	 * @param pointer
	 * @return
	 */
	public double getdataType10(byte[] packedpackge, int pointer) {
		long value = ((long) packedpackge[0] & 0xff) | (((long) packedpackge[pointer + 1] & 0xff) << 8)
				| (((long) packedpackge[pointer + 2] & 0xff) << 16) | (((long) packedpackge[pointer + 3] & 0xff) << 24)
				| (((long) packedpackge[pointer + 4] & 0xff) << 32) | (((long) packedpackge[pointer + 5] & 0xff) << 40)
				| (((long) packedpackge[pointer + 6] & 0xff) << 48) | (((long) packedpackge[pointer + 7] & 0xff) << 56);

		return Double.longBitsToDouble(value);
	}

	/**
	 * 解包状态型或是指令型
	 * 
	 * @param packedpackge
	 * @param pointer
	 * @return
	 */
	public int getDataStateorConmandFromBytes(byte[] currentUnpack, int pointer) {
		byte currentdata = currentUnpack[pointer];
		int data = currentdata;
		return data;
	}

	/**
	 * 
	 * @param arr
	 * @param position
	 * @return
	 */
	public String byteToConvertString(byte[] arr, int position, int count) {
		StringBuffer buffer = new StringBuffer();
		for (int i = count - 1; i >= 0; i--) {
			buffer.append(hexStringConvert(arr[position + i]));
		}
		return buffer.toString();
	}

	public String hexStringConvert(byte b) {
		String hex = Integer.toHexString(b & 0xff);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		return hex.toUpperCase();
	}

	/**
	 * 解析数据，按照约定的id来解
	 * @param arr 数据包
	 * @param position 起始位置
	 * @param count 长度
	 * @return
	 */
	public Long byteToConvertLong(byte[] arr, int position, int count) {
		Long val = null;
		try {
			val = Long.valueOf(byteToConvertString(arr, position, count));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return val;
	}
}
