package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.dto.LoginRequest;
import com.familytree.dto.RegisterRequest;
import com.familytree.model.User;
import com.familytree.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    private AuthService authService;
    
    @InjectMocks
    private AuthController authController;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testLoginSuccess() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        
        String token = "test-token";
        when(authService.login(request)).thenReturn(token);
        
        // Act
        ApiResponse<Map<String, Object>> response = authController.login(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(token, response.getData().get("token"));
        verify(authService, times(1)).login(request);
    }
    
    @Test
    void testLoginFailure() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        
        String errorMessage = "Invalid email or password";
        when(authService.login(request)).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Map<String, Object>> response = authController.login(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(authService, times(1)).login(request);
    }
    
    @Test
    void testRegisterSuccess() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setNickname("Test User");
        
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setNickname("Test User");
        
        when(authService.register(request)).thenReturn(user);
        
        // Act
        ApiResponse<User> response = authController.register(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(user, response.getData());
        verify(authService, times(1)).register(request);
    }
    
    @Test
    void testRegisterFailure() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        
        String errorMessage = "Email already exists";
        when(authService.register(request)).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<User> response = authController.register(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(authService, times(1)).register(request);
    }
    
    @Test
    void testGetCurrentUserSuccess() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        
        // 模拟 SecurityContext 和 Authentication
        Authentication authentication = mock(Authentication.class);
        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                userId.toString(), "password", java.util.Collections.emptyList());
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        
        when(authService.getUserById(userId)).thenReturn(user);
        
        // Act
        ApiResponse<User> response = authController.getCurrentUser();
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(user, response.getData());
        verify(authService, times(1)).getUserById(userId);
        
        // 清理 SecurityContext
        SecurityContextHolder.clearContext();
    }
    
    @Test
    void testGetCurrentUserFailure() {
        // Arrange
        Long userId = 1L;
        String errorMessage = "User not found";
        
        // 模拟 SecurityContext 和 Authentication
        Authentication authentication = mock(Authentication.class);
        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                userId.toString(), "password", java.util.Collections.emptyList());
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        
        when(authService.getUserById(userId)).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<User> response = authController.getCurrentUser();
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(authService, times(1)).getUserById(userId);
        
        // 清理 SecurityContext
        SecurityContextHolder.clearContext();
    }
    
    @Test
    void testGetCurrentUserUnauthenticated() {
        // Arrange - 未设置 SecurityContext，模拟未登录状态
        
        // Act
        ApiResponse<User> response = authController.getCurrentUser();
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals("User not authenticated", response.getMessage());
        assertNull(response.getData());
        verify(authService, times(0)).getUserById(anyLong());
    }
    
    @Test
    void testGetCurrentUserInvalidPrincipal() {
        // Arrange
        // 模拟 SecurityContext 和 Authentication，但 principal 不是 UserDetails
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("invalid-principal");
        
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        
        // Act
        ApiResponse<User> response = authController.getCurrentUser();
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals("Invalid principal", response.getMessage());
        assertNull(response.getData());
        verify(authService, times(0)).getUserById(anyLong());
        
        // 清理 SecurityContext
        SecurityContextHolder.clearContext();
    }
}