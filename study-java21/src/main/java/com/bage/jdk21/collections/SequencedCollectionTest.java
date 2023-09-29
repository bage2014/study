package com.bage.jdk21.collections;

import java.util.ArrayList;
import java.util.List;

public class SequencedCollectionTest {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.addLast(1);
        list.addFirst(2);
        list.addFirst(3);
        list.addFirst(4);
        list.addFirst(7);
        System.out.println(list);

    }
}
