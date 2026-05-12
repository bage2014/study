package com.bage.study.ai.best.practice.dev.flow.service;

import com.bage.study.ai.best.practice.dev.flow.dto.request.FamilyRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.FamilyDTO;

import java.util.List;

public interface FamilyService {

    FamilyDTO createFamily(Long userId, FamilyRequest request);

    FamilyDTO updateFamily(Long familyId, FamilyRequest request);

    void deleteFamily(Long familyId);

    FamilyDTO getFamilyById(Long familyId);

    List<FamilyDTO> getFamiliesByUser(Long userId);

    void transferAdministrator(Long familyId, Long newAdminId);
}