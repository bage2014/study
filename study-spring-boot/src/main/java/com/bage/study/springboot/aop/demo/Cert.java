package com.bage.study.springboot.aop.demo;

import com.bage.study.springboot.aop.annotation.WithField;

public class Cert {

    private String code;
    @WithField
    private String desc;

    private SubSert subSert;

    public Cert(String code, String desc, SubSert subSert) {
        this.code = code;
        this.desc = desc;
        this.subSert = subSert;
    }

    public SubSert getSubSert() {
        return subSert;
    }

    public void setSubSert(SubSert subSert) {
        this.subSert = subSert;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Cert{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
