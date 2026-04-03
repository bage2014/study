package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Family;
import com.familytree.service.FamilyService;
import com.familytree.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/families")
public class FamilyController {
    @Autowired
    private FamilyService familyService;
    
    @Autowired
    private PermissionService permissionService;
    
    @PostMapping
    public ApiResponse<Family> createFamily(@RequestAttribute("userId") Long userId, @RequestBody Family family) {
        try {
            Family createdFamily = familyService.createFamily(
                    family.getName(),
                    family.getDescription(),
                    family.getAvatar(),
                    userId
            );
            return ApiResponse.success(createdFamily);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping
    public ApiResponse<List<Family>> getFamilies(@RequestAttribute("userId") Long userId) {
        try {
            List<Family> families = familyService.getFamiliesByCreatorId(userId);
            return ApiResponse.success(families);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Family> getFamily(@PathVariable Long id) {
        try {
            Family family = familyService.getFamilyById(id);
            return ApiResponse.success(family);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Family> updateFamily(@PathVariable Long id, @RequestBody Family family) {
        try {
            Family updatedFamily = familyService.updateFamily(
                    id,
                    family.getName(),
                    family.getDescription(),
                    family.getAvatar()
            );
            return ApiResponse.success(updatedFamily);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFamily(@PathVariable Long id) {
        try {
            familyService.deleteFamily(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/{id}/leave")
    public ApiResponse<Void> leaveFamily(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        try {
            permissionService.removeUserFromFamily(userId, id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
