package com.music;

import java.util.List;

import com.music.api.IAudio;
import com.music.api.IAudioManager;
import com.music.api.impl.MP3Manager;
import com.music.cache.MusicCache;
import com.music.core.MusicItemOnclickListener;
import com.music.net.INetRequest;
import com.music.net.impl.TNetRequestImpl;
import com.music.store.MusicAdapter;
import com.music.sys.SysCons;
import com.music.util.MessageHandlerUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

	/** 进度条 **/
	public ProgressBar progress = null;
	/**
	 * 列表对象
	 */
	private ListView lv = null;

	private View mainView = null;
	
	private Button btn = null;
	
	private MusicAdapter adapter = null;
	
	private OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new MP3Task().execute(getResources().getString(
					R.string.random_musics));
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		progress = (ProgressBar) findViewById(R.id.progress);
		// 完成后更新ui
		mainView = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.listview, null);
		lv = (ListView) mainView.findViewById(R.id.mylistview);
		lv.setOnItemClickListener(new MusicItemOnclickListener(this));
		btn = (Button) LayoutInflater.from(getApplicationContext()).inflate(R.layout.btn,
				null);
		adapter = new MusicAdapter(getApplicationContext());
		// 列表页角并注册事件
		//btn = (Button) findViewById(R.id.moresong);
		lv.addFooterView(btn);
		lv.setAdapter(adapter);
		btn.setOnClickListener(btnClickListener);
		init();
	}

	/**
	 * @return
	 */
	public ListView getLv() {
		return (lv);
	}

	public void init() {
		if (MusicCache.getInstance().getLocalCache() == null) {
			new MP3Task().execute(getResources().getString(
					R.string.random_musics));
		} else {
			setViewAdapter(MusicCache.getInstance().getLocalCache());
		}
	}

	/**
	 * 
	 */
	public void setViewAdapter(List<IAudio> datas) {
		adapter.addMusic(datas);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MusicCache.getInstance().clear();
		//结束进程
		Process.killProcess(Process.myPid());
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_profile, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int mId = item.getItemId();
		switch (mId) {
		case R.id.fresh_menu_item:
			new MP3Task().execute(getResources().getString(
					R.string.random_musics));
			break;
		case R.id.about_menu_item:
			break;
		case R.id.exit_menu_item:
			this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/** 发送请求任务命令 **/
	protected class MP3Task extends AsyncTask<String, Integer, List<IAudio>> {

		INetRequest request = null;

		public MP3Task() {
			request = new TNetRequestImpl(getApplicationContext());
		}

		@Override
		protected List<IAudio> doInBackground(String... params) {
			List<IAudio> datas = null;
			int progress = 5;
			try {
				// 1.请求网络
				progress += 15;
				publishProgress(progress);
				String result = request.getJSONArray(params[0]);
				progress += 15;
				publishProgress(progress);
				// 2.处理json数据
				IAudioManager manager = new MP3Manager(getApplicationContext());
				if (result != null) {
					progress += 20;
					publishProgress(progress);
					datas = manager.getMP3(result);
					progress += 20;
					publishProgress(progress);
					MusicCache.getInstance().cacheRemoteMusic(datas);
					progress += 25;
					publishProgress(progress);
				}
			} catch (Exception e) {
				e.printStackTrace();
				MessageHandlerUtils.getInstance(getApplicationContext())
						.handleMsg(SysCons.NET_EXCEPTION, "网络故障");
			}
			return datas;
		}

		@Override
		protected void onPostExecute(List<IAudio> result) {
			adapter.addMusic(result);
			// 最后将界面更新
			setContentView(mainView);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progress.setProgress(values[0]);
		}
	}
}