package com.bage.zhongxinyun.test;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class HttpClients {

	public static void get(String url,String docId) {
		URL source;
		try {
			source = new URL(url);
			File destination = new File("C:\\Users\\luruihua\\git\\study\\study\\study-gateway-test\\img\\" + docId + ".png" );
			FileUtils.copyURLToFile(source , destination );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
