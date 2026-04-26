package com.familytree.integration;

import com.familytree.dto.ApiResponse;
import com.familytree.dto.FamilyCreateRequest;
import com.familytree.model.Family;
import com.familytree.model.User;
import com.familytree.repository.FamilyRepository;
import com.familytree.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FamilyApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private Long testUserId;

    @BeforeEach
    void setUp() throws Exception {
        familyRepository.deleteAll();
        userRepository.deleteAll();
        authToken = null;
        testUserId = null;

        User user = new User();
        user.setEmail("integration-test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setNickname("Integration Test User");
        user = userRepository.save(user);
        testUserId = user.getId();

        String loginRequest = """
                {
                    "email": "integration-test@example.com",
                    "password": "password123"
                }
                """;

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse<?> loginResponse = objectMapper.readValue(
                loginResult.getResponse().getContentAsString(),
                ApiResponse.class);

        authToken = (String) ((java.util.Map<?, ?>) loginResponse.getData()).get("token");
    }

    @AfterEach
    void tearDown() {
        familyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("创建家族 - 成功")
    void testCreateFamily_Success() throws Exception {
        FamilyCreateRequest request = new FamilyCreateRequest();
        request.setName("测试家族");
        request.setDescription("这是一个测试家族");

        mockMvc.perform(post("/api/families")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("测试家族"))
                .andExpect(jsonPath("$.data.description").value("这是一个测试家族"))
                .andExpect(jsonPath("$.data.creatorId").value(testUserId));
    }

    @Test
    @Order(2)
    @DisplayName("创建家族 - 未授权")
    void testCreateFamily_Unauthorized() throws Exception {
        FamilyCreateRequest request = new FamilyCreateRequest();
        request.setName("测试家族");

        mockMvc.perform(post("/api/families")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    @DisplayName("获取家族列表")
    void testGetFamilies() throws Exception {
        FamilyCreateRequest request = new FamilyCreateRequest();
        request.setName("测试家族1");
        request.setDescription("第一个测试家族");

        mockMvc.perform(post("/api/families")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/families")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.data[0].name").value("测试家族1"));
    }

    @Test
    @Order(4)
    @DisplayName("获取家族详情")
    void testGetFamilyById() throws Exception {
        FamilyCreateRequest createRequest = new FamilyCreateRequest();
        createRequest.setName("测试家族详情");
        createRequest.setDescription("用于测试获取详情");

        MvcResult createResult = mockMvc.perform(post("/api/families")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse<?> createResponse = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                ApiResponse.class);

        Long familyId = ((Family) createResponse.getData()).getId();

        mockMvc.perform(get("/api/families/" + familyId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(familyId))
                .andExpect(jsonPath("$.data.name").value("测试家族详情"));
    }

    @Test
    @Order(5)
    @DisplayName("更新家族")
    void testUpdateFamily() throws Exception {
        FamilyCreateRequest createRequest = new FamilyCreateRequest();
        createRequest.setName("原始名称");

        MvcResult createResult = mockMvc.perform(post("/api/families")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse<?> createResponse = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                ApiResponse.class);

        Long familyId = ((Family) createResponse.getData()).getId();

        String updateRequest = """
                {
                    "name": "更新后的名称",
                    "description": "更新后的描述"
                }
                """;

        mockMvc.perform(put("/api/families/" + familyId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("更新后的名称"))
                .andExpect(jsonPath("$.data.description").value("更新后的描述"));
    }

    @Test
    @Order(6)
    @DisplayName("删除家族")
    void testDeleteFamily() throws Exception {
        FamilyCreateRequest createRequest = new FamilyCreateRequest();
        createRequest.setName("待删除的家族");

        MvcResult createResult = mockMvc.perform(post("/api/families")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse<?> createResponse = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                ApiResponse.class);

        Long familyId = ((Family) createResponse.getData()).getId();

        mockMvc.perform(delete("/api/families/" + familyId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(get("/api/families/" + familyId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    @DisplayName("检查管理员权限")
    void testIsAdministrator() throws Exception {
        FamilyCreateRequest createRequest = new FamilyCreateRequest();
        createRequest.setName("管理员测试家族");

        MvcResult createResult = mockMvc.perform(post("/api/families")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse<?> createResponse = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                ApiResponse.class);

        Long familyId = ((Family) createResponse.getData()).getId();

        mockMvc.perform(get("/api/families/" + familyId + "/administrator")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }
}