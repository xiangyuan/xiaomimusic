package com.music.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.music.api.IAudio;
import com.music.api.IAudioManager;
import com.music.api.IAudio.IAlbum;
import com.music.sys.SysCons;
import com.music.util.MessageHandlerUtils;

/**
 * @title
 * @author Wondershare XiangYuan
 * @version 2011-7-14 下午05:58:52
 */
public class MP3Manager implements IAudioManager {

	private Context mContext = null;

	public MP3Manager(Context context) {
		this.mContext = context;
	}

	@Override
	public IAudio getAudio(String audioId) {
		return null;
	}

	@Override
	public List<IAudio> getMP3(String json) {
		List<IAudio> datas = new ArrayList<IAudio>(50);
		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("songs");
			int len = array.length();
			JSONObject songClazz = null;
			TMP3 audio = null;
			IAlbum album = null;
			for (int index = 0; index < len; index++) {
				songClazz = array.optJSONObject(index);
				audio = new TMP3();
				album = new TAblum();
				audio.setId(songClazz.optString("song_id", ""));
				audio.setName(songClazz.optString("name", ""));
				audio.setArtist(songClazz.optString("artist_name", ""));
				audio.setMP3Size(songClazz.optString("low_size", "0"));
				audio.setSongURL(songClazz.optString("location", null));
				audio.setLyric(songClazz.optString("lyric", null));
				album.setId(songClazz.optString("album_id", ""));
				album.setAlbumCover(songClazz.optString("album_logo", ""));
				audio.setAlbum(album);
				datas.add(audio);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			MessageHandlerUtils.getInstance(mContext).handleMsg(
					SysCons.GET_AUDIO_JSON_HANDLER_EXCEPTION, "处理json数据异常");
		}
		return datas;
	}
}
