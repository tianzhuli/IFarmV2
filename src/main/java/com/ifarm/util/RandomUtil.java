package com.ifarm.util;

import java.util.Random;

public class RandomUtil {

	private static final String ALL_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static Random random = new Random();
	static final int num = 10;

	public static String randomString() {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < num; i++) {
			stringBuffer.append(ALL_STRING.charAt(random.nextInt(ALL_STRING.length())));
		}
		return stringBuffer.toString();
	}

	public static String randomString(int num) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < num; i++) {
			stringBuffer.append(ALL_STRING.charAt(random.nextInt(ALL_STRING.length())));
		}
		return stringBuffer.toString();
	}

	public static Integer randomTenInteger() {
		return 100000000 + random.nextInt(900000000);
	}

	public static Integer randomInteger(int base, int size) {
		return base * 1000000 + random.nextInt((int) Math.pow(10, size));
	}

	public static Integer randomSixInteger() {
		return 100000 + random.nextInt(900000);
	}

	public static void main(String[] args) {
		System.out.println(randomInteger(181, 5));
	}
}
