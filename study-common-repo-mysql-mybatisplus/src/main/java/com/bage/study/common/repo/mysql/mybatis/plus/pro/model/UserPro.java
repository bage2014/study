package com.bage.study.common.repo.mysql.mybatis.plus.pro.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserPro {

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
