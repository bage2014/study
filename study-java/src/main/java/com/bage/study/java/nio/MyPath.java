package com.bage.study.java.nio;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Path + Files 学习
 * @author bage
 *
 */
public class MyPath {

	public static void main(String[] args) throws Exception {
		
		String url = "hello.txt";
		// 创建文件
		createFile(url);
		
		// 修改文件信息
		updateFile(url);
		
		// 删除
		
		
		deleteFile(url);
	}

	private static void updateFile(String url) throws Exception {
		Path source= Paths.get(url);
		Path target= Paths.get("hellonew.txt");
		if(Files.exists(source)){
			Files.move(source, target); // 创建文件
		}
		System.out.println(Files.exists(source));
		System.out.println(Files.exists(target));

	}

	private static void deleteFile(String url)  throws Exception {
		Path path= Paths.get(url);
		if(Files.exists(path)){
			Files.delete(path); // 删除文件
		}
		System.out.println(Files.exists(path));
	}

	private static void createFile(String url) throws Exception {
		Path path= Paths.get(url);
		if(!Files.exists(path)){
			Files.createFile(path); // 创建文件
		}
		System.out.println(Files.exists(path));
		System.out.println(path.toAbsolutePath().toUri().toString());
	}
	
}
