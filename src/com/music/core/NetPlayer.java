package com.music.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.music.util.FileUtils;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * @title
 * @author XiangYuan mailto:liyajie1209@gmail.com
 * @version 2011-7-22 下午05:40:37
 */
public class NetPlayer implements IPlayNetwork {

	/**
	 * 开始八十K的缓存
	 */
	private static final int BUFFER_CACHE = 1024 * 80;
	/**
	 * 临时文件的名称与后缀
	 */
	private static final String TEMPFILE_NAME = "dowload_music";
	/**
	 */
	private static final String TEMPFILE_POSIX = ".dat";

	private MediaPlayer mPlayer = null;
	/**
	 * 音乐总长
	 */
	private int musicLen = 0;

	private File tempMusicFile = null;

	/**
	 * 下载是否停止
	 */
	private boolean isStop = false;

	// private boolean isDowloadFinish = false;

	private boolean isPlay = false;

	public NetPlayer() {
	}

	@Override
	public int getCurPostion() {
		if (mPlayer != null) {
			return (mPlayer.getCurrentPosition());
		}
		return 0;
	}

	@Override
	public int getDuration() {
		if (mPlayer != null) {
			return mPlayer.getDuration();
		}
		return 0;
	}

	@Override
	public boolean isPause() {
		return false;
	}

	@Override
	public boolean isPlay() {
		if (mPlayer != null) {
			return (mPlayer.isPlaying());
		}
		return false;
	}

	@Override
	public void release() {
		if (mPlayer != null) {
			mPlayer.release();
		}
	}

	@Override
	public void start() {
		try {
			if (mPlayer == null || !isPlay) {
				if (totalKB >= BUFFER_CACHE) {// 大于这个值时才播放
					mPlayer = new MediaPlayer();

					File tPlayFile = File.createTempFile("tmp_music",
							TEMPFILE_POSIX);
					FileUtils.fileCopy(tempMusicFile, tPlayFile);

					tempMusicFile.deleteOnExit();
					mPlayer.setDataSource(tPlayFile.getAbsolutePath());
					mPlayer.prepare();
					mPlayer.start();
					isPlay = true;
				}
			} else {// 正在播放
				boolean isPlay = mPlayer.isPlaying();
				if (isPlay) {
					mPlayer.pause();
				}
				int curPos = mPlayer.getCurrentPosition();
				File tPlayFile = File.createTempFile("tmp_music",
						TEMPFILE_POSIX);
				FileUtils.fileCopy(tempMusicFile, tPlayFile);

				tempMusicFile.deleteOnExit();
				mPlayer.setDataSource(tPlayFile.getAbsolutePath());
				mPlayer.prepare();
				mPlayer.seekTo(curPos);
				mPlayer.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void play(final String path) {
		new PlayerThread(path).start();
	}

	/**
	 * 总字节数
	 */
	public int totalKB = 0;

	/**
	 * 下载音乐文件，分段并播放
	 * 
	 * @param url
	 * @param start
	 * @param end
	 */
	public void dowloadSplit(final String url, int start) {
		URLConnection conn = null;
		InputStream in = null;
		FileOutputStream fos = null;
		try {
			URL mPath = new URL(url);
			conn = mPath.openConnection();
			conn.setDoInput(true);
			conn.connect();

			in = conn.getInputStream();
			// 得到文件总长
			musicLen = conn.getContentLength();
			if (in == null) {
				return;
			}
			// 创建临时文件,因为是非多线程下载，此处在创建临时文件前，先进行一次清除
			if (tempMusicFile != null) {
				tempMusicFile.deleteOnExit();// 如果存在将其删除
			}

			tempMusicFile = File.createTempFile(TEMPFILE_NAME, TEMPFILE_POSIX);
			fos = new FileOutputStream(tempMusicFile);
			byte[] buffer = new byte[1024 * 4];
			int len = 0;

			while ((len = in.read(buffer)) != -1 && !isStop) {
				totalKB += len;
				fos.write(buffer, 0, len);
				// 开始播放
				// dowloadSplit(url, start, end)
				start();
			}
			// 完全下载完成
			if (totalKB == musicLen) {

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 播放与下载
	 */
	public class PlayerThread extends Thread {

		private String srcPath;

		public PlayerThread(String path) {
			this.srcPath = path;
		}

		@Override
		public void run() {
			// 非网络歌曲
			if (srcPath.startsWith("content://")
					|| srcPath.startsWith("CONTENT://")) {
				try {
					mPlayer.setDataSource(srcPath);
					mPlayer
							.setAudioStreamType(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
					mPlayer.prepare();
					mPlayer.start();// 开始播放
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				// 分段下载音乐文件数据
				dowloadSplit(srcPath, 0);
			}
		}
	}
}
