package com.ifarm.util;

public class StringUtil {

	public static String firstCharUpper(String val) {
		char[] cs = val.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}

	public static boolean equals(String str1, String str2) {
		boolean res = false;
		if (str1 == null && str2 == null) {
			res = true;
		} else if (str1 == null) {
			res = str2.equals(str1);
		} else {
			res = str1.equals(str2);
		}
		return res;
	}
	
	public static boolean equals(String str1, Object str2) {
		if (str2 instanceof String) {
			return equals(str1, (String)str2);
		}
		return false;
	}
}
