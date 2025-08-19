package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.AppVersion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
    AppVersion findTopByOrderByVersionDesc();

    // 分页获取版本列表
    Page<AppVersion> findAll(Pageable pageable);

    // 根据关键词分页搜索版本 - 修改为使用实际存在的字段
    @Query("SELECT v FROM AppVersion v WHERE v.version LIKE %:keyword% OR v.releaseNotes LIKE %:keyword%")
    Page<AppVersion> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}