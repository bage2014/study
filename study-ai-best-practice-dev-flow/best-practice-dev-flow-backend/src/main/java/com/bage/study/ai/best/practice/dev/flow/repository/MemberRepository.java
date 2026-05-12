package com.bage.study.ai.best.practice.dev.flow.repository;

import com.bage.study.ai.best.practice.dev.flow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByFamilyId(Long familyId);

    List<Member> findByFamilyIdAndDeletedFalse(Long familyId);

    List<Member> findByPhone(String phone);

    List<Member> findByEmail(String email);

    List<Member> findByNameContaining(String name);

    List<Member> findByNameContainingAndFamilyId(String name, Long familyId);

    long countByFamilyIdAndDeletedFalse(Long familyId);
}