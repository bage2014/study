package com.bage.study.common.repo.mysql.mybatis.plus.pro.model;

public enum SexEnum {
    unknown("unknown"),
    Male("Male"),
    Female("Female");
    ;
    private String code;

    SexEnum(String code) {
        this.code = code;
    }
}
