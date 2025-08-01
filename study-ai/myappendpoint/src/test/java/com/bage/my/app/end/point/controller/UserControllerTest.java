package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.dto.LoginRequest;
import com.bage.my.app.end.point.dto.RegisterRequest;
import com.bage.my.app.end.point.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bage.my.app.end.point.util.JsonUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        userRepository.deleteAll();
    }

    @Test
    void testLoginSuccess() throws Exception {
        // 先注册用户
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("testuser");
        registerRequest.setPassword("password123");
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.getGson().toJson(registerRequest)));

        // 登录测试
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/login")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.getGson().toJson(loginRequest)))
                .andExpect(status().isOk());
//                .andExpect(content().string("登录成功".contains()));
    }

    @Test
    void testLoginFailure() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistent");
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/login")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.getGson().toJson(loginRequest)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message").value("用户不存在"));
    }

    @Test
    void testRegister() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPassword("newpassword");
        registerRequest.setEmail("test@example.com");

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.getGson().toJson(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("注册成功"));
    }

    @Test
    void testGetCaptcha() throws Exception {
        mockMvc.perform(get("/captcha").session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));
    }
}