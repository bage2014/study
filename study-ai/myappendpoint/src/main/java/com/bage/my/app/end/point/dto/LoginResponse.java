package com.bage.my.app.end.point.dto;

import com.bage.my.app.end.point.entity.User;
import com.bage.my.app.end.point.entity.UserToken;

import lombok.Data;

@Data
public class LoginResponse {

    private User user;
    private UserToken userToken;
    
}