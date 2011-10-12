package com.music.sys;

public interface SysCons {

	int IO_EXCEPTION = 1;
	
	int NET_EXCEPTION = 2;
	
	/**
	 * 处理json数据异常
	 */
	int GET_AUDIO_JSON_HANDLER_EXCEPTION = 3;
	
	/**
	 * 加载专辑封面异常
	 */
	int LOADING_ALBUM_IMAGE_EXCEPTION = 4;
}
