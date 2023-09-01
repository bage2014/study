package com.bage.study.java.java8;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Java 8的新特性
 *
 * @author bage
 */
public class JavaStream {


    private static final int MB = 1024 * 1024;
    private static final List<Object> temp = new ArrayList<>();

    public static void main(String[] args) throws Exception {
//        basic();

        int n = 20;
        while (n -- > 0){
            Thread.sleep(10 * 1000L);
            bigArray();
        }
    }

    private static void basic() {
        String hh = Arrays.asList("a", "b")
                .stream()
                .peek(item -> System.out.println(item)) // 临时拿一下
                .collect(Collectors.joining(","));
        System.out.println(hh);

        int result = Arrays.asList(1, 2, 3, 4)
                .stream()
                .reduce((a, b) -> a + b)
                .get();

        System.out.println(result);
    }

    private static void bigArray() throws InterruptedException {
        Thread.sleep(10 * 1000L);
        int step = 500;
        for (int i = 0; i < step; i++) {
            temp.add(new byte[MB]);
        }
        System.out.println(info());

        Thread.sleep(10 * 1000L);
        List<Object> collect = temp.stream().collect(Collectors.toList());
        System.out.println(info());

        Thread.sleep(10 * 1000L);
        List<Object> collect2 = collect.stream().collect(Collectors.toList());
        System.out.println(info());

        Thread.sleep(10 * 1000L);
        List<Object> collect3 = collect2.stream().collect(Collectors.toList());
        System.out.println(info());
    }

    public static Object info() {
        Runtime runtime = Runtime.getRuntime();

        Map<String, Object> map = new HashMap<>();
        map.put("maxMemory", runtime.maxMemory() / MB + " MB;");
        map.put("freeMemory", runtime.freeMemory() / MB + " MB;");
        map.put("totalMemory", runtime.totalMemory() / MB + " MB;");
        return map;
    }

}
