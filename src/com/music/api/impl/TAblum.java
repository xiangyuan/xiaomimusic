package com.music.api.impl;

import com.music.api.IAudio.IAlbum;

public class TAblum implements IAlbum {

	private String id;
	private String coverURL;
	//小米音乐发送的随机音乐列表中，是没有专辑名的
	private String name;
	
	@Override
	public String getAlubmCover() {
		return (coverURL);
	}

	@Override
	public void setAlbumCover(String coverUrl) {
		this.coverURL = coverUrl;
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

}
