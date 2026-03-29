package com.familytree.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String phone;
    private String password;
    private String nickname;
}