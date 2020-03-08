package com.bage.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComplexDomain extends BaseDomain{

    // 基本类型
    private int id;
    private Integer age;

    /**
     * 自定义对象
     */
    private User user;

    // 常用类
    private String name;
    private Date birthday;

    // 枚举
    private Sex sex;

    // 泛型
    private List<Seller> list;
    private Map<String, MapAttr> map;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<Seller> getList() {
        return list;
    }

    public void setList(List<Seller> list) {
        this.list = list;
    }

    public Map<String, MapAttr> getMap() {
        return map;
    }

    public void setMap(Map<String, MapAttr> map) {
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
                ", user=" + user +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", sex=" + sex +
                ", list=" + list +
                ", map=" + map +
                ", set=" + set +
                '}';
    }
}
