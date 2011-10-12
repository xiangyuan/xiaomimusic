package com.music.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

/**
 * @title
 * @author Wondershare XiangYuan
 * @version 2011-7-15 下午01:29:09
 */
public class MessageHandlerUtils {

	private Context mContext = null;

	/** 处得响应消息 **/
	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			String message = (String) msg.obj;
			showDialog(message);
		}
	};

	private static MessageHandlerUtils instance = null;

	protected MessageHandlerUtils(Context context) {
		this.mContext = context;
	}

	/**
	 * 得到实例
	 * @param context
	 * @return
	 */
	public static synchronized MessageHandlerUtils getInstance(Context context) {
		if (instance == null) {
			instance = new MessageHandlerUtils(context);
		}
		return instance;
	}

	/**
	 * 处理异常信息
	 * 
	 * @param what
	 *            类别
	 * @param ex
	 */
	public void handleMsg(int what, String message) {
		Message msg = mHandler.obtainMessage();
		msg.what = what;
		msg.obj = message;
		mHandler.sendMessage(msg);
	}

	/**
	 * @param msg
	 */
	public void showDialog(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}
}
