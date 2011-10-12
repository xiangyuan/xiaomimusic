package com.music;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.music.core.NetPlayer;
import com.music.net.impl.AsynImageDowloader;
import com.music.player.Player;
import com.music.player.PlayerImpl;
import com.music.util.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @title
 * @author XiangYuan mailto:liyajie1209@gmail.com
 * @version 2011-7-21 下午03:20:59
 */
@SuppressWarnings("unused")
public class MusicPlayActivity extends Activity implements OnClickListener {

	/** 专辑封面 **/
	private ImageView albumCover = null;
	/**
	 * 播放操作按钮
	 */
	private Button pre, play, next;
	/** 播放进度 **/
	private ProgressBar progress = null;

	private TextView txtName, txtLyric;

	private String songURL, lyricURL;

	boolean flag = true;

	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			int cuPos = msg.what;
			progress.setProgress(cuPos);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		albumCover = (ImageView) findViewById(R.id.album_cover);
		play = (Button) findViewById(R.id.play);
		play.setOnClickListener(this);
		progress = (ProgressBar) findViewById(R.id.progressBar);
		txtName = (TextView) findViewById(R.id.songName);
		init();
	}

	public void init() {
		Intent intent = getIntent();
		/***
		 * mp3.getId() + "," + mp3.getName() + "," + mp3.getArtist() + "," +
		 * mp3.getLyric() + "," + mp3.getSongURL() + "," + mp3.getMP3Size() +
		 * "," + mp3.getAlbum().getAlubmCover(); Intent intent = new
		 * Intent(PLAYER);
		 */
		if (intent != null) {
			String[] datas = intent.getStringExtra("song").split(",");
			try {
				songURL = URLDecoder
						.decode(
								"http://zhangmenshiting.baidu.com/data/music/4724833/%E9%9D%92%E8%A1%A3.mp3?xcode=eeba4d0ba6f2830523d614f88ac17286",
								"utf-8");
				Log.d("liyajie", songURL);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			songURL = datas[4];
			lyricURL = datas[3];
			txtName.setText(datas[1] + "   " + datas[2]);
			HashMap<String, SoftReference<Drawable>> cache = AsynImageDowloader
					.getInstance(getApplicationContext()).getImageCache();
			if (cache.containsKey(datas[6])) {
				albumCover.setImageDrawable(cache.get(datas[6]).get());
			} else {
				albumCover.setImageResource(R.drawable.album);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.finish();
	}

	@Override
	public void onClick(View v) {
		if (flag) {
			play.setBackgroundResource(R.drawable.player_pause_light);
			flag = false;
			// Timer timer = new Timer();
			// timer.schedule(task, 500, 500);
		} else {
			play.setBackgroundResource(R.drawable.player_play_light);
			flag = true;
		}
		new Thread() {
			@Override
			public void run() {
				//playMusic();
				playOne();
			}
		}.start();
	}

	private TimerTask task = new TimerTask() {

		@Override
		public void run() {
		}
	};

	public void playOne() {
		Player player = new PlayerImpl();
		player.setDataPath(StringUtils.replaceURL(songURL));
		if (!flag) {
			player.pause();
		}
		if (player.isPlaying()) {
			player.pause();
		} else {
			player.play();
		}
	}
	public void playMusic() {
		HttpURLConnection conn = null;
		try {
			String dest = StringUtils.replaceURL(songURL);
			URL url = new URL(dest);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			
			final File music = File.createTempFile("temp", ".dat");
			FileOutputStream fos = new FileOutputStream(music);
			InputStream in = conn.getInputStream();
			byte[] buff = new byte[1024 * 5];
			int len = 0;
			while ((len = in.read(buff))!= -1) {
				fos.write(buff, 0, len);
			}
			fos.flush();
			in.close();
			fos.close();
			
			MediaPlayer player = new MediaPlayer();
			player.setDataSource(music.getAbsolutePath());
			player.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					music.deleteOnExit();
				}
			});
			player.prepare();
			player.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
}
