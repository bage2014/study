package com.familytree.controller;

import com.familytree.dto.LoginRequest;
import com.familytree.dto.RegisterRequest;
import com.familytree.model.User;
import com.familytree.service.AuthService;
import com.familytree.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.familytree.dto.ApiResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        registerRequest = new RegisterRequest();
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setNickname("新用户");
    }

    @Test
    void login_Success() {
        String token = "jwt-token-test";
        when(authService.login(any(LoginRequest.class))).thenReturn(token);

        ApiResponse<Map<String, Object>> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals(token, response.getData().get("token"));
        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    void login_Failure() {
        when(authService.login(any(LoginRequest.class))).thenThrow(new RuntimeException("Invalid credentials"));

        ApiResponse<Map<String, Object>> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(500, response.getCode());
        assertEquals("Invalid credentials", response.getMessage());
    }

    @Test
    void register_Success() {
        User user = new User();
        user.setId(1L);
        user.setEmail("newuser@example.com");
        user.setNickname("新用户");
        when(authService.register(any(RegisterRequest.class))).thenReturn(user);

        ApiResponse<User> response = authController.register(registerRequest);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("newuser@example.com", response.getData().getEmail());
        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void register_Failure_EmailExists() {
        when(authService.register(any(RegisterRequest.class))).thenThrow(new RuntimeException("Email already exists"));

        ApiResponse<User> response = authController.register(registerRequest);

        assertNotNull(response);
        assertEquals(500, response.getCode());
        assertEquals("Email already exists", response.getMessage());
    }

    @Test
    void register_Success_WithUsernameFallback() {
        RegisterRequest requestWithoutUsername = new RegisterRequest();
        requestWithoutUsername.setEmail("test@example.com");
        requestWithoutUsername.setPassword("password123");
        requestWithoutUsername.setNickname("Test User");

        User user = new User();
        user.setId(1L);
        user.setUsername("test@example.com");
        user.setEmail("test@example.com");
        user.setNickname("Test User");

        when(authService.register(any(RegisterRequest.class))).thenReturn(user);

        ApiResponse<User> response = authController.register(requestWithoutUsername);

        assertNotNull(response);
        assertEquals(200, response.getCode());
    }
}