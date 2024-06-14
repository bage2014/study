package com.bage.study.java.fanxing;

/**
 * https://juejin.cn/post/7379806770363367451
 * @param <T>
 */
public class Box<T extends Number> {
    private T value;

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
