package io.github.ylbfdev.slideshowview.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.ylbfdev.slideshowview.R;


/**
 * @author YLBF
 * 
 * <p>类说明 ImageLoader 多渠道加载图片 如果有必要后期会替换成UIL</p>
 *
 * @version 修改时间 2015年7月17日下午1:48:04
 * @version 版本号 1.0.0.0
 */
public class ImageLoader {
	private MemoryCache memoryCache = new MemoryCache();
	private FileCache fileCache;
	private ExecutorService executorService;
	private Context context;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());

	// 需要下载的图片
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	public ImageLoader(Context context) {
		this.context = context;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(6);
	}

	public void displayImage(String url, ImageView imageView, boolean busy) {
		imageViews.put(imageView, url);

		// 内存缓存获取图片
		Bitmap bitmap = memoryCache.get(url);

		if (bitmap != null) {
			// 缓存获取到图片，set
			imageView.setImageBitmap(bitmap);
		} else if (!busy) {
			// 非滑动中，缓存没有获取到，开启线程到线程池
			// 从file或url加载图片，set
			LoadRunnable r = new LoadRunnable(new PhotoToLoad(url, imageView));
			executorService.submit(r);
		}
	}

	// 加载图片并显示
	class LoadRunnable implements Runnable {
		private PhotoToLoad p;

		public LoadRunnable(PhotoToLoad p) {
			this.p = p;
		}

		@Override
		public void run() {
			loadimage(p);
		}

	}

	/**
	 * 加载图片 出现异常继续加载
	 * 
	 * @param p
	 */
	private void loadimage(PhotoToLoad p) {
		// 开始下载图片之前，判断view是否重用，重用不加载图片
		if (imageViewReused(p))
			return;
		Bitmap b = getBitmap(p.url);
		if (b != null) {
			memoryCache.put(p.url, b);
			// 显示图片之前，判断view是否重用，重用不显示图片
			if (imageViewReused(p))
				return;
			SetBitmap sb = new SetBitmap(b, p);
			// 更新的操作放在UI线程中
			Activity a = (Activity) p.imageView.getContext();
			a.runOnUiThread(sb);
		} else {
			System.out.println("加载图片出现异常" + p.url);
			loadimage(p);
		}
	}

	class SetBitmap implements Runnable {
		Bitmap b;
		PhotoToLoad p;

		public SetBitmap(Bitmap b, PhotoToLoad p) {
			this.b = b;
			this.p = p;
		}

		@Override
		public void run() {
			if (imageViewReused(p))
				return;
			if (b != null)
				p.imageView.setImageBitmap(b);
		}

	}

	// 判断imageView是否重用
	private boolean imageViewReused(PhotoToLoad p) {
		String u = imageViews.get(p.imageView);
		if (u == null || !u.equals(p.url))
			return true;
		return false;
	}

	// 文件缓存或网络获取图片
	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);
		Bitmap b = null;
		if (f != null && f.exists()) {
			b = decodeFile(f);
		}
		if (b != null) {
			System.out.println("图片来源于本地" + url);
			return b;
		} else {
			try {
				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl
						.openConnection();
				// ？？connection参数
				conn.setConnectTimeout(300000);
				conn.setReadTimeout(300000);
				conn.setInstanceFollowRedirects(true);

				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				copyStream(is, os);
				os.close();
				b = decodeFile(f);
				System.out.println("图片来源于网络" + url);
				return b;
			} catch (FileNotFoundException e) {
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				b = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.loading_slideshowview);
				return b;
			}
		}
	}

	// decode File 没做压缩处理
	private Bitmap decodeFile(File f) {
		try {
			return (BitmapFactory.decodeStream(new FileInputStream(f)));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	// ??private void URL获取输入流，写入输出流
	public static void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			for (;;) {
				byte[] bytes = new byte[buffer_size];
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

}
