package com.bage.study.java.java8;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
 
public class Main {
    public static void main(String[] args) {
        LocalDate currentDate = LocalDate.now(); //获取当前日期

        long ageInYears = ChronoUnit.YEARS.between(currentDate, LocalDate.of(1900, 1, 1)); //计算与1900年1月1日之间的年龄
        long daysSinceEpochStart = currentDate.toEpochDay() - LocalDate.of(1970, 1, 1).toEpochDay(); //计算从1970年1月1日到今天的天数
        long weeksSinceEpochStart = daysSinceEpochStart / 7; //将天数转换为周数

        System.out.println("年龄：" + ageInYears);
        System.out.println("距离公元开始已经过去了 " + weeksSinceEpochStart + " 周");
    }
}