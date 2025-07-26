package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.Trajectory;
import com.bage.my.app.end.point.repository.TrajectoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bage.my.app.end.point.util.JsonUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class MapTrajectoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrajectoryRepository trajectoryRepository;

    @BeforeEach
    void setUp() {
        trajectoryRepository.deleteAll();
    }

    @Test
    void testGetTrajectoryData() throws Exception {
        // 添加测试数据
        Trajectory trajectory = new Trajectory(31.2, 121.4, LocalDateTime.now(), "测试地址");
        trajectoryRepository.save(trajectory);

        mockMvc.perform(get("/trajectorys/query")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].address").value("测试地址"));
    }

    @Test
    void testSaveTrajectory() throws Exception {
        Trajectory trajectory = new Trajectory(31.2, 121.4, null, "保存测试地址");

        mockMvc.perform(post("/trajectorys/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.getGson().toJson(trajectory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.address").value("保存测试地址"));
    }

    @Test
    void testGetTrajectoryDataWithTimeFilter() throws Exception {
        mockMvc.perform(get("/trajectorys/query")
                .param("startTime", LocalDateTime.now().minusDays(1).toString())
                .param("endTime", LocalDateTime.now().toString()))
                .andExpect(status().isOk());
    }
}