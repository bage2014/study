package com.bage.jdk9;

public interface Calculator {
    // 公共的默认方法
    int add(int a, int b);

    default int subtract(int a, int b) {
        return add(a, negate(b));
    }

    default int multiply(int a, int b) {
        return a * b;
    }

    // 私有接口方法
    private int negate(int number) {
        return -number;
    }
}
