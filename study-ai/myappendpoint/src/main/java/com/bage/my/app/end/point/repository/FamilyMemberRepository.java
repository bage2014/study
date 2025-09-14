package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.FamilyMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    // 新增：根据关键词分页查询成员
    @Query("SELECT f FROM FamilyMember f WHERE " +
           "(f.name LIKE %:keyword% OR " +
           "f.birthPlace LIKE %:keyword% OR " +
           "f.occupation LIKE %:keyword% OR " +
           "f.education LIKE %:keyword% OR " +
           "f.contactInfo LIKE %:keyword%)")
    Page<FamilyMember> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}