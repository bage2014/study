package com.familytree.service;

import com.familytree.dto.LoginRequest;
import com.familytree.dto.RegisterRequest;
import com.familytree.model.User;
import com.familytree.repository.UserRepository;
import com.familytree.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setUsername("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setNickname("Test User");

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
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtils.generateToken(1L)).thenReturn("jwt-token-test");

        String token = authService.login(loginRequest);

        assertEquals("jwt-token-test", token);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
        verify(jwtUtils, times(1)).generateToken(1L);
    }

    @Test
    void login_Failure_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void login_Failure_WrongPassword() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        verify(jwtUtils, never()).generateToken(anyLong());
    }

    @Test
    void register_Success() {
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2L);
            return user;
        });

        User result = authService.register(registerRequest);

        assertNotNull(result);
        assertEquals("newuser@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("新用户", result.getNickname());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_Failure_EmailExists() {
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.of(testUser));

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_WithUsernameFallback() {
        RegisterRequest requestWithoutUsername = new RegisterRequest();
        requestWithoutUsername.setEmail("test@example.com");
        requestWithoutUsername.setPassword("password123");
        requestWithoutUsername.setNickname("Test User");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        User result = authService.register(requestWithoutUsername);

        assertNotNull(result);
        assertEquals("test@example.com", result.getUsername());
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = authService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.getUserById(999L));
    }
}
