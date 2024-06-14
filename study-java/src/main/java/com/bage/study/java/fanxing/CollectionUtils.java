package com.bage.study.java.fanxing;

import java.util.List;

public class CollectionUtils {
    public static void addIntegers(List<? super Integer> list) {
        list.add(10);
        list.add(20);
        list.add(30);
    }
}
