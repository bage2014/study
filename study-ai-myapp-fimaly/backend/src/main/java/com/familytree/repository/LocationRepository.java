package com.familytree.repository;

import com.familytree.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByMemberId(Long memberId);
    List<Location> findByMemberIdAndIsSharedTrue(Long memberId);
    List<Location> findByMemberIdAndIsPrimaryTrue(Long memberId);
}
