package com.music.store;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.R;
import com.music.api.IAudio;
import com.music.api.impl.TMP3;
import com.music.net.LoadingImageBack;
import com.music.net.impl.AsynImageDowloader;
import com.music.util.StringUtils;

public class MusicAdapter extends BaseAdapter {

	/** 所存在的数据集 **/
	private List<IAudio> datas = null;

	private Context mContext = null;

	public MusicAdapter(Context context) {
		this.mContext = context;
		datas = new ArrayList<IAudio>(80);
	}

	/**
	 * 添加一批音乐
	 * 
	 * @param newMusics
	 */
	public void addMusic(List<IAudio> newMusics) {
		this.datas.addAll(newMusics);
		notifyDataSetChanged();
	}

	/**
	 * 添加一首音乐
	 * 
	 * @param music
	 */
	public void addMusic(IAudio music) {
		datas.add(music);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		if (position < datas.size()) {
			return datas.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (position < datas.size()) {
			return (Long.valueOf(datas.get(position).getId()));
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder vHolder = null;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.list_item, null);
			vHolder = new ViewHolder();
			vHolder.alumLogo = (ImageView) view.findViewById(R.id.album_cover);
			vHolder.txtMsg = (TextView) view.findViewById(R.id.song_msg);
			vHolder.txtSize = (TextView) view.findViewById(R.id.size);
			view.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) view.getTag();
		}
		TMP3 mp3 = (TMP3) datas.get(position);
		Drawable d = AsynImageDowloader.getInstance(mContext).loadImageFromURL(mp3.getAlbum()
				.getAlubmCover(), new LoadingImageBack() {
			@Override
			public void update(Drawable image, ImageView logo) {
				logo.setImageDrawable(image);
			}
		}, vHolder.alumLogo);
		if (d == null) {
			vHolder.alumLogo.setImageResource(R.drawable.album);
		} else {
			vHolder.alumLogo.setImageDrawable(d);
		}
		vHolder.txtMsg.setText("歌曲: " + mp3.getName() + " 艺术家: "
				+ mp3.getArtist());
		vHolder.txtSize.setText(StringUtils.getMusicSize(mp3.getMP3Size()));
		return view;
	}

	/**
	 * @title 缓存视图的
	 * @author XiangYuan mailto:liyajie1209@gmail.com
	 * @version 2011-7-21 上午10:26:27
	 */
	class ViewHolder {
		ImageView alumLogo;
		TextView txtMsg;
		TextView txtSize;

	}
}
