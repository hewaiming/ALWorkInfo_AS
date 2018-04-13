package io.github.ylbfdev.slideshowview.utils;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>类说明 虚拟缓存工具类</p>
 * @author YLBF
 * @version 修改时间 2015年7月17日下午1:50:07
 * @version 版本号 1.0.0.0
 */
public class MemoryCache {
	// 同步的~ 链表作为 cacheMap
	// 参数（初始容量为10，存储系数单精度1.5float>1不可扩容？，true最少使用的放最前）
	private Map<String, Bitmap> cacheMap = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
	private long size = 0;
	private long limit = 0;

	public MemoryCache() {
		// 限制map大小为虚拟机（JVM运行时环境）内存的1/10
		this.limit = Runtime.getRuntime().maxMemory() / 10;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public Bitmap get(String url) {
		if (!cacheMap.containsKey(url))
			return null;
		return cacheMap.get(url);
	}

	public void put(String url, Bitmap bitmap) {
		// 如果map存在这个bitmap，覆盖之前减掉旧图大小
		if (cacheMap.containsKey(url))
			size -= getSizeInBytes(cacheMap.get(url));
		cacheMap.put(url, bitmap);
		// 获取bitmap大小，累积加入size
		size += bitmap.getRowBytes() * bitmap.getHeight();
		// 检查缓存map大小
		checkSize();
	}

	// LRU最近最少使用， 如果size>limit，移除最少使用的图片，移除后如果size<limit退出循环
	private void checkSize() {
		if (size > limit) {
			// map转化成set集合,获取集合迭代器iterator
			Iterator<Entry<String, Bitmap>> iterator = cacheMap.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Bitmap> entry = iterator.next();
				iterator.remove();
				size -= getSizeInBytes(entry.getValue());
				if (size < limit)
					break;
			}
		}

	}

	private long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null)
			return 0;
		return bitmap.getRowBytes() * bitmap.getHeight();

	}

	public void clear() {
		cacheMap.clear();
	}

}
