package com.bage.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComplexDomain {

    // 基本类型
    private int id;
    private Integer age;

    // 常用类
    private String name;
    private Date birthday;

    // 枚举
    private Sex sex;

    // 泛型
    private List<String> list;
    private Map<String, String> map;
    private Set<String> set;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    @Override
    public String toString() {
        return "ComplexDomain{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", sex=" + sex +
                ", list=" + list +
                ", map=" + map +
                ", set=" + set +
                '}';
    }
}
