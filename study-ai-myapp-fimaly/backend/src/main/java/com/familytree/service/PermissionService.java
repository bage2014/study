package com.familytree.service;

import com.familytree.model.Permission;
import com.familytree.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;
    
    public Permission assignPermission(Long userId, Long familyId, String role) {
        Permission permission = new Permission();
        permission.setUserId(userId);
        permission.setFamilyId(familyId);
        permission.setRole(role);
        return permissionRepository.save(permission);
    }
    
    public List<Permission> getPermissions() {
        return permissionRepository.findAll();
    }
    
    public Permission updatePermission(Long id, String role) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        permission.setRole(role);
        return permissionRepository.save(permission);
    }
    
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }
    
    public void removeUserFromFamily(Long userId, Long familyId) {
        Permission permission = permissionRepository.findByUserIdAndFamilyId(userId, familyId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        permissionRepository.delete(permission);
    }
}