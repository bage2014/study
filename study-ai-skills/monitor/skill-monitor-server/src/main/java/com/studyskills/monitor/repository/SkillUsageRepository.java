package com.studyskills.monitor.repository;

import com.studyskills.monitor.entity.SkillUsage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface SkillUsageRepository extends JpaRepository<SkillUsage, Long> {

    /** 通用过滤片段：时间范围 + 可选用户 + 可选项目（null 表示不过滤） */
    String FILTER = "u.ts BETWEEN :start AND :end " +
            "AND (:user IS NULL OR u.userId = :user) " +
            "AND (:project IS NULL OR u.projectName = :project) ";

    /** 过滤后的全部记录（overview 聚合用，本地数据量小） */
    @Query("SELECT u FROM SkillUsage u WHERE " + FILTER + "ORDER BY u.ts ASC")
    List<SkillUsage> findFiltered(@Param("start") Instant start, @Param("end") Instant end,
                                  @Param("user") String user, @Param("project") String project);

    /** 按日期分组（整体每日趋势） */
    @Query("SELECT FUNCTION('FORMATDATETIME', u.ts, 'yyyy-MM-dd'), COUNT(u) " +
           "FROM SkillUsage u WHERE " + FILTER +
           "GROUP BY FUNCTION('FORMATDATETIME', u.ts, 'yyyy-MM-dd') ORDER BY 1 ASC")
    List<Object[]> dailyTotalTrend(@Param("start") Instant start, @Param("end") Instant end,
                                   @Param("user") String user, @Param("project") String project);

    /** 触发方式分布 */
    @Query("SELECT u.triggerType, COUNT(u) FROM SkillUsage u WHERE " + FILTER +
           "GROUP BY u.triggerType")
    List<Object[]> triggerDistribution(@Param("start") Instant start, @Param("end") Instant end,
                                       @Param("user") String user, @Param("project") String project);

    /** 客户端分布 */
    @Query("SELECT u.client, COUNT(u) FROM SkillUsage u WHERE " + FILTER +
           "GROUP BY u.client")
    List<Object[]> clientDistribution(@Param("start") Instant start, @Param("end") Instant end,
                                      @Param("user") String user, @Param("project") String project);

    /** Top N Skill */
    @Query("SELECT u.skillName, COUNT(u) as cnt FROM SkillUsage u WHERE " + FILTER +
           "GROUP BY u.skillName ORDER BY cnt DESC")
    List<Object[]> topSkills(@Param("start") Instant start, @Param("end") Instant end,
                             @Param("user") String user, @Param("project") String project, Pageable pageable);

    /** Top N 项目（可按用户过滤） */
    @Query("SELECT u.projectName, COUNT(u) as cnt, COUNT(DISTINCT u.skillName) FROM SkillUsage u " +
           "WHERE u.ts BETWEEN :start AND :end AND u.projectName IS NOT NULL " +
           "AND (:user IS NULL OR u.userId = :user) " +
           "GROUP BY u.projectName ORDER BY cnt DESC")
    List<Object[]> topProjects(@Param("start") Instant start, @Param("end") Instant end,
                               @Param("user") String user, Pageable pageable);

    /** Top N 用户（可按项目过滤） */
    @Query("SELECT u.userId, COUNT(u) as cnt, COUNT(DISTINCT u.projectName) FROM SkillUsage u " +
           "WHERE u.ts BETWEEN :start AND :end " +
           "AND (:project IS NULL OR u.projectName = :project) " +
           "GROUP BY u.userId ORDER BY cnt DESC")
    List<Object[]> topUsers(@Param("start") Instant start, @Param("end") Instant end,
                            @Param("project") String project, Pageable pageable);

    /** 最新记录（可选用户/项目过滤，时间倒序） */
    @Query("SELECT u FROM SkillUsage u " +
           "WHERE (:user IS NULL OR u.userId = :user) " +
           "AND (:project IS NULL OR u.projectName = :project) " +
           "ORDER BY u.ts DESC")
    List<SkillUsage> recentFiltered(@Param("user") String user, @Param("project") String project, Pageable pageable);

    /** 全部去重用户（下拉选项） */
    @Query("SELECT DISTINCT u.userId FROM SkillUsage u WHERE u.userId IS NOT NULL ORDER BY 1")
    List<String> distinctUsers();

    /** 全部去重项目（下拉选项） */
    @Query("SELECT DISTINCT u.projectName FROM SkillUsage u WHERE u.projectName IS NOT NULL ORDER BY 1")
    List<String> distinctProjects();

    /** 检查去重键是否已存在 */
    boolean existsByDedupKey(String dedupKey);
}
