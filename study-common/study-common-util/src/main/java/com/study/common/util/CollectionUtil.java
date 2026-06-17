package com.study.common.util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionUtil {

    private CollectionUtil() {
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static <T> List<T> emptyIfNull(List<T> list) {
        return list == null ? new ArrayList<>() : list;
    }

    public static <T> Set<T> emptyIfNull(Set<T> set) {
        return set == null ? new HashSet<>() : set;
    }

    public static <K, V> Map<K, V> emptyIfNull(Map<K, V> map) {
        return map == null ? new HashMap<>() : map;
    }

    public static <T> T first(List<T> list) {
        return isEmpty(list) ? null : list.get(0);
    }

    public static <T> T last(List<T> list) {
        return isEmpty(list) ? null : list.get(list.size() - 1);
    }

    public static <T, K> Map<K, T> toMap(List<T> list, Function<T, K> keyExtractor) {
        if (isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(keyExtractor, item -> item, (k1, k2) -> k1));
    }

    public static <T, K, V> Map<K, V> toMap(List<T> list, Function<T, K> keyExtractor, Function<T, V> valueExtractor) {
        if (isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(keyExtractor, valueExtractor, (k1, k2) -> k1));
    }

    public static <T, K> Map<K, List<T>> groupBy(List<T> list, Function<T, K> classifier) {
        if (isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(classifier));
    }

    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .map(mapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static <T> List<T> filter(List<T> list, java.util.function.Predicate<T> predicate) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static <T> T findFirst(List<T> list, java.util.function.Predicate<T> predicate) {
        if (isEmpty(list)) {
            return null;
        }
        return list.stream()
                .filter(Objects::nonNull)
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }
}