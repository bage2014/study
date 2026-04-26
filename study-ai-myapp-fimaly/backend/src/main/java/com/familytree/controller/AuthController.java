package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.dto.LoginRequest;
import com.familytree.dto.RegisterRequest;
import com.familytree.model.User;
import com.familytree.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "认证", description = "用户认证和注册接口")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录", description = "使用邮箱和密码登录系统，返回JWT令牌")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "登录成功",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "参数错误"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "认证失败")
    })
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        log.info("[用户登录] email={}", request.getEmail());
        try {
            String token = authService.login(request);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            return ApiResponse.success(response, "登录成功");
        } catch (RuntimeException e) {
            log.warn("[用户登录] 失败: {}", e.getMessage());
            return ApiResponse.error("A001", e.getMessage());
        }
    }

    @Operation(summary = "用户注册", description = "注册新用户，需要提供邮箱、密码和昵称")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "注册成功",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "参数错误或邮箱已存在")
    })
    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody RegisterRequest request) {
        log.info("[用户注册] email={}", request.getEmail());
        try {
            User user = authService.register(request);
            return ApiResponse.success(user, "注册成功");
        } catch (RuntimeException e) {
            log.warn("[用户注册] 失败: {}", e.getMessage());
            return ApiResponse.error("A002", e.getMessage());
        }
    }

    @Operation(summary = "获取当前用户", description = "从JWT令牌中解析用户信息")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "获取成功",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "用户未认证")
    })
    @GetMapping("/me")
    public ApiResponse<User> getCurrentUser() {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("[获取当前用户] 用户未认证");
            return ApiResponse.unauthorized("用户未认证");
        }

        Object principal = authentication.getPrincipal();
        log.debug("[获取当前用户] principal={}", principal);

        if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) principal;
            String username = userDetails.getUsername();
            Long userId = Long.parseLong(username);
            User user = authService.getUserById(userId);
            return ApiResponse.success(user);
        } else {
            log.warn("[获取当前用户] 无效的principal类型: {}", principal.getClass());
            return ApiResponse.error("无效的用户信息");
        }
    }
}