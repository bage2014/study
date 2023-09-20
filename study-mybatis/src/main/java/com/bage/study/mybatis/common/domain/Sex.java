package com.bage.study.mybatis.common.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum Sex {

    Unknown("Unknown","未知"),
    Famale("Famale","女"),
    Male("Male","男");

    private String code;
    private String desc;

    private static Map<String,Sex> map = new HashMap<>();
    static {
        Sex[] sexes = Sex.values();
        for (int i = 0; i < sexes.length; i++) {
            map.put(sexes[i].getCode(),sexes[i]);
        }
    }

    public static Sex of(String code){
        Sex sex = map.get(code);
        if(Objects.isNull(sex)){
            sex = Unknown;
        }
        return sex;
    }

    Sex(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "Sex{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
