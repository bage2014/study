package com.bage.study.java.java8;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class LocalDateTimeTest {

    public static void main(String[] args) {

        LocalDateTime dateTime = LocalDateTime.of(
                2019, 10, 30,
                14, 30, 0, 0);
        dateTime = dateTime.withYear(2020);
        System.out.println(dateTime);


        int month = dateTime.getMonthValue();  // 10
        int minute = dateTime.getMinute();     // 30
        System.out.println(month);
        System.out.println(minute);


        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();   // WEDNESDAY
        int dayOfMonth = dateTime.getDayOfMonth();       // 30

        System.out.println(dayOfWeek);
        System.out.println(dayOfMonth);
    }

}
