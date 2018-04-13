package com.hewaiming.ALWorkInfo.config;

import java.io.File;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;


import android.content.Context;

public class ImageConfig {
	public ImageConfig(Context context) {
		super();
		this.context = context;
	}

	private static Context context;

	public static void initImageLoader() {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "ALWorkInfo/Cache");// 获取到缓存的目录地址
		// 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				// 线程池内加载的数量
				.threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2).memoryCache(new WeakMemoryCache())
				.denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO).discCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
				// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);// 全局初始化此配置
	}

	public static void initImageLoader(Context context) {
		new ImageLoadOptions();
		DisplayImageOptions options = ImageLoadOptions.getOptions();

		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "ALWorkInfo/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.memoryCacheExtraOptions(480, 800) // maxwidth, max
													// height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))

				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024).discCacheSize(50 * 1024 * 1024)

				.tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(100) // 缓存的文件数量
				.discCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
				// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.defaultDisplayImageOptions(options)
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout

				// s)超时时间
				.writeDebugLogs() // Remove for releaseapp
				.build();// 开始构建

		ImageLoader.getInstance().init(config);
	}
}
