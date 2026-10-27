package com.bage.study.ai.dev.flow.service.impl;

import com.bage.study.ai.dev.flow.dto.request.UserCreateRequest;
import com.bage.study.ai.dev.flow.dto.request.UserUpdateRequest;
import com.bage.study.ai.dev.flow.dto.response.UserResponse;
import com.bage.study.ai.dev.flow.entity.UserEntity;
import com.bage.study.ai.dev.flow.exception.BusinessException;
import com.bage.study.ai.dev.flow.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPhone("13800138000");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("findAll - 查询所有用户")
    class FindAll {

        @Test
        @DisplayName("应返回所有用户的 Response 列表")
        void shouldReturnAllUsers() {
            when(userRepository.findAll()).thenReturn(List.of(testUser));

            List<UserResponse> result = userService.findAll();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getUsername()).isEqualTo("testuser");
            assertThat(result.get(0).getEmail()).isEqualTo("test@example.com");
            verify(userRepository).findAll();
        }

        @Test
        @DisplayName("无数据时返回空列表")
        void shouldReturnEmptyList() {
            when(userRepository.findAll()).thenReturn(List.of());

            List<UserResponse> result = userService.findAll();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findById - 根据ID查询用户")
    class FindById {

        @Test
        @DisplayName("用户存在时返回 Response")
        void shouldReturnUserWhenExists() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            UserResponse result = userService.findById(1L);

            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getUsername()).isEqualTo("testuser");
        }

        @Test
        @DisplayName("用户不存在时抛出 BusinessException")
        void shouldThrowWhenNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.findById(99L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("用户不存在");
        }
    }

    @Nested
    @DisplayName("create - 创建用户")
    class Create {

        @Test
        @DisplayName("正常创建用户")
        void shouldCreateUser() {
            UserCreateRequest request = new UserCreateRequest();
            request.setUsername("newuser");
            request.setEmail("new@example.com");
            request.setPhone("13900139000");

            when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
            when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
                UserEntity saved = invocation.getArgument(0);
                saved.setId(2L);
                return saved;
            });

            UserResponse result = userService.create(request);

            assertThat(result.getId()).isEqualTo(2L);
            assertThat(result.getUsername()).isEqualTo("newuser");
            verify(userRepository).save(any(UserEntity.class));
        }

        @Test
        @DisplayName("邮箱重复时抛出异常")
        void shouldThrowWhenEmailDuplicate() {
            UserCreateRequest request = new UserCreateRequest();
            request.setUsername("newuser");
            request.setEmail("test@example.com");

            when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

            assertThatThrownBy(() -> userService.create(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("邮箱已存在");

            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("update - 更新用户")
    class Update {

        @Test
        @DisplayName("正常更新用户")
        void shouldUpdateUser() {
            UserUpdateRequest request = new UserUpdateRequest();
            request.setUsername("updated");

            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

            UserResponse result = userService.update(1L, request);

            assertThat(result.getUsername()).isEqualTo("updated");
        }

        @Test
        @DisplayName("用户不存在时抛出异常")
        void shouldThrowWhenNotFound() {
            UserUpdateRequest request = new UserUpdateRequest();
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.update(99L, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("用户不存在");
        }
    }

    @Nested
    @DisplayName("delete - 删除用户")
    class Delete {

        @Test
        @DisplayName("正常删除用户")
        void shouldDeleteUser() {
            when(userRepository.existsById(1L)).thenReturn(true);

            userService.delete(1L);

            verify(userRepository).deleteById(1L);
        }

        @Test
        @DisplayName("用户不存在时抛出异常")
        void shouldThrowWhenNotFound() {
            when(userRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> userService.delete(99L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("用户不存在");
        }
    }
}
