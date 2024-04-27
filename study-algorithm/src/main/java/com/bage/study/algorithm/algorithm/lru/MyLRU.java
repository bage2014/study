package com.bage.study.algorithm.algorithm.lru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyLRU {

    private String[] arr = new String[]{};
    private Map<String, Integer> arrKeys = new HashMap<>();

    public MyLRU(int n) {
        this.arr = new String[n];
    }

    public void set(String key) {
        Integer index = arrKeys.get(key);
        if (index == null) {
            // 不存在
            int defaultIndex = arr.length - 1;
            arrKeys.put(key,defaultIndex);
            arr[defaultIndex] = key;
            return;
        }
        // 已存在
        arrKeys.put(key,index);
        arr[index] = key;



    }

    public void print() {
        for (String item : arr) {
            System.out.print(item + ",");
        }
        System.out.println();

//        arrKeys.forEach((key,value)->{
//            System.out.print(item + ",");
//        });
//        System.out.println();
    }

}
