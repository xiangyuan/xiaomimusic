package com.music.player;

@Deprecated
public interface PlayerEngineListener {
	
	/**
	 * Callback invoked before starting a new track, return false to prevent
	 * playback from happening
	 * 
	 * @param playlistEntry
	 */
	public boolean onTrackStart(); 
	
//	/**
//	 * Callback invoked when a new track is played
//	 * 
//	 * @param playlistEntry
//	 */
//	public void onTrackChanged(PlaylistEntry playlistEntry); 
	
	/**
	 * Callback invoked periodically, contains info on track 
	 * playing progress
	 * 
	 * @param seconds int value
	 */
	public void onTrackProgress(int seconds);
	
	/**
	 * Callback invoked when buffering a track
	 * 
	 * @param percent
	 */
	public void onTrackBuffering(int percent);
	
	/**
	 * Callback invoked when track is stoped
	 */
	public void onTrackStop();
	
	/**
	 * Callback invoked when track is paused
	 */
	public void onTrackPause();
	
	/**
	 * Callback invoked when there was an error with
	 * streaming
	 */
	public void onTrackStreamError();

}
