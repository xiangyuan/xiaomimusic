package com.music.core;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * @title 处理多实例播放
 * @author XiangYuan mailto:liyajie1209@gmail.com
 * @version 2011-7-21 下午07:33:37
 */
@Deprecated
public class MusicPlayer {

//	private boolean isPlay;
	private Context mContext;
	private boolean mIsInitialized = false;
	private MediaPlayer mMediaPlayer;
	@SuppressWarnings("unused")
	private String mPath;

	public MusicPlayer(Context context) {
		MediaPlayer localMediaPlayer = new MediaPlayer();
		this.mMediaPlayer = localMediaPlayer;
//		this.isPlay = false;
		this.mContext = context;
	}

	public int duration() {
		if (mIsInitialized) {
			return mMediaPlayer.getDuration();
		}
		return 0;
	}

	public boolean isInitialized() {
		return this.mIsInitialized;
	}

	public boolean isPlaying() {
		return this.mMediaPlayer.isPlaying();
	}

	public void pause() {
		this.mMediaPlayer.pause();
	}

	public int position() {
		if (mIsInitialized) {
			return mMediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	public void release() {
		stop();
		this.mMediaPlayer.release();
	}

	public long seek(long pos) {
		try {
			MediaPlayer localMediaPlayer = this.mMediaPlayer;
			localMediaPlayer.seekTo((int) pos);
			return pos;
		} catch (IllegalStateException localIllegalStateException) {
			while (true)
				localIllegalStateException.printStackTrace();
		}
	}

	public void setDataSourceAsync(String path) {
		try {
			this.mPath = path;
			this.mMediaPlayer.reset();
			if (path.startsWith("content://")) {
				Uri localUri = Uri.parse(path);
				this.mMediaPlayer.setDataSource(mContext, localUri);
			} else {
				mMediaPlayer = MediaPlayer.create(mContext, Uri.parse(path));
//				//this.mMediaPlayer.setAudioStreamType(3);
//				mMediaPlayer = mMediaPlayer.create(mContext, Uri.parse(path));
//				this.mMediaPlayer.prepareAsync();
//				this.mMediaPlayer.setDataSource(mPath);
			}
			mIsInitialized = true;
		} catch (Exception e) {
			e.printStackTrace();
			mIsInitialized = false;
		}
	}

	public void setVolume(float paramFloat) {
		try {
			this.mMediaPlayer.setVolume(paramFloat, paramFloat);
			return;
		} catch (IllegalStateException localIllegalStateException) {
			while (true)
				localIllegalStateException.printStackTrace();
		}
	}

	public void start() {
		this.mMediaPlayer.start();
//		isPlay = true;
	}

	public void stop() {
		this.mMediaPlayer.reset();
		this.mIsInitialized = false;
	}
}
