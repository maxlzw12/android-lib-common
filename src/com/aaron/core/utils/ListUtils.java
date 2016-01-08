package com.aaron.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * list的工具类(使用此类，需要重写对象的equals方法)
 * 
 * @author lzhiwei
 * 
 *         2014-12-19
 */
public class ListUtils {
	/**
	 * 获取集合的差集
	 * 
	 * @param listOne
	 *            集合1
	 * @param listTwo
	 *            集合2
	 * @return 集合1 - 集合2
	 */
	public static List<?> differenceOfSets(List<?> listOne, List<?> listTwo) {
		List<Object> list = new ArrayList<Object>(
				Arrays.asList(new Object[listOne.size()]));
		Collections.copy(list, listOne);
		list.removeAll(listTwo);
		return list;
	}

	/**
	 * 集合的并集
	 * 
	 * @param listOne
	 *            集合1
	 * @param listTwo
	 *            集合2
	 * @return 集合1 并 集合2
	 */
	public static List<?> unionOfSets(List<?> listOne, List<?> listTwo) {
		List<Object> list = new ArrayList<Object>(
				Arrays.asList(new Object[listOne.size()]));
		Collections.copy(list, listOne);
		list.addAll(listTwo);
		return list;
	}

	/**
	 * 集合的交集
	 * 
	 * @param listOne
	 *            集合1
	 * @param listTwo
	 *            集合2
	 * @return 集合1 交 集合2
	 */
	public List<?> intersectOfSets(List<?> listOne, List<?> listTwo) {
		List<Object> list = new ArrayList<Object>(
				Arrays.asList(new Object[listOne.size()]));
		Collections.copy(list, listOne);
		list.retainAll(listTwo);
		return list;
	}
}
