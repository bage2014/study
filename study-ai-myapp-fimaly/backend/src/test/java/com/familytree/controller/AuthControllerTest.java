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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        ResponseEntity<ApiResponse<Map<String, Object>>> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertEquals(token, response.getBody().getData().get("token"));
        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    void login_Failure() {
        when(authService.login(any(LoginRequest.class))).thenThrow(new RuntimeException("Invalid credentials"));

        ResponseEntity<ApiResponse<Map<String, Object>>> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("Invalid credentials", response.getBody().getMessage());
    }

    @Test
    void register_Success() {
        User user = new User();
        user.setId(1L);
        user.setEmail("newuser@example.com");
        user.setNickname("新用户");
        when(authService.register(any(RegisterRequest.class))).thenReturn(user);

        ResponseEntity<ApiResponse<User>> response = authController.register(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertEquals("newuser@example.com", response.getBody().getData().getEmail());
        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void register_Failure_EmailExists() {
        when(authService.register(any(RegisterRequest.class))).thenThrow(new RuntimeException("Email already exists"));

        ResponseEntity<ApiResponse<User>> response = authController.register(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("Email already exists", response.getBody().getMessage());
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

        ResponseEntity<ApiResponse<User>> response = authController.register(requestWithoutUsername);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
    }
}
