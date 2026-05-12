package com.bage.study.ai.best.practice.dev.flow.service;

import com.bage.study.ai.best.practice.dev.flow.dto.request.MemberMilestoneRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.MemberMilestoneDTO;

import java.util.List;

public interface MemberMilestoneService {

    MemberMilestoneDTO createMilestone(MemberMilestoneRequest request);

    MemberMilestoneDTO updateMilestone(Long milestoneId, MemberMilestoneRequest request);

    void deleteMilestone(Long milestoneId);

    MemberMilestoneDTO getMilestoneById(Long milestoneId);

    List<MemberMilestoneDTO> getMilestonesByMember(Long memberId);
}