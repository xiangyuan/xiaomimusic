package com.music.player;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

/**
 * Player core engine allowing playback, in other words, a wrapper around
 * Android's <code>MediaPlayer</code>, supporting <code>Playlist</code> classes
 */
public class PlayerImpl implements Player {

	/**
	 * Time frame - used for counting number of fails within that time
	 */
	private static final long FAIL_TIME_FRAME = 1000;

	/**
	 * Acceptable number of fails within FAIL_TIME_FRAME
	 */
	private static final int ACCEPTABLE_FAIL_NUMBER = 2;

	/**
	 * Beginning of last FAIL_TIME_FRAME
	 */
	private long mLastFailTime;

	/**
	 * Number of times failed within FAIL_TIME_FRAME
	 */
	private long mTimesFailed;

	/**
	 * Simple MediaPlayer extensions, adds reference to the current track
	 * 
	 * @author Lukasz Wisniewski
	 */
	private class InternalMediaPlayer extends MediaPlayer {

		/**
		 * Still buffering
		 */
		public boolean preparing = false;

		/**
		 * Determines if we should play after preparation, e.g. we should not
		 * start playing if we are pre-buffering the next track and the old one
		 * is still playing
		 */
		public boolean playAfterPrepare = false;

	}

	/**
	 * InternalMediaPlayer instance (maybe add another one for cross-fading)
	 */
	private InternalMediaPlayer mCurrentMediaPlayer;

	// /**
	// * Runnable periodically querying Media Player about the current position
	// of
	// * the track and notifying the listener
	// */
	// private Runnable mUpdateTimeTask = new Runnable() {
	// public void run() {
	//
	// if (mPlayerEngineListener != null) {
	// // TODO use getCurrentPosition less frequently (usage of
	// // currentTimeMillis or uptimeMillis)
	// if (mCurrentMediaPlayer != null)
	// mPlayerEngineListener.onTrackProgress(mCurrentMediaPlayer
	// .getCurrentPosition() / 1000);
	// mHandler.postDelayed(this, 1000);
	// }
	// }
	// };

	/**
	 * Default constructor
	 */
	public PlayerImpl() {
		mLastFailTime = 0;
		mTimesFailed = 0;
	}

	@Override
	public void next() {
		play();
	}

	@Override
	public void pause() {
		if (mCurrentMediaPlayer != null) {
			// still preparing
			if (mCurrentMediaPlayer.preparing) {
				mCurrentMediaPlayer.playAfterPrepare = false;
				return;
			}

			// check if we play, then pause
			if (mCurrentMediaPlayer.isPlaying()) {
				mCurrentMediaPlayer.pause();
				return;
			}
		}
	}

	private String songPath = null;

	public void setDataPath(String path) {
		this.songPath = path;
	}

	private int createTime = 0;

	@Override
	public void play() {

		// check if media player is initialized
		if (mCurrentMediaPlayer == null) {
			mCurrentMediaPlayer = build(songPath);
		}

		// // check if current media player is set to our song
		// if (mCurrentMediaPlayer != null) {
		// cleanUp(); // this will do the cleanup job
		// mCurrentMediaPlayer = build(songPath);
		// }
		createTime++;
		if (createTime == 3)
			return;

		// check if there is any player instance, if not, abort further
		// execution
		if (mCurrentMediaPlayer == null) {
			play();// 还去创建
		} else {
			// check if current media player is not still buffering
			if (!mCurrentMediaPlayer.preparing) {

				// prevent double-press
				if (!mCurrentMediaPlayer.isPlaying()) {

					mCurrentMediaPlayer.start();
				}
			} else {
				// tell the mediaplayer to play the song as soon as it ends
				// preparing
				mCurrentMediaPlayer.playAfterPrepare = true;
			}
		}
	}

	@Override
	public void prev() {
		play();
	}

	@Override
	public void skipTo(int index) {
		play();
	}

	@Override
	public void stop() {
		cleanUp();
	}

	/**
	 * Stops & destroys media player
	 */
	private void cleanUp() {
		// nice clean-up job
		if (mCurrentMediaPlayer != null)
			try {
				mCurrentMediaPlayer.stop();
			} catch (IllegalStateException e) {
				// this may happen sometimes
			} finally {
				mCurrentMediaPlayer.release();
				mCurrentMediaPlayer = null;
			}
	}

	private InternalMediaPlayer build(String path) {
		final InternalMediaPlayer mediaPlayer = new InternalMediaPlayer();

		// some albums happen to contain empty stream url, notify of error,
		// abort playback
		if (path.length() == 0) {
			stop();
			return null;
		}

		try {
			mediaPlayer.setDataSource(path);
			// mediaPlayer.setScreenOnWhilePlaying(true);

			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					next();
				}
			});

			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					mediaPlayer.preparing = false;

					// we may start playing
					if (mediaPlayer.playAfterPrepare) {
						mediaPlayer.playAfterPrepare = false;
						play();
					}

				}

			});

			mediaPlayer
					.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

						@Override
						public void onBufferingUpdate(MediaPlayer mp,
								int percent) {
						}

					});

			mediaPlayer.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
						// we probably lack network
						stop();
						return true;
					}

					// not sure what error code -1 exactly stands for but it
					// causes player to start to jump songs
					// if there are more than 5 jumps without playback during 1
					// second then we abort
					// further playback
					if (what == -1) {
						long failTime = System.currentTimeMillis();
						if (failTime - mLastFailTime > FAIL_TIME_FRAME) {
							// outside time frame
							mTimesFailed = 1;
							mLastFailTime = failTime;
						} else {
							// inside time frame
							mTimesFailed++;
							if (mTimesFailed > ACCEPTABLE_FAIL_NUMBER) {
								stop();
								return true;
							}
						}
					}
					return false;
				}
			});

			mediaPlayer.preparing = true;
			mediaPlayer.prepareAsync();
			// // this is a new track, so notify the listener
			// if (mPlayerEngineListener != null) {
			// mPlayerEngineListener.onTrackChanged(mPlaylist
			// .getSelectedTrack());
			// }
			return mediaPlayer;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isPlaying() {

		// no media player instance
		if (mCurrentMediaPlayer == null)
			return false;

		// so there is one, let's see if it's not preparing
		if (mCurrentMediaPlayer.preparing)
			return false;

		// finally
		return mCurrentMediaPlayer.isPlaying();
	}

}
