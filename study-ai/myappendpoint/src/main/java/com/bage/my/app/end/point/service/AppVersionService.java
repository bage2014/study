package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.AppVersion;
import com.bage.my.app.end.point.repository.AppVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppVersionService {
    @Autowired
    private AppVersionRepository appVersionRepository;

    @Transactional
    public AppVersion saveVersion(AppVersion version) {
        return appVersionRepository.save(version);
    }

    public List<AppVersion> getAllVersions() {
        return appVersionRepository.findAll();
    }

    public AppVersion getLatestVersion() {
        return appVersionRepository.findTopByOrderByVersionDesc();
    }
    
    // 分页获取版本列表
    public Page<AppVersion> getVersionsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appVersionRepository.findAll(pageable);
    }
    
    // 根据关键词分页搜索版本
    public Page<AppVersion> searchVersionsByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appVersionRepository.findByKeyword(keyword, pageable);
    }

    @Transactional
    public void initDefaultVersions() {
        // 初始化5个版本
        if (appVersionRepository.count() == 0) {
            saveVersion(new AppVersion("1.0.0", LocalDate.now().plusDays(-20), "初始版本", "", false));
            saveVersion(new AppVersion("1.1.0", LocalDate.now().plusDays(-10), "修复了登录问题", "", false));
            saveVersion(new AppVersion("1.2.0", LocalDate.now().plusDays(-8), "新增功能", "", false));
            saveVersion(new AppVersion("2.0.0", LocalDate.now().plusDays(-2), "全新UI设计", "", true));
            saveVersion(new AppVersion("2.1.0", LocalDate.now(), "性能优化", "", false));
        }
    }
}