package com.bage.my.app.end.point.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}