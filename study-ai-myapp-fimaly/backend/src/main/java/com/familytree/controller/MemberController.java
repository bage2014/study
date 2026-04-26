package com.familytree.controller;

import com.familytree.dto.*;
import com.familytree.model.Member;
import com.familytree.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Tag(name = "成员管理", description = "家族成员CRUD接口")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "添加成员", description = "向指定家族添加新成员")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "添加成功",
                    content = @Content(schema = @Schema(implementation = Member.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "参数错误")
    })
    @PostMapping("/members")
    public ApiResponse<Member> addMember(@Valid @RequestBody MemberCreateRequest request) {
        log.info("[添加成员] request={}", request);
        try {
            Member createdMember = memberService.addMember(
                    request.getFamilyId(),
                    request.getName(),
                    request.getGender(),
                    request.getBirthDate(),
                    request.getDeathDate(),
                    request.getPhoto(),
                    request.getDetails(),
                    request.getPhone(),
                    request.getEmail()
            );
            return ApiResponse.success(createdMember, "成员添加成功");
        } catch (RuntimeException e) {
            log.warn("[添加成员] 失败: {}", e.getMessage());
            return ApiResponse.error("M001", e.getMessage());
        }
    }

    @Operation(summary = "获取家族成员列表", description = "获取指定家族的所有成员")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "获取成功")
    })
    @GetMapping("/members/family")
    public ApiResponse<List<Member>> getMembers(
            @Parameter(description = "家族ID", required = true) @RequestParam Long familyId) {
        log.info("[获取家族成员列表] familyId={}", familyId);
        try {
            List<Member> members = memberService.getMembersByFamilyId(familyId);
            return ApiResponse.success(members);
        } catch (RuntimeException e) {
            log.warn("[获取家族成员列表] 失败: {}", e.getMessage());
            return ApiResponse.error("M002", e.getMessage());
        }
    }

    @Operation(summary = "获取成员详情", description = "根据ID获取成员的详细信息")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "获取成功",
                    content = @Content(schema = @Schema(implementation = Member.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "成员不存在")
    })
    @GetMapping("/members/{id}")
    public ApiResponse<Member> getMember(
            @Parameter(description = "成员ID") @PathVariable Long id) {
        log.info("[获取成员详情] memberId={}", id);
        try {
            Member member = memberService.getMemberById(id);
            return ApiResponse.success(member);
        } catch (RuntimeException e) {
            log.warn("[获取成员详情] 失败: {}", e.getMessage());
            return ApiResponse.error("M001", e.getMessage());
        }
    }

    @Operation(summary = "更新成员", description = "更新成员的详细信息")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功",
                    content = @Content(schema = @Schema(implementation = Member.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "参数错误"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "成员不存在")
    })
    @PutMapping("/members/{id}")
    public ApiResponse<Member> updateMember(
            @Parameter(description = "成员ID") @PathVariable Long id,
            @Valid @RequestBody MemberUpdateRequest request) {
        log.info("[更新成员] memberId={}, request={}", id, request);
        try {
            Member updatedMember = memberService.updateMember(
                    id,
                    request.getName(),
                    request.getGender(),
                    request.getBirthDate(),
                    request.getDeathDate(),
                    request.getPhoto(),
                    request.getDetails(),
                    request.getPhone(),
                    request.getEmail()
            );
            return ApiResponse.success(updatedMember, "成员更新成功");
        } catch (RuntimeException e) {
            log.warn("[更新成员] 失败: {}", e.getMessage());
            return ApiResponse.error("M001", e.getMessage());
        }
    }

    @Operation(summary = "删除成员", description = "删除指定的成员")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "删除成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "成员不存在")
    })
    @DeleteMapping("/members/{id}")
    public ApiResponse<Void> deleteMember(
            @Parameter(description = "成员ID") @PathVariable Long id) {
        log.info("[删除成员] memberId={}", id);
        try {
            memberService.deleteMember(id);
            return ApiResponse.success(null, "成员删除成功");
        } catch (RuntimeException e) {
            log.warn("[删除成员] 失败: {}", e.getMessage());
            return ApiResponse.error("M001", e.getMessage());
        }
    }

    @Operation(summary = "获取所有成员", description = "获取系统中所有成员")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "获取成功")
    })
    @GetMapping("/members")
    public ApiResponse<List<Member>> getAllMembers() {
        log.info("[获取所有成员列表]");
        List<Member> members = memberService.getAllMembers();
        return ApiResponse.success(members);
    }

    @Operation(summary = "搜索成员", description = "根据手机号或邮箱搜索成员")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "搜索成功")
    })
    @GetMapping("/members/search")
    public ApiResponse<List<Member>> searchMembers(
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "邮箱") @RequestParam(required = false) String email) {
        log.info("[搜索成员] phone={}, email={}", phone, email);
        List<Member> members = memberService.searchMembers(phone, email);
        return ApiResponse.success(members);
    }
}