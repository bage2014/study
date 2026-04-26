package com.familytree.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MemberUpdateRequest {

    @Size(min = 1, max = 50, message = "成员姓名长度必须在1-50个字符之间")
    private String name;

    @Pattern(regexp = "^(男|女|MALE|FEMALE|OTHER)?$", message = "性别格式不正确")
    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deathDate;

    private String photo;

    @Size(max = 2000, message = "详细信息长度不能超过2000个字符")
    private String details;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;
}