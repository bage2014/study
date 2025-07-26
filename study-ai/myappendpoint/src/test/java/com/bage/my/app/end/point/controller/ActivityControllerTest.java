package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.Activity;
import com.bage.my.app.end.point.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bage.my.app.end.point.util.JsonUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ActivityRepository activityRepository;

    @BeforeEach
    void setUp() {
        activityRepository.deleteAll();
    }

    @Test
    void testGetActivities() throws Exception {
        // 添加测试数据
        Activity activity = new Activity("2024-01-01", "测试活动", "测试用户");
        activityRepository.save(activity);

        mockMvc.perform(get("/activities")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].description").value("测试活动"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testAddActivity() throws Exception {
        Activity activity = new Activity("2024-01-02", "新增活动", "测试用户");

        mockMvc.perform(post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.getGson().toJson(activity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("新增活动"));
    }

    @Test
    void testAddMockActivity() throws Exception {
        mockMvc.perform(post("/activities/mock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").exists());
    }
}