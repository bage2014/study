package com.bage.study.best.practice.model;

import java.util.HashMap;
import java.util.Map;

public enum SexEnum {
    unknown("unknown"),
    Male("Male"),
    Female("Female");;
    private String code;

    SexEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    private static Map<String, SexEnum> map = new HashMap<>();

    static {
        for (SexEnum value : SexEnum.values()) {
            map.put(value.getCode(), value);
        }
    }

    public static SexEnum valueOfCode(String code) {
        return map.get(code);
    }

}
