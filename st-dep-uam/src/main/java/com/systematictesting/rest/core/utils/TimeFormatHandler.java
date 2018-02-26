package com.systematictesting.rest.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeFormatHandler {
	
	private static String DEFAULT_TIME_STAMP_FORMAT = "dd-MMM-yyyy HH:mm:ss";
	
	public static String getCurrentTimeStamp(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}
	
	public static String getCurrentTimeStamp() {
		return getCurrentTimeStamp(DEFAULT_TIME_STAMP_FORMAT);
	}
	
	public static String convertTimeStamp(String dateFormat, Long timestamp) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timestamp);
		//c.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(c.getTime());
	}
	
	public static String convertTimeStamp(Long timestamp) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timestamp);
		//c.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_TIME_STAMP_FORMAT);
		return sdf.format(c.getTime());
	}
	
	public static String now(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}
}
