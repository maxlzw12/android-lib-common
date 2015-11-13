package com.aaron.core;

import java.io.File;

import com.aaron.core.log.LogUtils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

/**
 * 应用的工具类
 * 
 * @author lzhiwei
 * @since 2014-7-24
 */
public class ApkUtils {
	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @param packName
	 * @return
	 */
	public static int getVersionCode(Context context, String packName) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(packName, 0).versionCode;
		} catch (NameNotFoundException e) {
			LogUtils.e(ApkUtils.class.getSimpleName(), e.getMessage());
		}
		return verCode;
	}
	
	/**
	 * 获取版本名
	 * @param context
	 * @param packName
	 * @return
	 */
	public static String getVersionName(Context context, String packName){
		String verName = null;
		try {
			verName = context.getPackageManager().getPackageInfo(packName, 0).versionName;
		} catch (NameNotFoundException e) {
			LogUtils.e(ApkUtils.class.getSimpleName(), e.getMessage());
		}
		return verName;
	}
	
	/**
	 * 根据安装包路径安装apk
	 * @param mContex 上下文
	 * @param apkPath apk路径
	 */
	public static void installApk(Context mContex,String apkPath){
        Intent intent = new Intent(Intent.ACTION_VIEW);  
        intent.setDataAndType(Uri.fromFile(new File(apkPath)),  
                "application/vnd.android.package-archive");  
        mContex.startActivity(intent);
	}
	
}
