package com.bage.study.ai.best.practice.exception.analysis.controller;

import com.bage.study.ai.best.practice.exception.analysis.dto.request.AnalysisRequest;
import com.bage.study.ai.best.practice.exception.analysis.dto.response.AnalysisResponse;
import com.bage.study.ai.best.practice.exception.analysis.service.AnalysisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AnalysisControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AnalysisService analysisService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        AnalysisController controller = new AnalysisController(analysisService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @DisplayName("健康检查接口 - 应该返回UP状态")
    void health_ShouldReturnUpStatus() throws Exception {
        mockMvc.perform(get("/api/analysis/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("exception-analysis"));
    }

    @Test
    @DisplayName("分析接口 - 有效请求应该返回分析结果")
    void analyze_ValidRequest_ShouldReturnAnalysisResponse() throws Exception {
        AnalysisRequest request = new AnalysisRequest();
        request.setAppId("app-001");
        request.setAlarmDescription("用户登录失败率突然升高");
        request.setAlarmUrl("https://monitor.example.com/alarm/123");
        request.setAlarmTime(LocalDateTime.of(2024, 1, 15, 10, 30));

        AnalysisResponse mockResponse = createMockAnalysisResponse();

        when(analysisService.analyze(any(AnalysisRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/analysis/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.analysisId").value("ANALYSIS-1234567890"))
                .andExpect(jsonPath("$.appId").value("app-001"))
                .andExpect(jsonPath("$.alarmDescription").value("用户登录失败率突然升高"))
                .andExpect(jsonPath("$.rootCause.type").value("高错误率"))
                .andExpect(jsonPath("$.rootCause.confidence").value("85%"))
                .andExpect(jsonPath("$.evidences").isArray())
                .andExpect(jsonPath("$.evidences[0].source").value("请求量监控"))
                .andExpect(jsonPath("$.suggestions").isArray())
                .andExpect(jsonPath("$.suggestions[0]").value("检查服务日志，定位具体错误类型"));
    }

    @Test
    @DisplayName("分析接口 - 缺少appId应该返回400错误")
    void analyze_MissingAppId_ShouldReturnBadRequest() throws Exception {
        AnalysisRequest request = new AnalysisRequest();
        request.setAlarmDescription("用户登录失败率突然升高");

        mockMvc.perform(post("/api/analysis/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("分析接口 - 缺少告警描述应该返回400错误")
    void analyze_MissingAlarmDescription_ShouldReturnBadRequest() throws Exception {
        AnalysisRequest request = new AnalysisRequest();
        request.setAppId("app-001");

        mockMvc.perform(post("/api/analysis/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("分析接口 - 空请求体应该返回400错误")
    void analyze_EmptyRequestBody_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/analysis/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    private AnalysisResponse createMockAnalysisResponse() {
        AnalysisResponse response = new AnalysisResponse();
        response.setAnalysisId("ANALYSIS-1234567890");
        response.setAppId("app-001");
        response.setAlarmDescription("用户登录失败率突然升高");
        response.setAnalysisTime(LocalDateTime.now());

        AnalysisResponse.RootCause rootCause = new AnalysisResponse.RootCause(
                "高错误率",
                "当前错误率(10.98%)超过10%阈值，可能存在服务异常或业务逻辑错误",
                "85%"
        );
        response.setRootCause(rootCause);

        List<AnalysisResponse.Evidence> evidences = Arrays.asList(
                new AnalysisResponse.Evidence("请求量监控", "错误率: 10.98%, 总请求数: 9740", "高"),
                new AnalysisResponse.Evidence("应用监控(CPU)", "CPU使用率: 24.33%", "低"),
                new AnalysisResponse.Evidence("发布记录", "最近发布版本: 1.7.33", "中")
        );
        response.setEvidences(evidences);

        List<String> suggestions = Arrays.asList(
                "检查服务日志，定位具体错误类型",
                "查看失败请求的详细信息",
                "查看完整监控面板获取更多上下文"
        );
        response.setSuggestions(suggestions);

        return response;
    }
}