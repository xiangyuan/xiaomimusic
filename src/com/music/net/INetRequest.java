package com.music.net;

import org.json.JSONException;

import com.music.api.IAudio;

public interface INetRequest {

	/**
	 * 将mp3文件下载到本地保存
	 * @param mp3
	 */
	public void dowloadFile(IAudio mp3);
	
	
	/**
	 * 得到请求的信息
	 * @param url
	 * @return
	 * @throws JSONException
	 */
	public String getJSONArray(String url) throws Exception;
	
}
