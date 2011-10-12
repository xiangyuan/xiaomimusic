/*******************************************************************************************
 * Copyright (c) 2010 Wondershare Co., Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Wondershare Co., Ltd. 
 * You shall not disclose such Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with Wondershare.
 ******************************************************************************************/

package com.music.api;

/**
 * @title
 * @author Wondershare XiangYuan
 * @version 2011-7-14 下午05:24:57
 */
public interface IAudio {

	public String getId();
	public void setId(String id);
	
	
	
	public String getName();
	public void setName(String name);
	
	public interface IAlbum extends IAudio {
		
		public String getAlubmCover();
		public void setAlbumCover(String coverUrl);
	}
	
	public interface IMP3 extends IAudio {
		
		public IAlbum getAlbum();
		public void setAlbum(IAlbum album);
		
		public String getSongURL();
		public void setSongURL(String url);
		
		public String getArtist();
		public void setArtist(String artist);
		
		public String getMP3Size();
		public void setMP3Size(String size);
		
		public String getLyric();
		public void setLyric(String lyric);
		
	}
}
