package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.dto.UserProfileResponse;
import com.familytree.dto.UserProfileUpdateRequest;
import com.familytree.model.User;
import com.familytree.service.AuthService;
import com.familytree.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "个人信息维护", description = "用户个人资料查看和编辑功能")
public class ProfileController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping
    @Operation(summary = "获取当前用户的个人信息")
    public ApiResponse<UserProfileResponse> getProfile(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        log.info("[获取个人信息] userId={}", userId);
        try {
            User user = authService.getUserById(userId);
            UserProfileResponse response = UserProfileResponse.fromUser(user);
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("[获取个人信息] 失败", e);
            return ApiResponse.error("获取个人信息失败: " + e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "更新个人信息")
    public ApiResponse<UserProfileResponse> updateProfile(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Valid @RequestBody UserProfileUpdateRequest request) {
        log.info("[更新个人信息] userId={}, request={}", userId, request);
        try {
            User user = authService.getUserById(userId);

            // 更新昵称
            if (request.getNickname() != null) {
                user.setNickname(request.getNickname());
            }

            // 更新邮箱（需要检查是否已被使用）
            if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
                userRepository.findByEmail(request.getEmail())
                        .ifPresent(existingUser -> {
                            throw new RuntimeException("该邮箱已被注册");
                        });
                user.setEmail(request.getEmail());
            }

            // 更新手机号（需要检查是否已被使用）
            if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
                userRepository.findByPhone(request.getPhone())
                        .ifPresent(existingUser -> {
                            throw new RuntimeException("该手机号已被注册");
                        });
                user.setPhone(request.getPhone());
            }

            // 更新头像
            if (request.getAvatar() != null) {
                user.setAvatar(request.getAvatar());
            }

            User updatedUser = userRepository.save(user);
            UserProfileResponse response = UserProfileResponse.fromUser(updatedUser);
            return ApiResponse.success(response, "个人信息更新成功");
        } catch (RuntimeException e) {
            log.warn("[更新个人信息] 失败: {}", e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }
}