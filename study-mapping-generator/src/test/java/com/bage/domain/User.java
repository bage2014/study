package com.bage.domain;

import java.util.Date;

public class User{

    private int id;
    private Integer code;
    private String name;
    private Date birthday;
    private Sex sex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", code=" + code +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", sex=" + sex +
                '}';
    }
}
