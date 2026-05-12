package com.bage.study.ai.best.practice.dev.flow;

import com.bage.study.ai.best.practice.dev.flow.dto.LoginRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.LoginResponse;
import com.bage.study.ai.best.practice.dev.flow.dto.RegisterRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.UserDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.User;
import com.bage.study.ai.best.practice.dev.flow.repository.UserRepository;
import com.bage.study.ai.best.practice.dev.flow.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testRegisterUser_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@test.com");
        request.setPassword("123456");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        UserDTO result = userService.register(request);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@test.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_UsernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existing");
        request.setEmail("test@test.com");
        request.setPassword("123456");

        when(userRepository.existsByUsername("existing")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("existing@test.com");
        request.setPassword("123456");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_UserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("password");

        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.login(request));
    }

    @Test
    void testGetUserById_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setEmail("admin@test.com");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("admin", result.getUsername());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(999L));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(999L));
    }
}