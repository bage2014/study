package com.bage.study.best.practice.repo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * https://baomidou.com/pages/223848/#tablename
 */
@TableName("tb_user")
@Data
public class UserEntity {
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