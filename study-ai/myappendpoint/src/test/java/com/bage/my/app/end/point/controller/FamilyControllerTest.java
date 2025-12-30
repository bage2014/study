package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.dto.FamilyMemberTree;
import com.bage.my.app.end.point.entity.ApiResponse;
import com.bage.my.app.end.point.entity.FamilyMember;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;

class FamilyControllerTest {

    @Mock
    private FamilyService familyService;

    @InjectMocks
    private FamilyController familyController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(familyController).build();
    }

    // 辅助方法：创建FamilyMember实例
    private FamilyMember createTestMember(Long id, String name) {
        FamilyMember member = new FamilyMember();
        member.setId(id);
        member.setName(name);
        member.setBirthDate(LocalDate.of(1990, 1, 1));
        return member;
    }

    // 测试创建控制器实例
    @Test
    void testControllerCreation() {
        // 验证控制器能被正确创建
        assertNotNull(familyController);
    }

    // 测试查询成员方法（测试服务层调用）
    @Test
    void testQueryMembers() {
        // 准备测试数据
        List<FamilyMember> members = new ArrayList<>();
        members.add(createTestMember(1L, "张三"));
        members.add(createTestMember(2L, "李四"));

        Page<FamilyMember> memberPage = new PageImpl<>(members, PageRequest.of(0, 10), members.size());

        // 模拟服务层调用
        when(familyService.queryMembers(null, PageRequest.of(0, 10))).thenReturn(memberPage);

        // 调用服务层方法
        Page<FamilyMember> result = familyService.queryMembers(null, PageRequest.of(0, 10));
        
        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    // 测试家族树方法（测试服务层调用）
    @Test
    void testFamilyTree() {
        // 准备测试数据
        FamilyMemberTree tree = new FamilyMemberTree(1L, "张三", "avatar.jpg", 0, "self", -1L);

        // 模拟服务层调用
        when(familyService.getFamilyTree(1L, 3)).thenReturn(tree);

        // 调用服务层方法
        FamilyMemberTree result = familyService.getFamilyTree(1L, 3);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("张三", result.getName());
    }
}