package com.ramune.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLogger {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ssXXX");
	
	/**
	 * Log string with level<br>
	 * format "yyyy/MM/dd+XX:XX hh:mm:ss [level] msg"
	 * @param logLevel
	 * @param msg
	 * @return Log String
	 */
	private static String defaultLog(String logLevel,String msg) {
		StringBuilder builder = new StringBuilder();
		builder.append(FORMATTER.format(ZonedDateTime.now()))
		.append(" [").append(logLevel).append("] ")
		.append(msg);
		return builder.toString();
	}
	
	/**
	 * Print info log<br>
	 * format "yyyy/MM/dd+XX:XX hh:mm:ss [info] msg"
	 * @param msg log message
	 */
	public static void log(String msg) {
		System.out.println(defaultLog(" info", msg));
	}
	
	/**
	 * Print debug log if DEBUG_ENABLED env is true<br>
	 * format "yyyy/MM/dd+XX:XX hh:mm:ss [debug] msg"
	 * @param msg log message
	 */
	public static void debug(String msg) {
		String envValue = System.getenv("DEBUG_ENABLED");
		if(envValue != null && envValue.equals("true")) {
			System.out.println(defaultLog("debug", msg));
		}
	}
	
	/**
	 * Print warn log to System Error<br>
	 * format "yyyy/MM/dd+XX:XX hh:mm:ss [ warn] msg"
	 * @param msg log message
	 */
	public static void warn(String msg) {
		System.err.println(defaultLog(" warn", msg));
	}

	/**
	 * Print warn log and print stack trace<br>
	 * format "yyyy/MM/dd+XX:XX hh:mm:ss [ warn] msg"<br>
	 * format "yyyy/MM/dd+XX:XX hh:mm:ss [ warn] stackTrace"
	 * @param msg log message
	 * @param e happened Exception
	 */
	public static void warn(String msg,Exception e) {
		warn(msg);
		ServerLogger.warn(e.toString());
		for(StackTraceElement element : e.getStackTrace()) {
			ServerLogger.warn(element.toString());
		}
	}
}
