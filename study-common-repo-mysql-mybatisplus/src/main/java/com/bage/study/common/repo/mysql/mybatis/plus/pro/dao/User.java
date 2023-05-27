package com.bage.study.common.repo.mysql.mybatis.plus.pro.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.sql.Date;

/**
 * https://baomidou.com/pages/223848/#tablename
 */
@TableName("user_pro")
public class User {
    private Long id;
    @TableField("first_name")
    private String firstName;
    @TableField("last_name")
    private String lastName;
    private String sex;
    private Date birthday;
    private String address;
    private String email;
    private String phone;
    private String remark;
}