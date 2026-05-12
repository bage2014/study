package com.bage.study.ai.best.practice.dev.flow.service;

import com.bage.study.ai.best.practice.dev.flow.dto.request.LoginRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.request.RegisterRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.request.UserProfileRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.LoginResponse;
import com.bage.study.ai.best.practice.dev.flow.dto.response.UserDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.User;

public interface UserService {

    LoginResponse login(LoginRequest request);

    UserDTO register(RegisterRequest request);

    UserDTO getUserById(Long id);

    UserDTO updateProfile(Long userId, UserProfileRequest request);

    User getCurrentUser(Long userId);
}