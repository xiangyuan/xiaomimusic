package com.wontube.downlader.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.webkit.URLUtil;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.wontube.downlader.R;

/**
 * @title
 * @author LiYa
 * @verson 1.0 Feb 6, 2012 3:01:34 PM
 */
public class DownloadService {

	private static final int NOTIFY_ID = 0;

	private boolean cancelled;
	private int progress;

	private Context mContext = null;

	private NotificationManager mNotificationManager;
	private Notification mNotification;

	public DownloadService(Context context) {
		mContext = context;
		mNotificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
	}

	/**
	 * @param rate
	 */
	public void downloadProgress(int rate) {
		// 更新进度
		RemoteViews contentView = mNotification.contentView;
		contentView.setTextViewText(R.id.rate, rate + "%");
		contentView.setProgressBar(R.id.progress, 100, rate, false);
		// 最后别忘了通知一下,否则不会更新
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}

	public void dowloadOver(File myFile) {
		// 下载完毕后变换通知形式
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotification.contentView = null;
		Intent install = new Intent();
		install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		install.setAction(android.content.Intent.ACTION_VIEW);

		// 调用getMIMEType()来取得MimeType
		String type = "application/vnd.android.package-archive";
		// 设定intent的file与MimeType

		install.setDataAndType(Uri.fromFile(myFile), type);
		// Intent intent = new Intent(mContext, FileMgrActivity.class);
		// // 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
				install, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.setLatestEventInfo(mContext, "Download Over", "The file download success",
				contentIntent);
		// 最后别忘了通知一下,否则不会更新
		mNotificationManager.notify(NOTIFY_ID, mNotification);
		mContext.startActivity(install);
		// if (myFile.exists()) {
		// myFile.delete();
		// }
	}

	public void cancel() {
		// 取消通知
		mNotificationManager.cancel(NOTIFY_ID);
		mNotification.contentView.removeAllViews(R.id.progress_layout);
	}

	/**
	 * 创建通知
	 */
	private void setUpNotification(String path) {
		int start = path.lastIndexOf("/");
		String fileName = path.substring(start + 1);
		int icon = R.drawable.down;
		CharSequence tickerText = mContext.getString(R.string.start_download);
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);

		// 放置在"正在运行"栏目中
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		RemoteViews contentView = new RemoteViews(mContext.getPackageName(),
				R.layout.download_notification);
		contentView.setTextViewText(R.id.fileName, fileName);
		// 指定个性化视图
		mNotification.contentView = contentView;

		// Intent intent = new Intent(this, YoutubeActivity.class);
		// PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
		// intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// // 指定内容意图
		// mNotification.contentIntent = contentIntent;
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}

	/**
	 * 开始下载升级包
	 * 
	 * @param path
	 * @throws Exception
	 */
	public void downloadFile(String path) throws InterruptedException {
		int start = path.lastIndexOf("/");
		String fileName = path.substring(start + 1);
		int spitIndex = fileName.lastIndexOf(".");
		String prefix = fileName.substring(0, spitIndex);
		String suffix = fileName.substring(spitIndex + 1);

		if (URLUtil.isNetworkUrl(path)) {
			URL mURL;
			try {
				mURL = new URL(path);
				URLConnection conn = mURL.openConnection();
				conn.connect();
				int contentLen = conn.getContentLength();
				InputStream in = conn.getInputStream();
				FileOutputStream fos = null;
				if (in == null) {

				} else {
					File myTempFile = File.createTempFile(prefix, suffix);
					myTempFile.getAbsolutePath();
					fos = new FileOutputStream(myTempFile);
					byte buf[] = new byte[1024 * 8];
					int len = 0;
					int downloadedLen = 0;
					Handler handler = NotificationUtilsHandler.getHandler();
					int times = 0;
					int lastPro = 0;
					while ((len = in.read(buf)) > 0) {
						downloadedLen += len;
						fos.write(buf, 0, len);
						int pro = (int) ((downloadedLen * 1.0 / contentLen * 1.0) * 100);
						// downloadProgress(pro);

						if ((times == 90) || (downloadedLen == contentLen)) {
							if (lastPro < pro) {
								lastPro = pro;
								handler.sendMessage(handler
										.obtainMessage(
												YoutubeActivity.UPDATE_PACKAGE_START_DOWNLOAD,
												pro));
							}
							times = 0;
							continue;
						}
						times++;
					}
					fos.flush();
					fos.close();
					in.close();
					// dowloadOver(myTempFile);
					handler.sendMessage(handler.obtainMessage(
							YoutubeActivity.UPDATE_DOWNLOAD_OVER, myTempFile));
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(mContext, R.string.network_exception,
						Toast.LENGTH_SHORT).show();
			}
		}
		// 　　/*打开文件进行安装*/
		// 　　openFile(myTempFile);
	}

	/**
	 * 开始下载
	 */
	public void start(final String file) {
		// 将进度归零
		progress = 0;
		// 创建通知
		setUpNotification(file);
		new Thread() {
			public void run() {
				// 下载
				try {
					downloadFile(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * DownloadBinder中定义了一些实用的方法
	 * 
	 * @author user
	 * 
	 */
	public class DownloadBinder extends Binder {

		/**
		 * 开始下载
		 */
		public void start(final String file) {
			// 将进度归零
			progress = 0;
			// 创建通知
			setUpNotification(file);
			new Thread() {
				public void run() {
					// 下载
					try {
						downloadFile(file);
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
		}

		/**
		 * 获取进度
		 * 
		 * @return
		 */
		public int getProgress() {
			return progress;
		}

		/**
		 * 取消下载
		 */
		public void cancel() {
			cancelled = true;
			mNotificationManager.cancel(NOTIFY_ID);
		}

		/**
		 * 是否已被取消
		 * 
		 * @return
		 */
		public boolean isCancelled() {
			return cancelled;
		}

	}
}
