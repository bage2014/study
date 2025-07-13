package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
}