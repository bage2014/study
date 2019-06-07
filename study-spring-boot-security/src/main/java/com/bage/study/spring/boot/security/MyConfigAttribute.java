package com.bage.study.spring.boot.security;

import org.springframework.security.access.ConfigAttribute;

public class MyConfigAttribute implements ConfigAttribute {

    private String attribute;

    public MyConfigAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getAttribute() {
        return attribute;
    }

    @Override
    public String toString() {
        return "MyConfigAttribute{" +
                "attribute='" + attribute + '\'' +
                '}';
    }
}
