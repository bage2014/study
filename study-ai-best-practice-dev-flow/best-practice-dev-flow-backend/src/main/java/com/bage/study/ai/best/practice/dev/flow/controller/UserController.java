package com.bage.study.ai.best.practice.dev.flow.controller;

import com.bage.study.ai.best.practice.dev.flow.dto.RestResult;
import com.bage.study.ai.best.practice.dev.flow.dto.request.LoginRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.request.RegisterRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.request.UserProfileRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.LoginResponse;
import com.bage.study.ai.best.practice.dev.flow.dto.response.UserDTO;
import com.bage.study.ai.best.practice.dev.flow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<RestResult<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(RestResult.success(response));
    }

    @PostMapping("/register")
    public ResponseEntity<RestResult<UserDTO>> register(@Valid @RequestBody RegisterRequest request) {
        UserDTO user = userService.register(request);
        return ResponseEntity.ok(RestResult.success("注册成功", user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResult<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(RestResult.success(user));
    }

    @PutMapping("/profile")
    public ResponseEntity<RestResult<UserDTO>> updateProfile(
            @RequestAttribute("userId") Long userId,
            @RequestBody UserProfileRequest request) {
        UserDTO user = userService.updateProfile(userId, request);
        return ResponseEntity.ok(RestResult.success("更新成功", user));
    }

    @GetMapping("/profile")
    public ResponseEntity<RestResult<UserDTO>> getProfile(@RequestAttribute("userId") Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(RestResult.success(user));
    }
}