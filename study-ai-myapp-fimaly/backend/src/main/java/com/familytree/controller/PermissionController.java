package com.familytree.controller;

import com.familytree.model.Permission;
import com.familytree.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    
    @PostMapping
    public ResponseEntity<Permission> addPermission(@RequestBody Permission permission) {
        Permission createdPermission = permissionService.addPermission(
                permission.getUserId(),
                permission.getFamilyId(),
                permission.getPermissionLevel()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Permission>> getPermissionsByUserId(@PathVariable Long userId) {
        List<Permission> permissions = permissionService.getPermissionsByUserId(userId);
        return ResponseEntity.ok(permissions);
    }
    
    @GetMapping("/family/{familyId}")
    public ResponseEntity<List<Permission>> getPermissionsByFamilyId(@PathVariable Long familyId) {
        List<Permission> permissions = permissionService.getPermissionsByFamilyId(familyId);
        return ResponseEntity.ok(permissions);
    }
    
    @PutMapping("/user/{userId}/family/{familyId}")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long userId, @PathVariable Long familyId, @RequestBody Permission permission) {
        Permission updatedPermission = permissionService.updatePermission(
                userId,
                familyId,
                permission.getPermissionLevel()
        );
        return ResponseEntity.ok(updatedPermission);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}