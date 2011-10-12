package com.music.net.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;

import android.content.Context;

import com.music.api.IAudio;
import com.music.net.INetRequest;
import com.music.sys.SysCons;
import com.music.util.MessageHandlerUtils;

/**
 * @title
 * @author Wondershare XiangYuan
 * @version 2011-7-15 上午11:47:40
 */
public class TNetRequestImpl implements INetRequest {

	private Context mContext = null;
	
	public TNetRequestImpl(Context context) {
		this.mContext = context;
	}
	@Override
	public void dowloadFile(IAudio mp3) {

	}

	@Override
	public String getJSONArray(String url) throws JSONException {
		HttpURLConnection connection = null;
		String jsonSongs = "";
		try {
			URL songs = new URL(url);
			connection = (HttpURLConnection) songs.openConnection();
			connection.addRequestProperty("Connection", "Keep-alive");
			connection
					.addRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.14 Safari/534.24");
			connection.addRequestProperty("Accept-Charset",
					"GBK,utf-8;q=0.7,*;q=0.3");
			connection.setDoInput(true);
			StringBuilder sb = new StringBuilder(100);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			jsonSongs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			MessageHandlerUtils.getInstance(mContext).handleMsg(SysCons.IO_EXCEPTION, "流读取异常");
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return jsonSongs;
	}
}
