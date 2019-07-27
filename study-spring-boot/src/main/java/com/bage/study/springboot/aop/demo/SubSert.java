package com.bage.study.springboot.aop.demo;

import com.bage.study.springboot.aop.annotation.WithField;

public class SubSert {

    @WithField
    private String id;

    private String name;

    public SubSert(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SubSert{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
