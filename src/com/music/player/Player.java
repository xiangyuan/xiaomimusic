package com.music.player;


/**
 * @title
 * <p>播放音乐功能部分</p>
 * @author XiangYuan mailto:liyajie1209@gmail.com
 * @version 2011-7-22 下午02:21:54
 */
public interface Player {
	
	/**
	 * Start playing
	 */
	public void play();
	
	/**
	 * Checks whether player is playing or not 
	 * 
	 * @return boolean value
	 */
	public boolean isPlaying();
	
	/**
	 * Stop playing
	 */
	public void stop();
	
	/**
	 * Pause playing
	 */
	public void pause();
	
	/**
	 * Jump to the next song on the playlist
	 */
	public void next();
	
	/**
	 * Jump to the previous song on the playlist
	 */
	public void prev();
	
	/**
	 * Skip to the track on the playlist
	 * 
	 * @param index Track number on the playlist
	 */
	public void skipTo(int index);
	
//	/**
//	 * Set events listener
//	 * 
//	 * @param playerEngineListener
//	 */
//	public void setListener(PlayerEngineListener playerEngineListener);
	
	/**
	 * @param path
	 */
	public void setDataPath(String path);
}
