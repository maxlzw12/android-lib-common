package com.aaron.core.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * 设备信息工具类(单例)
 * 
 * @author lzhiwei
 * 
 *         2014-12-31
 */
public class Device {
	/**
	 * 设备Mac地址
	 */
	public String macAddress;
	/**
	 * 手机型号
	 */
	public String model;
	/**
	 * 本机号码
	 */
	public String lineNumber;
	/**
	 * sdk版本号
	 */
	public String sdkVersion;
	/**
	 * 版本号
	 */
	public String releaseVersion;
	/**
	 * 生产厂商
	 */
	public String manufacturer;
	/**
	 * 程序运行时，可用最大内存（单位：KB）
	 */
	public int usableRuntimeMaxMemory;
	
	private static Device instance = null;
	
	private Device(Context context){
		loadDeviceInfo(context);
		usableRuntimeMaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	}
	
	/**
	 * 载入设备信息
	 */
	private void loadDeviceInfo(Context context) {
		TelephonyManager phoneMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		lineNumber = phoneMgr.getLine1Number();
		macAddress = getMacAddress(context);
		model = Build.MODEL;
		sdkVersion = Build.VERSION.SDK;
		releaseVersion = Build.VERSION.RELEASE;
		manufacturer = Build.MANUFACTURER;
	}

	/**
	 * 获取本类实例
	 * @param context
	 * @return
	 */
	public static synchronized Device getInstance(Context context){
		if(instance==null){
			instance = new Device(context);
		}
		return instance;
	}

	/**
	 * 获取设备Mac地址
	 * 
	 * @param context
	 * @return 设备Mac地址
	 */
	private String getMacAddress(Context context) {
		String mac = "";
		// 获取wifi管理器
		WifiManager wifiMng = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfor = wifiMng.getConnectionInfo();
		mac = wifiInfor.getMacAddress();
		return mac;
	}

}
