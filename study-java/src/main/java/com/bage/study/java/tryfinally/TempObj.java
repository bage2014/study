package com.bage.study.java.tryfinally;

public class TempObj {
    private int value;

    public TempObj(int n){
        value = n;
    }
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "value:" + value;
    }
}
