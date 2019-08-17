package com.bage.study.springboot.aop.demo;

import com.bage.study.springboot.aop.annotation.crypt.WithField;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class User {

    private String id;
    @WithField
    private String name;
    private String sex;
    private List<Cert> certList;
    private Map<String, Cert> certMap;
    private Set<Cert> certSet;

    public User(){

    }
    public User(String id, String name, String sex, List<Cert> certList, Map<String, Cert> certMap, Set<Cert> certSet) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.certList = certList;
        this.certMap = certMap;
        this.certSet = certSet;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", certList=" + certList +
                ", certMap=" + certMap +
                ", certSet=" + certSet +
                '}';
    }
}
