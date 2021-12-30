package com.bage.study.java.java8;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * Java 8的新特性
 * @author bage
 *
 */
public class DurationTest {

	
	public static void main(String[] args) {
		LocalDateTime now = LocalDateTime.now();
		LocalDate now2 = LocalDate.now();

		System.out.println(Duration.between(now, now.plusHours(2)).toString());
		System.out.println(Duration.between(now, now.plusDays(10)).toString());
		System.out.println(Duration.between(now, now.plusMinutes(2)).toString());


		System.out.println(Period.between(now2, now2.plusDays(10)).toString());

	}
}
