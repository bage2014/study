package com.bage.study.best.practice.model;

public enum SexEnum {
    unknown("unknown"),
    Male("Male"),
    Female("Female");
    ;
    private String code;

    SexEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
