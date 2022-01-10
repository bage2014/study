package com.bage.study.java.java8;

import java.time.LocalDate;
import java.time.Period;

/**
 * @author bage
 *
 */
public class PeriodTest {

	
	public static void main(String[] args) {
		LocalDate now = LocalDate.now();

		System.out.println(Period.between(now, now.plusDays(2)).toString());

	}
}
