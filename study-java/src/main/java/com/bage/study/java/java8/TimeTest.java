package com.bage.study.java.java8;

import java.time.*;

/**
 * https://www.python100.com/html/92532.html
 */
public class TimeTest {

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);

        ZoneOffset zoneOffset = ZoneOffset.of("+7");
        OffsetDateTime offsetDateTime = localDateTime.atOffset(zoneOffset);
        LocalDateTime localDateTime1 = offsetDateTime.toLocalDateTime();
        System.out.println(offsetDateTime);

        System.out.println(localDateTime1);


        LocalDate birthDate = LocalDate.of(1995, Month.OCTOBER, 24);
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthDate, currentDate).getYears();

        System.out.println("Age: " + age);
    }
}
