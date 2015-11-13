package com.aaron.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 * @author lzhiwei
 * @since 2014-9-3
 */
public class DateUtils {

	public final static String DATA_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public final static String YMD_PATTERN = "yyyy-MM-dd";
	
	final static SimpleDateFormat sf = new SimpleDateFormat(DATA_PATTERN);

	// 时间对应的long值
	long nowtime;

	/**
	 * 以当前系统时间构造DateTime对象
	 * 
	 */
	public DateUtils() {
		nowtime = System.currentTimeMillis();
	}

	/**
	 * 以指定时间构造DateTime对象,如果指定的时间不符合格式，将使用 系统当前时间构造对象
	 * 
	 * @param time
	 *            时间格式为yyyy-MM-dd HH:mm:ss的字符串
	 */
	public DateUtils(String time) {
		this(time, DATA_PATTERN);
	}

	/**
	 * 以指定时间和指定时间格式构造DateTime对象. 如果指定的时间不符合格式，将使用系统当前时间构造对象
	 * 
	 * @param time
	 *            指定的时间
	 * @param timePattern
	 *            指定的日间格式
	 */
	public DateUtils(String time, String timePattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(timePattern);
			Date d = sdf.parse(time);
			nowtime = d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			nowtime = System.currentTimeMillis();
		}
	}

	/**
	 * 取回系统当前时间 时间格式yyyy-MM-dd hh:mm:ss
	 * 
	 * @return yyyy-MM-dd hh:mm:ss格式的时间字符串
	 */
	public String getNowTime() {
		String retValue = null;
		retValue = sf.format(new Date(nowtime));
		return retValue;
	}

	/**
	 * 按指定日期、时间格式返回当前日期
	 * 
	 * @param datePattern
	 *            格式字符串
	 * @return 格式化的日期、时间字符串
	 */
	public String getNowTime(String datePattern) {
		String retValue = null;
		SimpleDateFormat sf = new SimpleDateFormat(datePattern);
		retValue = sf.format(new Date(nowtime));
		return retValue;
	}

	/**
	 * 返回4位的年份,如'2004'
	 * 
	 * @return 年
	 */
	public String getYear() {
		return getNowTime("yyyy");
	}

	/**
	 * 返回月份. 一位的月份数字，如8月将返回8
	 * 
	 * @return 月份 如果8月返回8，12月返回12
	 */
	public String getMonth() {

		return getNowTime("M");
	}

	/**
	 * 返回一个月中的第几天
	 * 
	 * @return 天 一位的天数，如当前是4月1日将返回'1'
	 */
	public String getDay() {
		return getNowTime("d");
	}

	/**
	 * 返回24小时制的小时
	 * 
	 * @return 小时
	 */
	public String getHour() {
		return getNowTime("HH");
	}

	/**
	 * 返回分钟
	 * 
	 * @return 分钟 一位的分钟数
	 */
	public String getMinute() {
		return getNowTime("m");
	}

	/**
	 * 返回秒
	 * 
	 * @return 秒 一位的秒数
	 */
	public String getSecond() {
		return getNowTime("s");
	}

	/**
	 * 只返回日期 如2004-08-12.月份和日期都是两位，不足的在前面补0
	 * 
	 * @return 日期
	 */
	public String getDateOnly() {
		return getNowTime("yyyy-MM-dd");
	}

	/**
	 * 将long 类型的时间转换为字符串类型
	 * @param datetime  long类型的时间类型
	 * @return 返回的字符串类型的时间
	 */
	public static String getDateTimeByLong(long datetime) {
		String retValue = "";
		if(datetime>0){
			SimpleDateFormat sf = new SimpleDateFormat(DATA_PATTERN);
			retValue = sf.format(new Date(datetime));
		}
		return retValue;
	}
	
	/**
	 * 只返回时间 如12:20:30.时间为24小时制,分钟和秒数都是两位，不足补0
	 * 
	 * @return 时间
	 */
	public String getTimeOnly() {
		return getNowTime("HH:mm:ss");
	}

	/**
	 * 调整年份
	 * 
	 * @param i
	 *            要调整的基数，正表示加，负表示减
	 */
	public void adjustYear(int i) {
		adjustTime(i, 0, 0, 0, 0);
	}

	/**
	 * 调整月份
	 * 
	 * @param i
	 *            要调整的基数，正表示加，负表示减
	 */
	public void adjustMonth(int i) {
		adjustTime(0, i, 0, 0, 0);
	}

	/**
	 * 调整天数
	 * 
	 * @param i
	 *            要调整的基数，正表示加，负表示减
	 */
	public void adjustDay(int i) {
		adjustTime(0, 0, i, 0, 0);
	}

	/**
	 * 调整小时
	 * 
	 * @param i
	 *            要调整的基数，正表示加，负表示减
	 */
	public void adjustHour(int i) {
		adjustTime(0, 0, 0, i, 0);
	}

	/**
	 * 调整分数
	 * 
	 * @param i
	 *            要调整的基数，正表示加，负表示减
	 */
	public void adjustMinute(int i) {
		adjustTime(0, 0, 0, 0, i);
	}

	/**
	 * 调整时间
	 * 
	 * @param y
	 *            年
	 * @param m
	 *            月
	 * @param d
	 *            日
	 * @param h
	 *            小时
	 * @param mm
	 *            分钟
	 */
	protected void adjustTime(int y, int m, int d, int h, int mm) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(nowtime);
		cal.add(Calendar.YEAR, y);
		cal.add(Calendar.MONTH, m);
		cal.add(Calendar.DAY_OF_MONTH, d);
		cal.add(Calendar.HOUR_OF_DAY, h);
		cal.add(Calendar.MINUTE, mm);
		nowtime = cal.getTimeInMillis();
	}

	/**
	 * 返回当前日期.
	 * 
	 * @return yyyy-MM-dd HH:mm:ss格式的日期／时间
	 */
	public static String getNowDateTime() {
		DateUtils dt = new DateUtils();
		return dt.getNowTime();
	}

	/**
	 * 按指定格式返回当前日期.
	 * 
	 * @param pattern
	 *            时间格式
	 * @return 格式化的日期／时间
	 */
	public static String getNowDateTime(String pattern) {
		DateUtils dt = new DateUtils();
		return dt.getNowTime(pattern);
	}

	/**
	 * 按指定格式转换输出指定的日期
	 * 
	 * @param date
	 *            要输出的日期
	 * @param datePattern
	 *            要输出的时间格式
	 * @return 格式化后的字符串
	 */
	public static String getTime(Date date, String datePattern) {
		String retValue = null;
		SimpleDateFormat sf = new SimpleDateFormat(datePattern);
		retValue = sf.format(date);
		return retValue;
	}

	/**
	 * 把字符串格式化成相应的日期格式
	 * 
	 * @param s
	 *            日期字符串格式 String
	 * @param datePattern
	 *            日期模式 String
	 * @return 返回格式化后的日期
	 */
	public static Date parseDate(String s, String datePattern) {

		s = s.replaceAll(" ", "");
		if (s == null || s.equals(""))
			return null;

		Date d = null;
		SimpleDateFormat sf = new SimpleDateFormat(datePattern);
		try {
			d = sf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * 返回字符串
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getNowTime();
	}

}
