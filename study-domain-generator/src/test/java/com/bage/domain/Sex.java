package com.bage.domain;

public enum Sex {

    Unknown(0, "未知"),
    Boy(1, "男"),
    Girl(2, "女");

    private int code;
    private String desc;

    private Sex(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Sex{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
