package com.bage.study.dubbo.spring.boot.api;

import java.io.Serializable;

public class HelloParam implements Serializable {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return "HelloParam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
