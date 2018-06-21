package com.bage.utils;

import java.util.Date;

public class DateUtils {

	public static Date getJwtsNotBeforeDate() {
		return new Date(System.currentTimeMillis() + 5*60*1000);
	}
	
	
	public static Date getJwtsExpirationDate() {
		return new Date(System.currentTimeMillis() + 10*50*60*1000);
	}
	

	public static Date now() {
		return new Date();
	}
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		System.out.println(DateUtils.now().toLocaleString());
		System.out.println(DateUtils.getJwtsNotBeforeDate().toLocaleString());
		System.out.println(DateUtils.getJwtsExpirationDate().toLocaleString());

	}
	
}
