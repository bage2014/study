package com.bage.study.springboot.aop;

public class CustomErrorType {
    int value;
    String message;

    public CustomErrorType(int value, String message) {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
