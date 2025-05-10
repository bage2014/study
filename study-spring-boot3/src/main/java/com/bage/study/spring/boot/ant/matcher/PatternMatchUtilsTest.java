package com.bage.study.spring.boot.ant.matcher;

import org.springframework.util.PatternMatchUtils;

public class PatternMatchUtilsTest {
    public static void main(String[] args) {


        boolean matches1 = PatternMatchUtils.simpleMatch("user*", "username");  // true
        boolean matches2 = PatternMatchUtils.simpleMatch("user?", "user1");  // true
        boolean matches3 = PatternMatchUtils.simpleMatch(
                new String[]{"user*", "admin*"}, "username");  // true

        System.out.println("matches1 = " + matches1);
        System.out.println("matches2 = " + matches2);
        System.out.println("matches3 = " + matches3);
    }
}
