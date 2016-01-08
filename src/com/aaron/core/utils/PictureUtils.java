package com.aaron.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 图片工具类
 * 
 * @author lzhiwei
 * 
 *         2014-12-16
 */
public class PictureUtils {

	/**
	 * 图片按比例大小压缩方法（根据Bitmap图片压缩）
	 * 
	 * @param image
	 *            需要压缩的图片
	 * @return 压缩过后的图片
	 */
	public static Bitmap scaleCompressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
		if (baos.toByteArray().length / 1024 > 1024) {
			// 重置baos即清空baos
			baos.reset();
			// 这里压缩50%，把压缩后的数据存放到baos中
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		// 这里设置高度为800f
		float hh = 800f;
		// 这里设置宽度为480f
		float ww = 480f;
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		// be=1表示不缩放
		int be = 1;
		// 如果宽度大的话根据宽度固定大小缩放
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
			// 如果高度高的话根据宽度固定大小缩放
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		// 设置缩放比例
		newOpts.inSampleSize = be;
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		// 压缩好比例大小后再进行质量压缩
		return compressImage(bitmap);
	}

	/**
	 * 根据文件路径获取文件名
	 * 
	 * @param filePath
	 *            文件的绝对路径
	 * @return 文件名
	 */
	public static String getFileNameByPath(String filePath) {
		if (null == filePath) {
			return "";
		}
		String fName = filePath.trim();
		String fileName = fName.substring(fName.lastIndexOf("/") + 1);
		return fileName;
	}

	/**
	 * 保存bitmap文件
	 * 
	 * @param filePath
	 *            要保存的位置
	 * @param mBitmap
	 *            需要保存的bitmap
	 */
	public static void saveBitmap(String filePath, Bitmap mBitmap) {
		File f = new File(filePath);
		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 载入sd卡中的图片（压缩处理）
	 * 
	 * @param path
	 *            路径
	 * @param iv
	 *            要显示的控件
	 */
	public static void loadImageFromSdCard(String path, ImageView iv) {
		ImageViewParams params = getImageViewParams(iv);
		Bitmap bitmap = loadBigPictureFromPath(path, params.width, params.height);
		iv.setImageBitmap(bitmap);
	}

	/**
	 * 加载大图
	 * 
	 * @param path
	 *            大图的路径
	 * @param reqWidth
	 *            期望的宽
	 * @param reqHeight
	 *            期望的高
	 * @return
	 */
	private static Bitmap loadBigPictureFromPath(String path, int reqWidth,
			int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		Bitmap bitmapImage = BitmapFactory.decodeFile(path, options);
		return bitmapImage;
	}

	/**
	 * 计算inSampleSize
	 * 
	 * @param options
	 *            BitmapFactory.Options
	 * @param reqWidth
	 *            期望的宽
	 * @param reqHeight
	 *            期望的高
	 * @return inSampleSize
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 质量压缩方法
	 * 
	 * @param image
	 * @return
	 */
	private static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 100;
		// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
		while (baos.toByteArray().length / 1024 > 100) {
			// 重置baos即清空baos
			baos.reset();
			// 这里压缩options%，把压缩后的数据存放到baos中
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			// 每次都减少10
			options -= 10;
		}
		// 把压缩后的数据baos存放到ByteArrayInputStream中
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		// 把ByteArrayInputStream数据生成图片
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}

	private static class ImageViewParams {
		public int width;
		public int height;
	}

	/**
	 * a.根据ImageView获得适应的压缩的宽和高
	 * 
	 * @param imageView
	 * @return
	 */
	private static ImageViewParams getImageViewParams(ImageView imageView) {
		ImageViewParams imageParams = new ImageViewParams();
		// 获取ImageView控件的宽和高
		DisplayMetrics displayMetrics = imageView.getContext().getResources()
				.getDisplayMetrics();
		ViewGroup.LayoutParams lp = imageView.getLayoutParams();

		// 获取imageview的实际宽度
		int width = imageView.getWidth();
		// 首先企图通过getWidth获取显示的宽,如果返回0;那么再去看看它有没有在布局文件中声明宽
		if (width <= 0) {
			width = lp.width;
		}
		// 如果布局文件中也没有精确值，那么我们再去看看它有没有设置最大值
		/**
		 * 可以看到这里或者最大宽度，我们用的反射，而不是getMaxWidth()；为啥呢，因为getMaxWidth竟然要API
		 * 16，我也是醉了；为了兼容性，我们采用反射的方案。
		 */
		if (width <= 0) {
			width = getImageViewFieldValue(imageView, "mMaxWidth");
		}
		// 如果最大值也没设置，那么我们只有拿出我们的终极方案，使用我们的屏幕宽度
		if (width <= 0) {
			width = displayMetrics.widthPixels;
		}

		// 获取imageview的实际高度
		int height = imageView.getHeight();
		if (height <= 0) {
			height = lp.height;
		}
		if (height <= 0) {
			height = getImageViewFieldValue(imageView, "mMaxHeight");
		}
		if (height <= 0) {
			height = displayMetrics.heightPixels;
		}
		imageParams.width = width;
		imageParams.height = height;
		return imageParams;
	}

	/**
	 * 通过反射获取ImageView控件的某个属性值
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	private static int getImageViewFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			// Field：属性
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = field.getInt(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}
