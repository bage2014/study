package com.bage.study.best.practice.utils;

import java.time.*;
import java.util.Date;

public class DateUtils {

	public static String getCurrentDate(){
		return "";
	}
	
	public static String getCurrentDateTime(){
		return "";
	}

	public static LocalDate date2LocalDate(Date date){
		/**
		 * instant : 2018-10-08T09:50:21.852Z
		 * Zone : UTC+0
		 * 注意这里默认的Instant是比北京少8个时区
		 */
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime date2LocalDateTime(Date date){
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static Date localDateTime2Date(LocalDateTime localDateTime){
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

	}

	public static Date localDate2Date(LocalDate localDate){
		LocalDateTime of = LocalDateTime.of(localDate, LocalTime.MIN);
		return Date.from(of.atZone(ZoneId.systemDefault()).toInstant());
	}
}
