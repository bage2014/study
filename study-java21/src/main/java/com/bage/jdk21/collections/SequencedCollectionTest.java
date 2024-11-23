package com.bage.jdk21.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SequencedCollectionTest {
    public static void main(String[] args) {
        // addFirst addLast
        List<Integer> list = new ArrayList<>();
        list.addLast(1);
        list.addFirst(2);
        list.addFirst(3);
        list.addFirst(4);
        list.addFirst(7);
        System.out.println(list);

        // of
        List.of();
        List.of("Hello", "World");
        List.of(1, 2, 3);
        Set.of();
        Set.of("Hello", "World");
        Set.of(1, 2, 3);
        Map.of();
        Map.of("Hello", 1, "World", 2);

        // takeWhile
        List<Integer> integerList = List.of(11, 33, 66, 8, 9, 13);
        integerList.stream().takeWhile(x -> x < 50).forEach(System.out::println);// 11 33


    }
}
