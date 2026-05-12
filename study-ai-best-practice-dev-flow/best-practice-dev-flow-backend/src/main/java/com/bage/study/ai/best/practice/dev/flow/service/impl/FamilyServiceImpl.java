package com.bage.study.ai.best.practice.dev.flow.service.impl;

import com.bage.study.ai.best.practice.dev.flow.dto.request.FamilyRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.FamilyDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.Family;
import com.bage.study.ai.best.practice.dev.flow.entity.User;
import com.bage.study.ai.best.practice.dev.flow.exception.BusinessException;
import com.bage.study.ai.best.practice.dev.flow.exception.ResourceNotFoundException;
import com.bage.study.ai.best.practice.dev.flow.repository.FamilyRepository;
import com.bage.study.ai.best.practice.dev.flow.repository.MemberRepository;
import com.bage.study.ai.best.practice.dev.flow.repository.UserRepository;
import com.bage.study.ai.best.practice.dev.flow.service.FamilyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FamilyServiceImpl implements FamilyService {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    @Override
    public FamilyDTO createFamily(Long userId, FamilyRequest request) {
        log.info("创建家族: userId={}, name={}", userId, request.getName());

        Family family = new Family();
        family.setName(request.getName());
        family.setDescription(request.getDescription());
        family.setAvatar(request.getAvatar());
        family.setCreatorId(userId);
        family.setAdministratorId(userId);

        Family savedFamily = familyRepository.save(family);
        log.info("家族创建成功: familyId={}", savedFamily.getId());

        return convertToDTO(savedFamily);
    }

    @Override
    public FamilyDTO updateFamily(Long familyId, FamilyRequest request) {
        log.info("更新家族: familyId={}", familyId);

        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("家族不存在"));

        if (request.getName() != null) {
            family.setName(request.getName());
        }
        if (request.getDescription() != null) {
            family.setDescription(request.getDescription());
        }
        if (request.getAvatar() != null) {
            family.setAvatar(request.getAvatar());
        }

        Family updatedFamily = familyRepository.save(family);
        log.info("家族更新成功: familyId={}", updatedFamily.getId());

        return convertToDTO(updatedFamily);
    }

    @Override
    public void deleteFamily(Long familyId) {
        log.info("删除家族: familyId={}", familyId);

        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("家族不存在"));

        family.setDeleted(1);
        familyRepository.save(family);
        log.info("家族删除成功: familyId={}", familyId);
    }

    @Override
    public FamilyDTO getFamilyById(Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("家族不存在"));
        return convertToDTO(family);
    }

    @Override
    public List<FamilyDTO> getFamiliesByUser(Long userId) {
        return familyRepository.findByCreatorId(userId).stream()
                .filter(f -> f.getDeleted() == 0)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void transferAdministrator(Long familyId, Long newAdminId) {
        log.info("转让家族管理员: familyId={}, newAdminId={}", familyId, newAdminId);

        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("家族不存在"));

        if (!userRepository.existsById(newAdminId)) {
            throw new ResourceNotFoundException("新管理员不存在");
        }

        family.setAdministratorId(newAdminId);
        familyRepository.save(family);
        log.info("家族管理员转让成功: familyId={}", familyId);
    }

    private FamilyDTO convertToDTO(Family family) {
        FamilyDTO dto = new FamilyDTO();
        dto.setId(family.getId());
        dto.setName(family.getName());
        dto.setDescription(family.getDescription());
        dto.setAvatar(family.getAvatar());
        dto.setCreatorId(family.getCreatorId());
        dto.setAdministratorId(family.getAdministratorId());
        dto.setCreatedAt(family.getCreatedAt());

        User creator = userRepository.findById(family.getCreatorId()).orElse(null);
        if (creator != null) {
            dto.setCreatorName(creator.getUsername());
        }

        long memberCount = memberRepository.countByFamilyIdAndDeletedFalse(family.getId());
        dto.setMemberCount((int) memberCount);

        return dto;
    }
}