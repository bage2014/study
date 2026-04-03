package com.familytree.repository;

import com.familytree.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByFamilyId(Long familyId);
    List<Member> findByPhone(String phone);
    List<Member> findByEmail(String email);
    List<Member> findByPhoneAndEmail(String phone, String email);
}