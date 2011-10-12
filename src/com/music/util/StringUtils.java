package com.music.util;

public class StringUtils {

	/**
	 * @param source
	 * @return
	 */
	public static final String replaceURL(String source) {
	//	source = source.replaceAll("\\", "");
		int len = source.lastIndexOf("/");
		String value = source.subSequence(0, len + 3) + "%20" + source.subSequence(len + 4, source.length());
		return (value);
	}
	
	/**
	 * @param source
	 * @return
	 */
	public static final String replaceSpaceURL(String source) {
		source.replace(" ", "%20");
		return (source);
	}
	/**
	 * get the music to Mbit size format
	 * @param len
	 * @return
	 */
	public static String getMusicSize(String len) {
		long mLen = Long.valueOf(len);
		mLen = (mLen/1024/1024);
		return (mLen + "M");
	}
}
