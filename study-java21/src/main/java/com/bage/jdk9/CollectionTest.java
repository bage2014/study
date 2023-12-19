package com.bage.jdk9;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionTest {

    public static void main(String[] args) {

        List<String> fruits = List.of("Apple", "Banana", "Orange");
        System.out.println(fruits);

        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
        System.out.println(numbers);


        Map<String, Integer> studentScores = Map.of("Alice", 95, "Bob", 80, "Charlie", 90);
        System.out.println(studentScores);
    }
}
