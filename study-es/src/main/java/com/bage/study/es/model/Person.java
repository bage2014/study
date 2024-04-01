package com.bage.study.es.model;

import java.util.Date;

public class Person {

    private Long id;
    private Integer age;

    private String fullName;

    private Date dateOfBirth;

    public Person() {
    }

    public Person(Long id, Integer age, String fullName, Date dateOfBirth) {
        this.id = id;
        this.age = age;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Person [age=" + age + ", fullName=" + fullName + ", dateOfBirth=" + dateOfBirth + "]";
    }

}