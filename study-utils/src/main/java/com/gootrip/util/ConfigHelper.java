/** 
 * ��ȡ�����ļ�
 */
package com.gootrip.util;

import java.io.File;
import java.net.URL;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationFactory;

/**
 * @author advance
 * 
 */
public class ConfigHelper {
	public ConfigHelper(){
	}
	/**
	 * ��ȡ�����ļ�
	 * @param strfile
	 * @return
	 */
	public static Configuration getConfig(String strfile){
		Configuration config = null;
		try {
			ConfigurationFactory factory = new ConfigurationFactory(strfile);
//			URL configURL = new File(strfile).toURL();
//			factory.setConfigurationFileName(configURL.toString());
			config = factory.getConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//ConfigHelper ch = new ConfigHelper();
		//ch.test();
		try{
			Configuration config = getConfig("config.xml");
			String backColor = config.getString("colors.background");
			System.out.println("color: " + backColor);
			config = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
