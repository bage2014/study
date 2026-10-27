package com.bage.study.ai.dev.flow.controller;

import com.bage.study.ai.dev.flow.dto.request.UserCreateRequest;
import com.bage.study.ai.dev.flow.dto.response.UserResponse;
import com.bage.study.ai.dev.flow.exception.BusinessException;
import com.bage.study.ai.dev.flow.exception.GlobalExceptionHandler;
import com.bage.study.ai.dev.flow.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponse testResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        testResponse = new UserResponse();
        testResponse.setId(1L);
        testResponse.setUsername("testuser");
        testResponse.setEmail("test@example.com");
        testResponse.setPhone("13800138000");
        testResponse.setCreatedAt(LocalDateTime.now());
        testResponse.setUpdatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("GET /api/users")
    class FindAll {

        @Test
        @DisplayName("返回用户列表")
        void shouldReturnList() throws Exception {
            when(userService.findAll()).thenReturn(List.of(testResponse));

            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].username").value("testuser"))
                    .andExpect(jsonPath("$[0].email").value("test@example.com"));
        }
    }

    @Nested
    @DisplayName("GET /api/users/{id}")
    class FindById {

        @Test
        @DisplayName("返回指定用户")
        void shouldReturnUser() throws Exception {
            when(userService.findById(1L)).thenReturn(testResponse);

            mockMvc.perform(get("/api/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("testuser"));
        }

        @Test
        @DisplayName("用户不存在返回 400")
        void shouldReturn400WhenNotFound() throws Exception {
            when(userService.findById(99L)).thenThrow(new BusinessException("NOT_FOUND", "用户不存在"));

            mockMvc.perform(get("/api/users/99"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("POST /api/users")
    class Create {

        @Test
        @DisplayName("创建成功返回 201")
        void shouldCreate() throws Exception {
            when(userService.create(any())).thenReturn(testResponse);

            UserCreateRequest request = new UserCreateRequest();
            request.setUsername("testuser");
            request.setEmail("test@example.com");

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username").value("testuser"));
        }

        @Test
        @DisplayName("参数校验失败返回 400")
        void shouldReturn400WhenInvalid() throws Exception {
            UserCreateRequest request = new UserCreateRequest();
            // username 和 email 为空，触发 @NotBlank

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).create(any());
        }
    }

    @Nested
    @DisplayName("POST /api/users/{id}/update")
    class Update {

        @Test
        @DisplayName("更新成功返回 200")
        void shouldUpdate() throws Exception {
            when(userService.update(eq(1L), any())).thenReturn(testResponse);

            mockMvc.perform(post("/api/users/1/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"updated\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("testuser"));
        }
    }

    @Nested
    @DisplayName("POST /api/users/{id}/delete")
    class Delete {

        @Test
        @DisplayName("删除成功返回 204")
        void shouldDelete() throws Exception {
            doNothing().when(userService).delete(1L);

            mockMvc.perform(post("/api/users/1/delete"))
                    .andExpect(status().isNoContent());

            verify(userService).delete(1L);
        }
    }
}
