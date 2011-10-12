package com.music.core;

import com.music.MainActivity;
import com.music.MusicPlayActivity;
import com.music.api.impl.TMP3;
import com.music.cache.MusicCache;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @title <p>
 *        点击后进行音乐播放
 *        </p>
 * @author XiangYuan mailto:liyajie1209@gmail.com
 * @version 2011-7-21 下午02:15:53
 */
public class MusicItemOnclickListener implements OnItemClickListener {

	public MainActivity lv = null;

	public MusicItemOnclickListener(MainActivity lv) {
		this.lv = lv;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TMP3 mp3 = (TMP3) MusicCache.getInstance().getCaches().get(id + "");
		String songUrl = mp3.getSongURL();
		Log.d("liyajie", songUrl);
		String data = mp3.getId() + "," + mp3.getName() + "," + mp3.getArtist()
				+ "," + mp3.getLyric() + "," + mp3.getSongURL() + ","
				+ mp3.getMP3Size() + "," + mp3.getAlbum().getAlubmCover();
		Intent intent = new Intent();
		intent.setClass(lv.getApplicationContext(), MusicPlayActivity.class);
		intent.putExtra("song", data);
		lv.startActivity(intent);
	}
}
