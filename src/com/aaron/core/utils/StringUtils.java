package com.aaron.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author lzhiwei
 * 
 *         2014-12-19
 */
public class StringUtils {

	/**
	 * 判断是不是中文
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isChinese(String data) {
		if (data.getBytes().length != data.length())
			return true;
		return false;
	}

	/**
	 * 判断是不是数字
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isDigit(String data) {
		try {
			Long.valueOf(data);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是不是空字符串
	 */
	public static boolean isEmpty(String data) {
		if (data == null || "".equals(data))
			return true;
		return false;
	}

	/**
	 * 转换英文
	 * 
	 * @param num
	 * @return
	 */
	public static String numToValue(int num) {
		String value = "";
		if (num > 0 && num < 28) {
			if (num == 27) {
				value = "#";
				return value;
			}
			value = String.valueOf((char) (num + 64));
		}
		return value;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否为26个字母
	 * 
	 * @param num
	 * @return
	 */
	public static boolean checkLetter(char str) {
		if (str > 'a' && str < 'z') {
			return true;
		} else if (str > 'A' && str < 'Z') {
			return true;
		}
		return false;
	}

	/**
	 * 是否是有效的邮箱地址
	 * 
	 * @param email
	 *            邮箱地址
	 * @return ture:有效 false：无效
	 */
	public static boolean isValidEmail(String email) {
		String str = "^[a-zA-Z0-9_.-]*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	
	/**
	 * 功能：判断字符串是否为空
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNull(String str) {
		return null == str || "".equals(str) || "null".equals(str);
	}
	
	/**
	 * 判断是否是emoji表情
	 * @param codePoint
	 * @return
	 */
	public static boolean isEmojiCharacter(char codePoint) {
		if ((codePoint == 0x2b55) || (codePoint == 0x274c)) {
			return true;
		} else {
			return !((codePoint == 0x0) || (codePoint == 0x9)
					|| (codePoint == 0xA) || (codePoint == 0xD)
					|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
					|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));

		}
	}
}
