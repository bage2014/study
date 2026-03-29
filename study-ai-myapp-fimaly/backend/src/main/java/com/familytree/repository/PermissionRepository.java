package com.familytree.repository;

import com.familytree.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByUserId(Long userId);
    List<Permission> findByFamilyId(Long familyId);
    Optional<Permission> findByUserIdAndFamilyId(Long userId, Long familyId);
}