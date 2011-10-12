package com.music.net.impl;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.music.net.LoadingImageBack;
import com.music.sys.SysCons;
import com.music.util.MessageHandlerUtils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * @title
 * @author XiangYuan mailto:liyajie1209@gmail.com
 * @version 2011-7-21 上午11:29:54
 */
public class AsynImageDowloader {

	private Context mContext = null;

	/** 缓存网络图片 **/
	private HashMap<String, SoftReference<Drawable>> imageCache;

	private static AsynImageDowloader dowloader = null;
	
	protected AsynImageDowloader(Context context) {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
		this.mContext = context;
	}

	/**
	 * @param context
	 * @return
	 */
	public static synchronized AsynImageDowloader getInstance(Context context) {
		if (dowloader == null) {
			dowloader = new AsynImageDowloader(context);
		}
		return dowloader;
	}
	/**
	 * @param url
	 *            图片地址
	 * @param callBack
	 *            完成后的更新调用
	 * @param imageView
	 * @return
	 */
	public Drawable loadImageFromURL(final String url,
			final LoadingImageBack callBack, final ImageView imageView) {
		Drawable drawable = null;
		if (imageCache.containsKey(url)) {
			drawable = imageCache.get(url).get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				callBack.update((Drawable) msg.obj, imageView);
			}
		};
		new Thread() {
			public void run() {
				final Drawable d = loadImage(url);
				if (d != null) {
					final SoftReference<Drawable> s = new SoftReference<Drawable>(d);
					imageCache.put(url, s);
					Message msg = mHandler.obtainMessage();
					msg.obj = d;
					mHandler.sendMessage(msg);
				}
			}
		}.start();
		return null;
	}

	/**
	 * @param url
	 * @return
	 */
	public Drawable loadImage(String spec) {
		HttpURLConnection conn = null;
		Drawable d = null;
		try {
			URL url = new URL(spec);
			conn = (HttpURLConnection) url.openConnection();
			InputStream in = conn.getInputStream();
			d = BitmapDrawable.createFromStream(in, "logo");
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			MessageHandlerUtils.getInstance(mContext).handleMsg(
					SysCons.LOADING_ALBUM_IMAGE_EXCEPTION, e.getMessage());
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return d;
	}
	
	/**
	 * @return the imageCache
	 */
	public HashMap<String, SoftReference<Drawable>> getImageCache() {
		return imageCache;
	}
}
