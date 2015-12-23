package com.ezdi.audioplayer.logger;

public class LoggerUtil {
	private static final String APPLICATION_NAME = "ezAudioPlayerBE";

	public static String getMessage(String message) {
		return APPLICATION_NAME + "$#$" + message;
	}
}