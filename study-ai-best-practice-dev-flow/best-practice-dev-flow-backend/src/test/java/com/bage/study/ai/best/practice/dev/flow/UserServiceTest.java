package com.bage.study.ai.best.practice.dev.flow;

import com.bage.study.ai.best.practice.dev.flow.dto.request.LoginRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.request.RegisterRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.LoginResponse;
import com.bage.study.ai.best.practice.dev.flow.dto.response.UserDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.User;
import com.bage.study.ai.best.practice.dev.flow.exception.AuthenticationException;
import com.bage.study.ai.best.practice.dev.flow.exception.BusinessException;
import com.bage.study.ai.best.practice.dev.flow.exception.ResourceNotFoundException;
import com.bage.study.ai.best.practice.dev.flow.repository.UserRepository;
import com.bage.study.ai.best.practice.dev.flow.service.impl.UserServiceImpl;
import com.bage.study.ai.best.practice.dev.flow.util.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser(Long id, String username, String email, String password) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    @Test
    void UT_AUTH_001_Login_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        User user = createUser(1L, "admin", "admin@example.com", "encodedPassword");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("admin123", "encodedPassword")).thenReturn(true);
        when(jwtTokenProvider.createToken(1L, "admin")).thenReturn("test-token");

        LoginResponse result = userService.login(request);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals("test-token", result.getToken());
        assertNotNull(result.getUser());
        assertEquals("admin", result.getUser().getUsername());
    }

    @Test
    void UT_AUTH_002_Login_WrongPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrong");

        User user = createUser(1L, "admin", "admin@example.com", "encodedPassword");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> userService.login(request));
    }

    @Test
    void UT_AUTH_003_Login_UserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexist");
        request.setPassword("password");

        when(userRepository.findByUsername("nonexist")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> userService.login(request));
    }

    @Test
    void UT_AUTH_004_Login_EmptyUsername() {
        LoginRequest request = new LoginRequest();
        request.setUsername("");
        request.setPassword("password");

        assertThrows(Exception.class, () -> userService.login(request));
    }

    @Test
    void UT_AUTH_005_Login_EmptyPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("");

        assertThrows(Exception.class, () -> userService.login(request));
    }

    @Test
    void UT_AUTH_006_Register_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("new@example.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        UserDTO result = userService.register(request);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void UT_AUTH_007_Register_UsernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("admin");
        request.setEmail("new@example.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername("admin")).thenReturn(true);

        assertThrows(BusinessException.class, () -> userService.register(request));
    }

    @Test
    void UT_AUTH_008_Register_EmailExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("admin@example.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("admin@example.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> userService.register(request));
    }

    @Test
    void UT_AUTH_009_GetCurrentUser_Success() {
        User user = createUser(1L, "admin", "admin@example.com", "encodedPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getCurrentUser(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("admin", result.getUsername());
    }

    @Test
    void UT_AUTH_010_GetCurrentUser_InvalidToken() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getCurrentUser(999L));
    }

    @Test
    void UT_AUTH_011_GetUserById_Success() {
        User user = createUser(1L, "admin", "admin@example.com", "encodedPassword");
        user.setNickname("管理员");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("admin", result.getUsername());
    }

    @Test
    void UT_AUTH_012_GetUserById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(999L));
    }
}
