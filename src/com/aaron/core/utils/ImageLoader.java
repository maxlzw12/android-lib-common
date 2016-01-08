package com.aaron.core.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.LruCache;

/**
 * 图片加载器(未完成品，加载的图片未进行压缩处理)
 * 
 * @author lzhiwei
 * 
 *         2015-9-6
 */
@SuppressLint("NewApi")
public class ImageLoader {

	private static Context mContext;

	private static ImageLoader instance;

	private static LruCache<String, Bitmap> mCache;

	private static int mMaxMemory;

	private static ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(
			5, 10, 200, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(5));

	public interface ImageLoadListener {
		public void onLoaded(Bitmap bitmap);
	}

	public synchronized static ImageLoader getInstance(Context context) {
		mContext = context;
		if (instance == null) {
			instance = new ImageLoader();
		}
		if (mCache == null) {
			mMaxMemory = getUsableMaxMemorySize(mContext);
			mCache = new LruCache<String, Bitmap>(mMaxMemory) {

				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getByteCount() / 1024;
				}

			};
		}

		return instance;
	}

	/**
	 * 根据web地址，加载图片
	 * 
	 * @param path
	 *            图片的web url
	 */
	public void loadImageFromWeb(String webUrl, ImageLoadListener listener) {
		if (webUrl != null) {
			Bitmap result = getBitmapFromMemCache(webUrl);
			if (result == null) {
				addLoadWebImageThreadInPool(webUrl, listener);
			} else {
				listener.onLoaded(result);
			}
		} else {
			listener.onLoaded(null);
		}
	}

	/**
	 * 从sd卡中载入图片
	 * 
	 * @param sdCardPath
	 * @param listener
	 */
	public void loadImageFromSdCard(String sdCardPath,
			ImageLoadListener listener) {
		if (sdCardPath != null) {
			Bitmap result = getBitmapFromMemCache(sdCardPath);
			if (result == null) {
				// 从sd卡中载入图片并缓存起来
				addLoadSdImageThreadInPool(sdCardPath, listener);
			} else {
				listener.onLoaded(result);
			}
		} else {
			listener.onLoaded(null);
		}

	}

	/**
	 * 加载sd卡图片，并置入缓存中
	 * 
	 * @param path
	 * @param listener
	 */
	private void addLoadSdImageThreadInPool(final String path,
			final ImageLoadListener listener) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				Bitmap bmp = null;
				try {
					FileInputStream fis = new FileInputStream(path);
					bmp = compressInStreamToBitmap(fis);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					if (bmp != null) {
						addBitmapToMemoryCache(path, bmp);
					}
					listener.onLoaded(bmp);
				}

			}
		};
		mThreadPoolExecutor.execute(runnable);
	}

	/**
	 * 加载web图片，并置入缓存中
	 * 
	 * @param path
	 */
	private void addLoadWebImageThreadInPool(final String path,
			final ImageLoadListener listener) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				Bitmap bmp = null;
				try {
					URL myurl = new URL(path);
					// 获得连接
					HttpURLConnection conn = (HttpURLConnection) myurl
							.openConnection();
					conn.setConnectTimeout(6000);// 设置超时
					conn.setDoInput(true);
					conn.setUseCaches(false);// 不缓存
					conn.connect();
					InputStream is = conn.getInputStream();// 获得图片的数据流
					bmp = compressInStreamToBitmap(is);
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bmp != null) {
						addBitmapToMemoryCache(path, bmp);
					}
					listener.onLoaded(bmp);
				}
			}
		};
		mThreadPoolExecutor.execute(runnable);
	}

	/**
	 * 向缓存列表中添加一个bitmap
	 * 
	 * @param key
	 * @param bitmap
	 * @return
	 */
	private boolean addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (mCache != null) {
			mCache.put(key, bitmap);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 从缓存列表中取出一个bitmap
	 * 
	 * @param key
	 * @return
	 */
	private Bitmap getBitmapFromMemCache(String key) {
		return mCache.get(key);
	}

	/**
	 * 获取程序可以使用的最大内存/8
	 * 
	 * @param context
	 * @return
	 */
	private static int getUsableMaxMemorySize(Context context) {
		Device device = Device.getInstance(context);
		return device.usableRuntimeMaxMemory / 8;
	}

	private ImageLoader() {

	}

	/**
	 * 将输入流转化为压缩过后的bitmap
	 * 
	 * @param is
	 * @return
	 */
	private Bitmap compressInStreamToBitmap(InputStream is) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap b = null;
		try {
			b = PictureUtils.scaleCompressImage(BitmapFactory.decodeStream(is));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

}
