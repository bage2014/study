package com.bage.study.ai.best.practice.dev.flow.service;

import com.bage.study.ai.best.practice.dev.flow.dto.LoginRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.LoginResponse;
import com.bage.study.ai.best.practice.dev.flow.dto.RegisterRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.UpdateUserRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.UserDTO;

import java.util.List;

public interface UserService {
    LoginResponse login(LoginRequest loginRequest);
    UserDTO register(RegisterRequest registerRequest);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UpdateUserRequest updateUserRequest);
    void deleteUser(Long id);
}