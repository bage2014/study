package com.bage.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度必须在2到50个字符之间")
    private String username;

    @Size(min = 6, max = 100, message = "密码长度必须在6到100个字符之间")
    private String password;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Size(max = 20, message = "手机号长度不能超过20个字符")
    private String phone;

    @Size(max = 200, message = "地址长度不能超过200个字符")
    private String address;

    private Integer age;

    @Size(max = 10, message = "性别长度不能超过10个字符")
    private String gender;
}