package com.music.core;

/**
 * @title
 * <p>播放网络音乐</>
 * @author XiangYuan mailto:liyajie1209@gmail.com
 * @version 2011-7-22 上午10:22:49
 */
public interface IPlayNetwork {

	/**
	 * 得到音乐时长
	 * @return
	 */
	public int getDuration();
	
	/**
	 * current position
	 * @return
	 */
	public int getCurPostion();
	
	/**
	 * is play now
	 * @return
	 */
	public boolean isPlay();
	
	/**
	 * 
	 * @return
	 */
	public boolean isPause();
	
	/**
	 * release resources
	 */
	public void release();
	
	/**
	 * start to play
	 */
	public void start();
	
	/**
	 * @param path
	 */
	public void play(String path );
}
