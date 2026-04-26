package com.familytree.controller;

import com.familytree.dto.*;
import com.familytree.model.Family;
import com.familytree.service.FamilyService;
import com.familytree.service.PermissionService;
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
@RequestMapping("/api/families")
@RequiredArgsConstructor
@Validated
@Tag(name = "家族管理", description = "家族CRUD和成员管理接口")
public class FamilyController {

    private final FamilyService familyService;
    private final PermissionService permissionService;

    @Operation(summary = "创建家族", description = "创建一个新的家族，创建者自动成为管理员")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "创建成功",
                    content = @Content(schema = @Schema(implementation = Family.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "参数错误")
    })
    @PostMapping
    public ApiResponse<Family> createFamily(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Valid @RequestBody FamilyCreateRequest request) {
        log.info("[创建家族] userId={}, request={}", userId, request);
        try {
            Family createdFamily = familyService.createFamily(
                    request.getName(),
                    request.getDescription(),
                    request.getAvatar(),
                    userId
            );
            return ApiResponse.success(createdFamily, "家族创建成功");
        } catch (RuntimeException e) {
            log.warn("[创建家族] 失败: {}", e.getMessage());
            return ApiResponse.error("F001", e.getMessage());
        }
    }

    @Operation(summary = "获取家族列表", description = "获取当前用户创建的所有家族")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "获取成功")
    })
    @GetMapping
    public ApiResponse<List<Family>> getFamilies(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        log.info("[获取家族列表] userId={}", userId);
        try {
            List<Family> families = familyService.getFamiliesByCreatorId(userId);
            return ApiResponse.success(families);
        } catch (RuntimeException e) {
            log.warn("[获取家族列表] 失败: {}", e.getMessage());
            return ApiResponse.error("F002", e.getMessage());
        }
    }

    @Operation(summary = "获取家族详情", description = "根据ID获取家族的详细信息")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "获取成功",
                    content = @Content(schema = @Schema(implementation = Family.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "家族不存在")
    })
    @GetMapping("/{id}")
    public ApiResponse<Family> getFamily(
            @Parameter(description = "家族ID") @PathVariable Long id) {
        log.info("[获取家族详情] familyId={}", id);
        try {
            Family family = familyService.getFamilyById(id);
            return ApiResponse.success(family);
        } catch (RuntimeException e) {
            log.warn("[获取家族详情] 失败: {}", e.getMessage());
            return ApiResponse.error("F001", e.getMessage());
        }
    }

    @Operation(summary = "更新家族", description = "更新家族的名称、描述或头像")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功",
                    content = @Content(schema = @Schema(implementation = Family.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "参数错误"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "家族不存在")
    })
    @PutMapping("/{id}")
    public ApiResponse<Family> updateFamily(
            @Parameter(description = "家族ID") @PathVariable Long id,
            @Valid @RequestBody FamilyUpdateRequest request) {
        log.info("[更新家族] familyId={}, request={}", id, request);
        try {
            Family updatedFamily = familyService.updateFamily(
                    id,
                    request.getName(),
                    request.getDescription(),
                    request.getAvatar()
            );
            return ApiResponse.success(updatedFamily, "家族更新成功");
        } catch (RuntimeException e) {
            log.warn("[更新家族] 失败: {}", e.getMessage());
            return ApiResponse.error("F001", e.getMessage());
        }
    }

    @Operation(summary = "删除家族", description = "删除指定的家族")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "删除成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "家族不存在")
    })
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFamily(
            @Parameter(description = "家族ID") @PathVariable Long id) {
        log.info("[删除家族] familyId={}", id);
        try {
            familyService.deleteFamily(id);
            return ApiResponse.success(null, "家族删除成功");
        } catch (RuntimeException e) {
            log.warn("[删除家族] 失败: {}", e.getMessage());
            return ApiResponse.error("F001", e.getMessage());
        }
    }

    @Operation(summary = "离开家族", description = "当前用户离开指定的家族")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "操作成功")
    })
    @PostMapping("/{id}/leave")
    public ApiResponse<Void> leaveFamily(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "家族ID") @PathVariable Long id) {
        log.info("[离开家族] userId={}, familyId={}", userId, id);
        permissionService.removeUserFromFamily(userId, id);
        return ApiResponse.success(null, "已离开家族");
    }

    @Operation(summary = "更新管理员", description = "将家族的管理员权限转移给其他用户")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功",
                    content = @Content(schema = @Schema(implementation = Family.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "无权限操作")
    })
    @PutMapping("/{id}/administrator")
    public ApiResponse<Family> updateAdministrator(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "家族ID") @PathVariable Long id,
            @RequestBody Long administratorId) {
        log.info("[更新管理员] currentUserId={}, familyId={}, newAdminId={}", userId, id, administratorId);
        if (!familyService.isAdministrator(id, userId)) {
            return ApiResponse.error("只有管理员可以执行此操作");
        }
        Family updatedFamily = familyService.updateAdministrator(id, administratorId);
        return ApiResponse.success(updatedFamily, "管理员更新成功");
    }

    @Operation(summary = "检查管理员权限", description = "检查当前用户是否为家族管理员")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "获取成功")
    })
    @GetMapping("/{id}/administrator")
    public ApiResponse<Boolean> isAdministrator(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "家族ID") @PathVariable Long id) {
        log.debug("[检查管理员权限] userId={}, familyId={}", userId, id);
        boolean isAdmin = familyService.isAdministrator(id, userId);
        return ApiResponse.success(isAdmin);
    }
}