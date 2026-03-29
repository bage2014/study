package com.familytree.repository;

import com.familytree.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByFamilyId(Long familyId);
    List<Media> findByFamilyIdAndType(Long familyId, String type);
}