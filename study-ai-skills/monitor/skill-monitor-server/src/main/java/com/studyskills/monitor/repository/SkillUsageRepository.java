package com.studyskills.monitor.repository;

import com.studyskills.monitor.entity.SkillUsage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface SkillUsageRepository extends JpaRepository<SkillUsage, Long> {

    /** 指定时间范围内的记录 */
    List<SkillUsage> findByTsBetweenOrderByTsAsc(Instant start, Instant end);

    /** 按 Skill 分组统计调用次数（指定时间范围） */
    @Query("SELECT u.skillName, COUNT(u) as cnt, AVG(u.durationMs) as avgDur, SUM(CASE WHEN u.status = 'error' THEN 1 ELSE 0 END) as errCnt " +
           "FROM SkillUsage u WHERE u.ts BETWEEN :start AND :end " +
           "GROUP BY u.skillName ORDER BY cnt DESC")
    List<Object[]> countBySkillName(@Param("start") Instant start, @Param("end") Instant end);

    /** 按日期 + Skill 分组（每日趋势） */
    @Query("SELECT FUNCTION('FORMATDATETIME', u.ts, 'yyyy-MM-dd'), u.skillName, COUNT(u) " +
           "FROM SkillUsage u WHERE u.ts BETWEEN :start AND :end " +
           "GROUP BY FUNCTION('FORMATDATETIME', u.ts, 'yyyy-MM-dd'), u.skillName " +
           "ORDER BY 1 ASC, 3 DESC")
    List<Object[]> dailySkillTrend(@Param("start") Instant start, @Param("end") Instant end);

    /** 按日期分组（整体每日趋势） */
    @Query("SELECT FUNCTION('FORMATDATETIME', u.ts, 'yyyy-MM-dd'), COUNT(u) " +
           "FROM SkillUsage u WHERE u.ts BETWEEN :start AND :end " +
           "GROUP BY FUNCTION('FORMATDATETIME', u.ts, 'yyyy-MM-dd') " +
           "ORDER BY 1 ASC")
    List<Object[]> dailyTotalTrend(@Param("start") Instant start, @Param("end") Instant end);

    /** 触发方式分布 */
    @Query("SELECT u.triggerType, COUNT(u) FROM SkillUsage u " +
           "WHERE u.ts BETWEEN :start AND :end " +
           "GROUP BY u.triggerType")
    List<Object[]> triggerDistribution(@Param("start") Instant start, @Param("end") Instant end);

    /** 客户端分布 */
    @Query("SELECT u.client, COUNT(u) FROM SkillUsage u " +
           "WHERE u.ts BETWEEN :start AND :end " +
           "GROUP BY u.client")
    List<Object[]> clientDistribution(@Param("start") Instant start, @Param("end") Instant end);

    /** Top N Skill */
    @Query("SELECT u.skillName, COUNT(u) as cnt FROM SkillUsage u " +
           "WHERE u.ts BETWEEN :start AND :end " +
           "GROUP BY u.skillName ORDER BY cnt DESC")
    List<Object[]> topSkills(@Param("start") Instant start, @Param("end") Instant end, Pageable pageable);

    /** 检查去重键是否已存在 */
    boolean existsByDedupKey(String dedupKey);

    /** 统计指定时间范围的记录数 */
    long countByTsBetween(Instant start, Instant end);
}
