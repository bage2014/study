package com.bage.study.ai.repository;

import com.bage.study.ai.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    List<SearchHistory> findByKeywordContainingOrderByCreatedAtDesc(String keyword);

    List<SearchHistory> findTop10ByOrderByCreatedAtDesc();

    void deleteByCreatedAtBefore(LocalDateTime dateTime);
}