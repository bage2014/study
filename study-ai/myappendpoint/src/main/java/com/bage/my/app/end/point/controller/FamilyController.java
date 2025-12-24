package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.FamilyMember;
import com.bage.my.app.end.point.entity.FamilyRelationship;
import com.bage.my.app.end.point.entity.Gender;
import com.bage.my.app.end.point.service.FamilyService;
import com.bage.my.app.end.point.util.AuthUtil;
import com.bage.my.app.end.point.util.JsonUtil;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import com.bage.my.app.end.point.dto.FamilyMemberTree;
import com.bage.my.app.end.point.entity.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.bage.my.app.end.point.entity.RelationshipType;
import com.bage.my.app.end.point.repository.FamilyMemberRepository;

import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

import com.bage.my.app.end.point.dto.QueryFamilyResponse;

@RestController
@RequestMapping("/family")
@Slf4j
public class FamilyController {
    
    @Autowired
    private FamilyService familyService;
    
    @Autowired
    private FamilyMemberRepository memberRepository;
    
    /**
     * 查询成员列表（支持关键词搜索和分页）
     */
    @RequestMapping("/members/query")
    public ApiResponse<QueryFamilyResponse> queryMembers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<FamilyMember> memberPage = null;
            Pageable pageable = PageRequest.of(page, size);
            if (keyword != null && !keyword.isEmpty()) {
                memberPage = memberRepository.findByKeyword(keyword, pageable);
            } else {
                memberPage = memberRepository.findAll(pageable);
            }
            
