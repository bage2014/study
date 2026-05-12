package com.bage.study.ai.best.practice.dev.flow.service;

import com.bage.study.ai.best.practice.dev.flow.dto.request.MemberRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.MemberDTO;

import java.util.List;

public interface MemberService {

    MemberDTO createMember(MemberRequest request);

    MemberDTO updateMember(Long memberId, MemberRequest request);

    void deleteMember(Long memberId);

    MemberDTO getMemberById(Long memberId);

    List<MemberDTO> getMembersByFamily(Long familyId);

    List<MemberDTO> searchMembers(String keyword);
}