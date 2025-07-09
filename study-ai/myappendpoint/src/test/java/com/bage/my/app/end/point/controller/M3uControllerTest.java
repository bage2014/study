package com.bage.my.app.end.point.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class M3uControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testParseM3uWithoutKeyword() throws Exception {
        mockMvc.perform(get("/m3u/query")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testParseM3uWithKeyword() throws Exception {
        mockMvc.perform(get("/m3u/query")
                .param("page", "0")
                .param("size", "10")
                .param("keyword", "测试"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}