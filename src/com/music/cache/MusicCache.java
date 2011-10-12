package com.music.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import com.music.api.IAudio;

/**
 * @title
 * @author Wondershare XiangYuan
 * @version 2011-7-15 下午05:33:22
 */
public final class MusicCache {

	private static MusicCache instance = null;

	/**
	 * 缓存网络上下载下来的音乐信息
	 */
	private WeakHashMap<String, IAudio> caches = null;

	protected MusicCache() {
		caches = new WeakHashMap<String, IAudio>(50);
	}

	/**
	 * @return the caches
	 */
	public WeakHashMap<String, IAudio> getCaches() {
		return caches;
	}

	/**
	 * @return
	 */
	public static synchronized MusicCache getInstance() {
		if (instance == null) {
			instance = new MusicCache();
		}
		return instance;
	}

	/**
	 * 得到本地缓存
	 * @return
	 */
	public List<IAudio> getLocalCache() {
		List<IAudio> datas = null;
		if (caches.size() > 0) {
			datas = new ArrayList<IAudio>(50);
			Iterator<String> it = caches.keySet().iterator();
			while (it.hasNext()) {
				datas.add(caches.get(it.next()));
			}
			return datas;
		} else
			return null;
	}
	
	public void clear() {
		this.caches.clear();
	}
	
	
	/**
	 * 缓存数据
	 * @param datas
	 */
	public void cacheRemoteMusic(List<IAudio> datas) {
		for(IAudio audio : datas) {
			String id = audio.getId();
			if (caches.containsKey(id)) {
				continue;
			}
			caches.put(id, audio);
		}
	}
}
