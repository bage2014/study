package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.dto.FamilyMemberTree;
import com.bage.my.app.end.point.dto.QueryFamilyResponse;
import com.bage.my.app.end.point.entity.ApiResponse;
import com.bage.my.app.end.point.entity.FamilyMember;
import com.bage.my.app.end.point.entity.FamilyRelationship;
import com.bage.my.app.end.point.service.FamilyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

class FamilyControllerTest {

    @Mock
    private FamilyService familyService;

    @InjectMocks
    private FamilyController familyController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(familyController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // 支持LocalDate等Java 8日期类型
    }

    // 辅助方法：创建FamilyMember实例
    private FamilyMember createTestMember(Long id, String name) {
        FamilyMember member = new FamilyMember();
        member.setId(id);
        member.setName(name);
        member.setBirthDate(LocalDate.of(1990, 1, 1));
        return member;
    }

    // 测试查询成员列表接口
    @Test
    void testQueryMembers() throws Exception {
        // 准备测试数据
        List<FamilyMember> members = new ArrayList<>();
        members.add(createTestMember(1L, "张三"));
        members.add(createTestMember(2L, "李四"));

        Page<FamilyMember> memberPage = new PageImpl<>(members, PageRequest.of(0, 10), members.size());

        // 模拟服务层调用
        when(familyService.queryMembers(null, PageRequest.of(0, 10))).thenReturn(memberPage);

        // 执行测试
        mockMvc.perform(get("/family/members/query")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.members[0].name").value("张三"))
                .andExpect(jsonPath("$.data.total").value(2));
    }

    // 测试添加成员接口
    @Test
    void testAddMember() throws Exception {
        // 准备测试数据
        FamilyMember member = createTestMember(null, "王五");
        FamilyMember savedMember = createTestMember(3L, "王五");

        // 模拟服务层调用
        when(familyService.saveMember(any(FamilyMember.class))).thenReturn(savedMember);

        // 执行测试
        mockMvc.perform(post("/family/members/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(3L))
                .andExpect(jsonPath("$.data.name").value("王五"));
    }

    // 测试更新成员接口
    @Test
    void testUpdateMember() throws Exception {
        // 准备测试数据
        FamilyMember memberToUpdate = createTestMember(4L, "赵六更新");
        FamilyMember existingMember = createTestMember(4L, "赵六");
        FamilyMember updatedMember = createTestMember(4L, "赵六更新");

        // 模拟服务层调用
        when(familyService.getMemberById(4L)).thenReturn(existingMember);
        when(familyService.saveMember(any(FamilyMember.class))).thenReturn(updatedMember);

        // 执行测试
        mockMvc.perform(post("/family/members/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(4L))
                .andExpect(jsonPath("$.data.name").value("赵六更新"));
    }

    // 测试删除成员接口
    @Test
    void testDeleteMember() throws Exception {
        // 准备测试数据
        FamilyMember member = new FamilyMember();
        member.setId(5L);

        // 模拟服务层调用
        doNothing().when(familyService).deleteMember(any(FamilyMember.class));

        // 执行测试
        mockMvc.perform(post("/family/members/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("ok"));
    }

    // 测试获取单个成员接口
    @Test
    void testGetMember() throws Exception {
        // 准备测试数据
        FamilyMember member = createTestMember(6L, "孙七");

        // 模拟服务层调用
        when(familyService.getMemberById(6L)).thenReturn(member);

        // 执行测试
        mockMvc.perform(get("/family/members/6")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(6L))
                .andExpect(jsonPath("$.data.name").value("孙七"));
    }

    // 测试添加关系接口
    @Test
    void testAddRelationship() throws Exception {
        // 准备测试数据
        FamilyRelationship relationship = new FamilyRelationship();
        relationship.setId(1L);
        relationship.setMember1(createTestMember(1L, "张三"));
        relationship.setMember2(createTestMember(2L, "李四"));
        relationship.setType("parent");

        // 模拟服务层调用
        when(familyService.saveRelationship(any(FamilyRelationship.class))).thenReturn(relationship);

        // 执行测试
        mockMvc.perform(post("/family/relationships")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(relationship)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.type").value("parent"));
    }

    // 测试删除关系接口
    @Test
    void testDeleteRelationship() throws Exception {
        // 准备测试数据
        FamilyRelationship relationship = new FamilyRelationship();
        relationship.setId(1L);

        // 模拟服务层调用
        doNothing().when(familyService).deleteRelationship(1L);

        // 执行测试
        mockMvc.perform(post("/family/relationships/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(relationship)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("ok"));
    }

    // 测试获取家族树接口
    @Test
    void testGetFamilyTree() throws Exception {
        // 准备测试数据
        FamilyMemberTree tree = new FamilyMemberTree(1L, "张三", "avatar.jpg", 0, "self", -1);

        // 模拟服务层调用
        when(familyService.getFamilyTree(1L, 3)).thenReturn(tree);

        // 执行测试
        mockMvc.perform(get("/family/tree/1")
                .param("generations", "3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("张三"));
    }

    // 测试初始化家族树接口
    @Test
    void testInitFamilyTree() throws Exception {
        // 模拟服务层调用
        when(familyService.getMemberById(1L)).thenReturn(null);

        // 执行测试
        mockMvc.perform(get("/family/init")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("初始化成功"));
    }
}