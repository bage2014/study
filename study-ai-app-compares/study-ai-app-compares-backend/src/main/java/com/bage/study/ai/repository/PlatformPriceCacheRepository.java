package com.bage.study.ai.repository;

import com.bage.study.ai.entity.PlatformPriceCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlatformPriceCacheRepository extends JpaRepository<PlatformPriceCache, Long> {

    List<PlatformPriceCache> findByKeywordAndPlatform(String keyword, String platform);

    List<PlatformPriceCache> findByKeyword(String keyword);

    List<PlatformPriceCache> findByExpireTimeBefore(LocalDateTime dateTime);

    void deleteByExpireTimeBefore(LocalDateTime dateTime);
}