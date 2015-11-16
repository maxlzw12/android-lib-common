package com.aaron.core.volley;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * volley的控制类
 * 
 * @author lzhiwei
 * 
 *         2015-9-7
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class VolleyController {

	private static VolleyController mInstanse;

	private RequestQueue mRequestQueue;

	private static LruCache<String, Bitmap> mImageCache;

	private VolleyController() {
	}

	private VolleyController(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}

	@SuppressLint("NewApi")
	public synchronized static VolleyController getInstanse(Context context) {
		if (mInstanse == null) {
			mInstanse = new VolleyController(context);
		}
		if (mImageCache == null) {
			int maxMemorySize = (int) (Runtime.getRuntime().maxMemory() / 1024);
			mImageCache = new LruCache<String, Bitmap>(maxMemorySize / 8) {

				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getByteCount() / 1024;
				}

			};
		}

		return mInstanse;
	}

	/**
	 * post类型http请求
	 * 
	 * @param url
	 *            网络请求地址
	 * @param tag
	 *            标签
	 * @param onResponseListener
	 *            请求结果回调
	 * @param onErrorResponseListener
	 *            请求错误结果回调
	 */
	public void postHttp(String url, String tag,
			Response.Listener<String> onResponseListener,
			Response.ErrorListener onErrorResponseListener) {
		StringRequest strRequest = new StringRequest(Method.POST, url,
				onResponseListener, onErrorResponseListener);
		strRequest.setTag(tag);
		mRequestQueue.add(strRequest);
	}

	/**
	 * get类型http请求
	 * 
	 * @param url
	 *            网络请求地址
	 * @param tag
	 *            标签
	 * @param onResponseListener
	 *            请求结果回调
	 * @param onErrorResponseListener
	 *            请求错误结果回调
	 */
	public void getHttp(String url, String tag,
			Response.Listener<String> onResponseListener,
			Response.ErrorListener onErrorResponseListener) {
		StringRequest strRequest = new StringRequest(Method.GET, url,
				onResponseListener, onErrorResponseListener);
		strRequest.setTag(tag);
		mRequestQueue.add(strRequest);
	}

	/**
	 * 终止所有请求
	 */
	public void cancelAllRequest() {
		mRequestQueue.cancelAll(this);
	}

	/**
	 * 终止带某tag的请求
	 * 
	 * @param tag
	 */
	public void cancelRequsetByTag(String tag) {
		mRequestQueue.cancelAll(tag);
	}

	/**
	 * 加载web图片
	 * 
	 * @param imageView
	 *            ImageView实例
	 * @param loadingId
	 *            加载中的图片id（资源id）
	 * @param loadFailedId
	 *            加载失败的图片id（资源id）
	 */
	public void loadWebImage(String imageUrl, ImageView imageView,
			int loadingId, int loadFailedId) {
		ImageLoader imageLoader = new ImageLoader(mRequestQueue,
				new ImageCache() {

					@Override
					public void putBitmap(String key, Bitmap value) {
						mImageCache.put(key, value);
					}

					@Override
					public Bitmap getBitmap(String key) {
						return mImageCache.get(key);
					}
				});
		ImageListener listener = ImageLoader.getImageListener(imageView,
				loadingId, loadFailedId);
		imageLoader.get(imageUrl, listener);
	}

	/**
	 * 以get方式获取json数据
	 * 
	 * @param jsonUrl
	 *            json的url
	 * @param tag
	 *            标签
	 * @param responseListener
	 *            反馈监听器
	 * @param errorListener
	 *            错误监听器
	 */
	public void getJson(String jsonUrl, String tag,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
				jsonUrl, null, responseListener, errorListener);
		request.setTag(tag);
		mRequestQueue.add(request);
	}

	/**
	 * 以post方式获取json数据
	 * 
	 * @param jsonUrl
	 *            json的url
	 * @param tag
	 *            标签
	 * @param responseListener
	 *            反馈监听器
	 * @param errorListener
	 *            反馈监听器
	 */
	public void postJson(String jsonUrl, String tag,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
				jsonUrl, null, responseListener, errorListener);
		request.setTag(tag);
		mRequestQueue.add(request);
	}
}
