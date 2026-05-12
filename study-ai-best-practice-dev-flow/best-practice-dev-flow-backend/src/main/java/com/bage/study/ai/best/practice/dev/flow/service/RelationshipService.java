package com.bage.study.ai.best.practice.dev.flow.service;

import com.bage.study.ai.best.practice.dev.flow.dto.request.RelationshipRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.RelationshipDTO;

import java.util.List;

public interface RelationshipService {

    RelationshipDTO createRelationship(RelationshipRequest request);

    void deleteRelationship(Long relationshipId);

    List<RelationshipDTO> getRelationshipsByMember(Long memberId);

    List<RelationshipDTO> getRelationshipsByFamily(Long familyId);
}