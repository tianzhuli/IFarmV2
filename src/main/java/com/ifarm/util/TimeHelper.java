package com.ifarm.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeHelper {
	static Calendar calendar = Calendar.getInstance();
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static synchronized String secondConvertTime(long time) {
		calendar.setTimeInMillis(time * 1000);
		return format.format(calendar.getTime());
	}
}
