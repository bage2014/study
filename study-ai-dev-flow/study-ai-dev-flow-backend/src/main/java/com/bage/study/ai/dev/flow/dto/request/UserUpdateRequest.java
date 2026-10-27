package com.bage.study.ai.dev.flow.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateRequest {

    private String username;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String phone;
}
