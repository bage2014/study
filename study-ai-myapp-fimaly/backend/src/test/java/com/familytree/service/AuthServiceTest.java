package com.familytree.service;

import com.familytree.dto.LoginRequest;
import com.familytree.dto.RegisterRequest;
import com.familytree.exception.BusinessException;
import com.familytree.exception.ErrorCode;
import com.familytree.model.User;
import com.familytree.repository.UserRepository;
import com.familytree.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
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

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setCreatedAt(new Date());
    }

    @Nested
    @DisplayName("登录测试")
    class LoginTests {

        @Test
        @DisplayName("登录成功")
        void testLogin_Success() {
            LoginRequest request = new LoginRequest();
            request.setEmail("test@example.com");
            request.setPassword("password123");

            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
            when(jwtUtils.generateToken(1L)).thenReturn("jwt-token");

            String token = authService.login(request);

            assertNotNull(token);
            assertEquals("jwt-token", token);
            verify(userRepository, times(1)).findByEmail("test@example.com");
            verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
            verify(jwtUtils, times(1)).generateToken(1L);
        }

        @Test
        @DisplayName("用户不存在时登录失败")
        void testLogin_UserNotFound() {
            LoginRequest request = new LoginRequest();
            request.setEmail("nonexistent@example.com");
            request.setPassword("password123");

            when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(BusinessException.class, () -> {
                authService.login(request);
            });

            assertEquals(ErrorCode.INVALID_CREDENTIALS, exception.getErrorCode());
            verify(passwordEncoder, never()).matches(anyString(), anyString());
            verify(jwtUtils, never()).generateToken(any());
        }

        @Test
        @DisplayName("密码错误时登录失败")
        void testLogin_WrongPassword() {
            LoginRequest request = new LoginRequest();
            request.setEmail("test@example.com");
            request.setPassword("wrongPassword");

            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

            BusinessException exception = assertThrows(BusinessException.class, () -> {
                authService.login(request);
            });

            assertEquals(ErrorCode.INVALID_CREDENTIALS, exception.getErrorCode());
            verify(jwtUtils, never()).generateToken(any());
        }
    }

    @Nested
    @DisplayName("注册测试")
    class RegisterTests {

        @Test
        @DisplayName("注册成功")
        void testRegister_Success() {
            RegisterRequest request = new RegisterRequest();
            request.setEmail("new@example.com");
            request.setUsername("newuser");
            request.setPassword("password123");
            request.setNickname("新用户");

            when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(2L);
                return user;
            });

            User result = authService.register(request);

            assertNotNull(result);
            assertEquals("new@example.com", result.getEmail());
            assertEquals("newuser", result.getUsername());
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("邮箱已存在时注册失败")
        void testRegister_EmailAlreadyExists() {
            RegisterRequest request = new RegisterRequest();
            request.setEmail("test@example.com");
            request.setPassword("password123");

            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

            BusinessException exception = assertThrows(BusinessException.class, () -> {
                authService.register(request);
            });

            assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS, exception.getErrorCode());
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("手机号已存在时注册失败")
        void testRegister_PhoneAlreadyExists() {
            RegisterRequest request = new RegisterRequest();
            request.setEmail("new@example.com");
            request.setPhone("13800138000");
            request.setPassword("password123");

            when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
            when(userRepository.findByPhone("13800138000")).thenReturn(Optional.of(testUser));

            BusinessException exception = assertThrows(BusinessException.class, () -> {
                authService.register(request);
            });

            assertEquals(ErrorCode.PHONE_ALREADY_EXISTS, exception.getErrorCode());
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("注册时默认使用邮箱作为用户名")
        void testRegister_DefaultUsername() {
            RegisterRequest request = new RegisterRequest();
            request.setEmail("new@example.com");
            request.setPassword("password123");

            when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(2L);
                return user;
            });

            User result = authService.register(request);

            assertNotNull(result);
            assertEquals("new@example.com", result.getUsername());
        }
    }

    @Nested
    @DisplayName("获取用户测试")
    class GetUserTests {

        @Test
        @DisplayName("成功获取用户")
        void testGetUserById_Success() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            User result = authService.getUserById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("test@example.com", result.getEmail());
            verify(userRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("用户不存在时抛出异常")
        void testGetUserById_NotFound() {
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(BusinessException.class, () -> {
                authService.getUserById(999L);
            });

            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        }
    }
}