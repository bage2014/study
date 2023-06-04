package com.bage.study.best.practice.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private SexEnum sex;
    private LocalDate birthday;
    private String address;
    private String email;
    private String phone;
    private String remark;

}
