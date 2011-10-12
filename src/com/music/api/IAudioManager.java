/*******************************************************************************************
 * Copyright (c) 2010 Wondershare Co., Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Wondershare Co., Ltd. 
 * You shall not disclose such Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with Wondershare.
 ******************************************************************************************/

package com.music.api;

import java.util.List;

public interface IAudioManager {

	/**
	 * 得到所有的歌曲
	 * @return
	 */
	public List<IAudio> getMP3(String json);
	
	/**
	 * 得到某一歌曲
	 * @param audioId
	 * @return
	 */
	public IAudio getAudio(String audioId);
}
