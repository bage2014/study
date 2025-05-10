package com.bage.study.spring.boot.collection;

import org.springframework.util.CollectionUtils;

import java.util.*;


/**
 * https://juejin.cn/post/7497173460423753740
 */
public class SpringCollectionUtils {

    public static void main(String[] args) {
        // 检查集合是否为空
        boolean isEmpty = CollectionUtils.isEmpty(new ArrayList<>());  // true
        boolean isEmpty2 = CollectionUtils.isEmpty(Collections.emptyList());  // true

// 集合操作
//        List<String> list1 = Arrays.asList("a", "b", "c");
//        List<String> list2 = Arrays.asList("b", "c", "d");
//        Collection<String> intersection = CollectionUtils.intersection(list1, list2);  // [b, c]

// 合并集合
        List<String> target = new ArrayList<>();
        CollectionUtils.mergeArrayIntoCollection(new String[]{"a", "b"}, target);

    }
}
