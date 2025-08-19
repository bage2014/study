package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.AppVersion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
    AppVersion findTopByOrderByVersionDesc();

    // 分页获取版本列表
    Page<AppVersion> findAll(Pageable pageable);

    // 根据关键词分页搜索版本
    Page<AppVersion> findByKeyword(String keyword, Pageable pageable);

}