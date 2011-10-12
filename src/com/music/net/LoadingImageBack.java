package com.music.net;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * @title
 * @author XiangYuan mailto:liyajie1209@gmail.com
 * @version 2011-7-21 上午11:22:06
 */
public interface LoadingImageBack {

	/**
	 * 加载完成图片后更新视图
	 * @param image
	 * @param logo
	 */
	public void update(Drawable image,ImageView logo);
}
