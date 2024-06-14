package com.bage.study.java.fanxing;

import java.util.ArrayList;
import java.util.List;

/**
 *  https://juejin.cn/post/7379806770363367451
 */
public class Main {
    public static void main(String[] args) {
        Box<Integer> integerBox = new Box<>();
        integerBox.setValue(10);
        System.out.println("Integer Box Value: " + integerBox.getValue());

        Box<Double> doubleBox = new Box<>();
        doubleBox.setValue(10.5);
        System.out.println("Double Box Value: " + doubleBox.getValue());

        // Box<String> stringBox = new Box<>(); // 编译错误，因为 String 不是 Number 的子类


        List<Number> numberList = new ArrayList<>();
        CollectionUtils.addIntegers(numberList);
        System.out.println("Number List: " + numberList);

        List<Object> objectList = new ArrayList<>();
        CollectionUtils.addIntegers(objectList);
        System.out.println("Object List: " + objectList);

        // List<Double> doubleList = new ArrayList<>();
        // CollectionUtils.addIntegers(doubleList); // 编译错误，因为 Double 不是 Integer 的父类

    }
}
