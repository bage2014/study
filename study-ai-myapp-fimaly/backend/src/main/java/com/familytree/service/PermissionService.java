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
    
    public Permission addPermission(Long userId, Long familyId, String permissionLevel) {
        Permission permission = new Permission();
        permission.setUserId(userId);
        permission.setFamilyId(familyId);
        permission.setPermissionLevel(permissionLevel);
        return permissionRepository.save(permission);
    }
    
    public List<Permission> getPermissionsByUserId(Long userId) {
        return permissionRepository.findByUserId(userId);
    }
    
    public List<Permission> getPermissionsByFamilyId(Long familyId) {
        return permissionRepository.findByFamilyId(familyId);
    }
    
    public Permission getPermissionByUserIdAndFamilyId(Long userId, Long familyId) {
        return permissionRepository.findByUserIdAndFamilyId(userId, familyId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
    }
    
    public Permission updatePermission(Long userId, Long familyId, String permissionLevel) {
        Permission permission = getPermissionByUserIdAndFamilyId(userId, familyId);
        permission.setPermissionLevel(permissionLevel);
        return permissionRepository.save(permission);
    }
    
    public void deletePermission(Long permissionId) {
        permissionRepository.deleteById(permissionId);
    }
}