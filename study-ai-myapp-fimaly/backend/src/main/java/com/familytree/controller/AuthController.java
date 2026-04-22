package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.dto.LoginRequest;
import com.familytree.dto.RegisterRequest;
import com.familytree.model.User;
import com.familytree.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.serverError(e.getMessage());
        }
    }
    
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);
            return ApiResponse.success(user);
        } catch (Exception e) {
            return ApiResponse.serverError(e.getMessage());
        }
    }
    
    @GetMapping("/me")
    public ApiResponse<User> getCurrentUser() {
        try {
            // 从SecurityContext中获取用户ID
            org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                log.info("User not authenticated");
                return ApiResponse.error("User not authenticated");
            }
            
            Object principal = authentication.getPrincipal();
            log.info("Principal: {}", principal);
            log.info("Principal class: {}", principal.getClass());
            
            if (principal instanceof org.springframework.security.core.userdetails.User) {
                org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) principal;
                String username = userDetails.getUsername();
                log.info("Username: {}", username);
                Long userId = Long.parseLong(username);
                User user = authService.getUserById(userId);
                return ApiResponse.success(user);
            } else {
                log.info("Principal is not UserDetails: {}", principal);
                return ApiResponse.error("Invalid principal");
            }
        } catch (Exception e) {
            log.info("Error in getCurrentUser: {}", e.getMessage());
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }
}
