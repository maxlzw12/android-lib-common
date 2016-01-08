package com.aaron.core.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘工具类
 * 
 * @author lzhiwei
 * 
 *         2014-12-19
 */
public class KeyBoardUtils {

	/**
	 * 强制隐藏键盘
	 * 
	 * @param activity
	 * @param flags
	 *            键盘隐藏模式
	 */
	public static void hideSoftInput(Activity activity, int flags) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		try {
			imm.hideSoftInputFromWindow(activity.getCurrentFocus()
					.getWindowToken(), flags); // 强制隐藏键盘
		} catch (Exception e) {
		}
	}

	/**
	 * 强制隐藏键盘
	 * 
	 * @param activity
	 * @param flags
	 *            键盘隐藏模式
	 */
	public static void hideSoftInput(Activity activity, View view, int flags) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), flags); // 强制隐藏键盘
	}

	/**
	 * 强制显示软键盘
	 * 
	 * @param activity
	 * @param view
	 * @param flags
	 *            键盘显示模式
	 */
	public static void showSoftInput(Activity activity, View view, int flags) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, flags);// 强制显示键盘
	}
}
