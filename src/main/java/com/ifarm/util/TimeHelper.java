package com.ifarm.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeHelper {
	static Calendar calendar = Calendar.getInstance();
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat yyyyMMddHHmmssFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

	public static synchronized String secondConvertTime(long time) {
		calendar.setTimeInMillis(time * 1000);
		return format.format(calendar.getTime());
	}
	
	public static String now() {
		return yyyyMMddHHmmssFormat.format(new Date());
	}
}
