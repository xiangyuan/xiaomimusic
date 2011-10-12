package com.music.net;

public interface IDowloadListener {

	/**
	 * 下载结束
	 */
	public void dowloadedEnded();
	
	/**
	 * 下载开始
	 */
	public void dowloadStarted();
}
