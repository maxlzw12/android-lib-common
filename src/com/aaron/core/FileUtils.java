package com.aaron.core;

import java.io.File;

/**
 * 文件操作工具类
 * @author lzhiwei
 *
 * 2014-12-24
 */
public class FileUtils {

	/**
	 * 根据文件的路径获取文件的大小
	 * 
	 * @param filePath
	 *            文件的路径
	 * @return 文件的大小
	 */
	public static long getFileSize(String filePath) {
		long fileSize = 0;
		if(filePath==null||filePath.equals("")){
			return fileSize;
		}
		File f = new File(filePath);
		if (f.exists() && f.isFile()) {
			fileSize = f.length();
		}
		return fileSize;
	}
	
	/**
	 * 根据文件的路径获取文件名
	 * @param pathandname 带文件名的路径
	 * @return 文件名
	 */
	public static String getFileName(String pathandname){  
        int start=pathandname.lastIndexOf("/");  
        int end=pathandname.lastIndexOf(".");  
        if(start!=-1 && end!=-1){  
            return pathandname.substring(start+1,end);    
        }else{  
            return null;  
        }  
    }  
	
}