            QueryFamilyResponse response = new QueryFamilyResponse(
                memberPage.getContent(),
                memberPage.getTotalElements(),
                memberPage.getTotalPages(),
                memberPage.getNumber(),
                memberPage.getSize()
            );
            return ApiResponse.success(response);
            
        } catch (Exception e) {
            log.error("查询成员列表失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "查询成员列表失败");
        }
    }
    
    // 原有方法保持不变
    @PostMapping("/members/add")
    public ApiResponse<FamilyMember> addMember(@RequestBody FamilyMember member) {
        try {
            FamilyMember savedMember = familyService.saveMember(member);
            return ApiResponse.success(savedMember);
        } catch (IllegalArgumentException e) {
            log.error("添加成员参数错误: {}", e.getMessage(), e);
            return ApiResponse.fail(400, e.getMessage());
        } catch (Exception e) {
            log.error("添加成员系统错误", e);
            return ApiResponse.fail(500, "服务器内部错误");
        }
    }

    /**
     * 更新成员
     */
    @PostMapping("/members/update")
    public ApiResponse<FamilyMember> updateMember(@RequestBody FamilyMember member) {
        try {
            // 检查成员是否存在
            if (member.getId() == null) {
                return ApiResponse.fail(400, "成员ID不能为空");
            }
            FamilyMember existingMember = familyService.getMemberById(member.getId());
            if (existingMember == null) {
                return ApiResponse.fail(404, "成员不存在");
            }
            
            FamilyMember updatedMember = familyService.saveMember(member);
            return ApiResponse.success(updatedMember);
        } catch (IllegalArgumentException e) {
            log.error("更新成员参数错误: {}", e.getMessage(), e);
            return ApiResponse.fail(400, e.getMessage());
        } catch (Exception e) {
            log.error("更新成员系统错误", e);
            return ApiResponse.fail(500, "服务器内部错误");
        }
    }

    // 原有方法保持不变
    @PostMapping("/members/delete")
    public ApiResponse<String> deleteMember(@RequestBody FamilyMember member) {
        try {
            familyService.deleteMember(member);
            return ApiResponse.success("ok");
        } catch (IllegalArgumentException e) {
            log.error("删除成员参数错误: {}", e.getMessage(), e);
            return ApiResponse.fail(400, e.getMessage());
        } catch (Exception e) {
            log.error("删除成员系统错误", e);
            return ApiResponse.fail(500, "服务器内部错误");
        }
    }

    @RequestMapping("/save/mock")
    public ApiResponse<FamilyMember> addMember() {
        try {
            // 初始化张三
            FamilyMember zhangSan = new FamilyMember();
            zhangSan.setName("张三");
            zhangSan.setAvatar("https://avatars.githubusercontent.com/u/18094768?s=400&u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&v=4");
            // 设置随机值
            zhangSan.setGender(Math.random() > 0.5 ? Gender.MALE : Gender.FEMALE);
            zhangSan.setBirthDate(LocalDate.now().minusYears(30 + (int)(Math.random() * 20)));
            zhangSan.setOccupation(randomOccupation());
            zhangSan.setEducation("本科");
            zhangSan.setContactInfo("13800138000");
            zhangSan.setDeceased(false);
            zhangSan.setDeathDate(null);
            FamilyMember root = familyService.saveMember(zhangSan);
            log.info("initFamilyTree root: {}", root);
            return ApiResponse.success(root);
        } catch (IllegalArgumentException e) {
            log.error("添加成员参数错误: {}", e.getMessage(), e);
            return ApiResponse.fail(400, e.getMessage());
        } catch (Exception e) {
            log.error("添加成员系统错误", e);
            return ApiResponse.fail(500, "服务器内部错误");
        }
    }
    
    
    @GetMapping("/members/{id}")
    public ApiResponse<FamilyMember> getMember(@PathVariable Long id) {
        try {
            FamilyMember member = familyService.getMemberById(id);
            if (member == null) {
                log.error("获取成员不存在, ID: {}", id);
                return ApiResponse.fail(404, "成员不存在");
            }
            return ApiResponse.success(member);
        } catch (Exception e) {
            log.error("获取成员系统错误, ID: {}", id, e);
            return ApiResponse.fail(500, "服务器内部错误");
        }
    }
    
    @PostMapping("/relationships")
    public ApiResponse<FamilyRelationship> addRelationship(@RequestBody FamilyRelationship relationship) {
        try {
            // 完善空指针检查
            if (relationship == null) {
                throw new IllegalArgumentException("关系信息不能为空");
            }
            
            if (relationship.getMember1() == null) {
                relationship.setMember1(new FamilyMember());
            }
            
            if (relationship.getMember2() == null) {
                relationship.setMember2(new FamilyMember());
            }
            
            if(relationship.getMember1().getId() == null){
                relationship.getMember1().setId(AuthUtil.getCurrentUserId());
            }
            
            // 确保member2有ID
            if (relationship.getMember2().getId() == null) {
                throw new IllegalArgumentException("成员2的ID不能为空");
            }
            
            familyService.validateRelationship(relationship);
            log.info("addRelationship relationship: {}", JsonUtil.toJson(relationship));
            FamilyRelationship savedRelationship = familyService.saveRelationship(relationship);
            return ApiResponse.success(savedRelationship);
        } catch (IllegalArgumentException e) {
            log.error("添加关系参数错误: {}", e.getMessage(), e);
            return ApiResponse.fail(400, e.getMessage());
        } catch (Exception e) {
            log.error("添加关系系统错误", e);
            return ApiResponse.fail(500, "服务器内部错误");
        }
    }
    
    // 在addRelationship方法之后添加
    /**
     * 删除家族关系
     */
    @RequestMapping("/relationships/delete")
    public ApiResponse<String> deleteRelationship(@RequestBody FamilyRelationship relationship) {
        try {
            familyService.deleteRelationship(relationship.getId());
            return ApiResponse.success("ok");
        } catch (IllegalArgumentException e) {
            log.error("删除关系参数错误: {}", e.getMessage(), e);
            return ApiResponse.fail(400, e.getMessage());
        } catch (Exception e) {
            log.error("删除关系系统错误", e);
            return ApiResponse.fail(500, "服务器内部错误");
        }
    }
    
    @GetMapping("/tree/{rootId}")
    public ApiResponse<FamilyMemberTree> getFamilyTree(
            @PathVariable Long rootId, 
            @RequestParam(defaultValue = "3") int generations) {
        try {
            FamilyMemberTree familyTree = familyService.getFamilyTree(rootId, generations);
            log.info("getFamilyTree familyTree: {}", JsonUtil.toJson(familyTree));
            if (familyTree == null) {
                log.error("获取家族树为空, rootId: {}, generations: {}", rootId, generations);
                return ApiResponse.fail(404, "未找到家族树");
            }
            return ApiResponse.success(familyTree);
        } catch (Exception e) {
            log.error("获取家族树系统错误, rootId: {}, generations: {}", rootId, generations, e);
            return ApiResponse.fail(500, "服务器内部错误");
        }
    }

    private String randomOccupation() {
        String[] occupations = {"工程师", "医生", "教师", "农民", "商人"};
        return occupations[(int)(Math.random() * occupations.length)];
    }
    
    @PostConstruct
    @Transactional
    public ApiResponse<String> initFamilyTree() {
        try {
            // 创建并保存所有成员
            FamilyMember zhangSan = createAndSaveMember("张三", "https://avatars.githubusercontent.com/u/18094768?v=4", 0);
            log.info("initFamilyTree zhangSan: {}", zhangSan);
            FamilyMember liSi = createAndSaveMember("李四", "https://avatars.githubusercontent.com/u/18094768?v=4", 0);
            // log.info("initFamilyTree liSi: {}", liSi);
            FamilyMember father = createAndSaveMember("张父", "https://avatars.githubusercontent.com/u/18094768?s=48&v=4", -1);
            // log.info("initFamilyTree father: {}", father);
            FamilyMember mother = createAndSaveMember("张母", "https://avatars.githubusercontent.com/u/18094768?s=48&v=4", -1);
            // log.info("initFamilyTree mother: {}", mother);
            FamilyMember grandfather = createAndSaveMember("张祖父", "https://avatars.githubusercontent.com/u/18094768?s=48&v=4", -2);
            // log.info("initFamilyTree grandfather: {}", grandfather);
            FamilyMember xiaoSan = createAndSaveMember("张小三", "https://avatars.githubusercontent.com/u/18094768?s=48&v=4", 1);
            // log.info("initFamilyTree xiaoSan: {}", xiaoSan);

            // 建立配偶关系
            createAndSaveRelationship(zhangSan, liSi, RelationshipType.SPOUSE);
            // log.info("initFamilyTree zhangSan and liSi: {}", zhangSan);
            createAndSaveRelationship(father, mother, RelationshipType.SPOUSE);
            // log.info("initFamilyTree father and mother: {}", father);
    
            // 建立父子关系
            createAndSaveRelationship(father, zhangSan, RelationshipType.PARENT_CHILD);
            // log.info("initFamilyTree father and zhangSan: {}", father);
            createAndSaveRelationship(grandfather, father, RelationshipType.PARENT_CHILD);
            // log.info("initFamilyTree grandfather and father: {}", grandfather);
            // createAndSaveRelationship(zhangSan, xiaoSan, RelationshipType.PARENT_CHILD);
            // log.info("initFamilyTree zhangSan and xiaoSan: {}", zhangSan);
    
            return ApiResponse.success("家族树初始化成功");
        } catch (Exception e) {
            log.error("初始化家族树失败", e);
            return ApiResponse.fail(500, "初始化家族树失败: " + e.getMessage());
        }
    }
    
    private FamilyMember createAndSaveMember(String name, String avatar, int generation) {
        FamilyMember member = new FamilyMember();
        member.setName(name);
        member.setAvatar(avatar);
        member.setGeneration(generation);
        return familyService.saveMember(member);
    }
    
    private void createAndSaveRelationship(FamilyMember from, FamilyMember to, RelationshipType type) {
        FamilyRelationship relationship = new FamilyRelationship();
        relationship.setMember1(from);
        relationship.setMember2(to);
        relationship.setType(type);
        familyService.saveRelationship(relationship);
    }
}