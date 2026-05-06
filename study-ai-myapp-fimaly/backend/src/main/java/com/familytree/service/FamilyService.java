package com.familytree.service;

import com.familytree.exception.BusinessException;
import com.familytree.exception.ErrorCode;
import com.familytree.model.Family;
import com.familytree.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;

    @Transactional
    public Family createFamily(String name, String description, String avatar, Long creatorId) {
        log.info("[创建家族] name={}, creatorId={}", name, creatorId);

        Family family = new Family();
        family.setName(name);
        family.setDescription(description);
        family.setAvatar(avatar);
        family.setCreatorId(creatorId);
        family.setAdministratorId(creatorId);
        family.setCreatedAt(new Date());

        Family savedFamily = familyRepository.save(family);
        log.info("[创建家族成功] familyId={}", savedFamily.getId());

        return savedFamily;
    }

    public List<Family> getFamiliesByCreatorId(Long creatorId) {
        log.debug("[获取家族列表] creatorId={}", creatorId);
        return familyRepository.findByCreatorId(creatorId);
    }

    public Family getFamilyById(Long familyId) {
        log.debug("[获取家族详情] familyId={}", familyId);
        return familyRepository.findById(familyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FAMILY_NOT_FOUND, String.valueOf(familyId)));
    }

    @Transactional
    public Family updateFamily(Long familyId, String name, String description, String avatar) {
        log.info("[更新家族] familyId={}, name={}", familyId, name);

        Family family = getFamilyById(familyId);

        if (name != null) {
            if (name.length() > 100) {
                throw new BusinessException(ErrorCode.FAMILY_NAME_TOO_LONG);
            }
            family.setName(name);
        }
        if (description != null) {
            if (description.length() > 2000) {
                throw new BusinessException(ErrorCode.FAMILY_DESCRIPTION_TOO_LONG);
            }
            family.setDescription(description);
        }
        if (avatar != null) {
            family.setAvatar(avatar);
        }

        Family updatedFamily = familyRepository.save(family);
        log.info("[更新家族成功] familyId={}", familyId);

        return updatedFamily;
    }

    @Transactional
    public void deleteFamily(Long familyId) {
        log.info("[删除家族] familyId={}", familyId);

        if (!familyRepository.existsById(familyId)) {
            throw new BusinessException(ErrorCode.FAMILY_NOT_FOUND, String.valueOf(familyId));
        }

        familyRepository.deleteById(familyId);
        log.info("[删除家族成功] familyId={}", familyId);
    }

    @Transactional
    public Family updateAdministrator(Long familyId, Long administratorId) {
        log.info("[更新管理员] familyId={}, newAdminId={}", familyId, administratorId);

        Family family = getFamilyById(familyId);
        family.setAdministratorId(administratorId);

        Family updatedFamily = familyRepository.save(family);
        log.info("[更新管理员成功] familyId={}, newAdminId={}", familyId, administratorId);

        return updatedFamily;
    }

    public boolean isAdministrator(Long familyId, Long userId) {
        log.debug("[检查管理员权限] familyId={}, userId={}", familyId, userId);
        Family family = getFamilyById(familyId);
        Long adminId = family.getAdministratorId();
        if (adminId == null) {
            // 如果管理员ID为空，允许创建者进行操作
            return family.getCreatorId().equals(userId);
        }
        return adminId.equals(userId);
    }
}