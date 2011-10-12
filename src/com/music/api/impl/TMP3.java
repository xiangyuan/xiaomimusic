package com.music.api.impl;

import com.music.api.IAudio.IMP3;

public class TMP3 implements IMP3 {

	private IAlbum album;
	
	private String id;
	
	private String name;
	
	private String pathURL;
	
	private String size;
	
	private String artist;
	
	private String lyric;
	
	@Override
	public IAlbum getAlbum() {
		return (album);
	}

	@Override
	public String getArtist() {
		return (artist);
	}

	@Override
	public String getMP3Size() {
		return (size);
	}

	@Override
	public String getSongURL() {
		return (pathURL);
	}

	@Override
	public void setAlbum(IAlbum album) {
		this.album = album;
	}

	@Override
	public void setArtist(String artist) {
		this.artist = artist;
	}

	@Override
	public void setMP3Size(String size) {
		this.size = size;
	}

	@Override
	public void setSongURL(String url) {
		this.pathURL = url;
	}

	@Override
	public String getId() {
		return (id);
	}

	@Override
	public String getName() {
		return (name);
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getLyric() {
		return (lyric);
	}

	@Override
	public void setLyric(String lyric) {
		this.lyric = lyric;
	}

}
