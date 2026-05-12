package com.bage.study.ai.best.practice.dev.flow.service;

import com.bage.study.ai.best.practice.dev.flow.dto.response.FamilyTreeDTO;

public interface FamilyTreeService {

    FamilyTreeDTO getFamilyTree(Long familyId);

    FamilyTreeDTO getFamilyTreeByMember(Long memberId);
}