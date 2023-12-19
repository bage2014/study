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


        LocalDate birthDate1 = LocalDate.of(1995, 12, 17);
        LocalDate birthDate2 = LocalDate.of(1995, 12, 18);
        LocalDate birthDate3 = LocalDate.of(1995, 12, 19);
        LocalDate currentDate = LocalDate.now();
        int age1 = Period.between(birthDate1, currentDate).getYears();
        int age2 = Period.between(birthDate2, currentDate).getYears();
        int age3 = Period.between(birthDate3, currentDate).getYears();

        System.out.println("Age: " + age1);
        System.out.println("Age: " + age2);
        System.out.println("Age: " + age2);
    }
}
