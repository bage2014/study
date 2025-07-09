package com.bage.my.app.end.point.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    // 新增字段
    private String email;
    private String gender;
    private LocalDate birthDate;
    private String avatarUrl;
}