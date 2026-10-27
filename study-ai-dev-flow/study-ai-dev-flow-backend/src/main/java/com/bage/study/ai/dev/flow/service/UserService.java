package com.bage.study.ai.dev.flow.service;

import com.bage.study.ai.dev.flow.dto.request.UserCreateRequest;
import com.bage.study.ai.dev.flow.dto.request.UserUpdateRequest;
import com.bage.study.ai.dev.flow.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse create(UserCreateRequest request);

    UserResponse update(Long id, UserUpdateRequest request);

    void delete(Long id);
}
