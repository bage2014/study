package com.bage.study.java.reg;

import java.util.regex.Pattern;

public class RegTest {

    public static void main(String[] args) {
        basic();
    }

    private static void basic() {
        String pattern = ".*bage.*";
        System.out.println(pattern + " isMatch:" + Pattern.matches(pattern, "bage"));
        System.out.println(pattern + " isMatch:" + Pattern.matches(pattern, "bag"));
    }
}
