package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Permission;
import com.familytree.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    
    @PostMapping
    public ApiResponse<Permission> assignPermission(@RequestBody Permission permission) {
        try {
            Permission createdPermission = permissionService.assignPermission(
                    permission.getUserId(),
                    permission.getFamilyId(),
                    permission.getRole()
            );
            return ApiResponse.success(createdPermission);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping
    public ApiResponse<List<Permission>> getPermissions() {
        try {
            List<Permission> permissions = permissionService.getPermissions();
            return ApiResponse.success(permissions);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Permission> updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        try {
            Permission updatedPermission = permissionService.updatePermission(id, permission.getRole());
            return ApiResponse.success(updatedPermission);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePermission(@PathVariable Long id) {
        try {
            permissionService.deletePermission(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
