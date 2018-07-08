package com.bage.study.java.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 文件操作工具 
 * @author bage
 *
 */
public class FileUtils {

	public static int writeToFile(String content,String path){
		return 0;
	}
	
	/**
	 * 往文件写对象
	 * @param obj
	 * @param path
	 * @return
	 */
	public static int writeToFile(Object obj,String path){
		FileOutputStream fops = null;
		ObjectOutputStream oops = null;
		try {
			fops = new FileOutputStream(new File(path));
			oops = new ObjectOutputStream(fops);
			oops.writeObject(obj);
			oops.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(oops != null){
					oops.close();
				}
				if(fops != null){
					fops.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	/**
	 * 从文件读取对象
	 * @param obj
	 * @param path
	 * @return
	 */
	public static Object readObjectFromFile(String path){
		Object obj = null;
		FileInputStream fips = null;
		ObjectInputStream oips = null;
		try {
			fips = new FileInputStream(new File(path));
			oips = new ObjectInputStream(fips);
			obj = oips.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(oips != null){
					oips.close();
				}
				if(fips != null){
					fips.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
	
}
