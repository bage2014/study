package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.AppVersion;
import com.bage.my.app.end.point.repository.AppVersionRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
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
        // 参数验证和默认值设置
        if (page < 0) {
            log.warn("页码不能为负数，已自动设置为0。输入页码: {}", page);
            page = 0;
        }
        
        if (size <= 0) {
            log.warn("每页大小必须大于0，已自动设置为10。输入大小: {}", size);
            size = 10;
        }
        
        // 限制最大页面大小，防止内存溢出
        int maxPageSize = 100;
        if (size > maxPageSize) {
            log.warn("每页大小超过最大限制{}，已自动设置为最大值。输入大小: {}", maxPageSize, size);
            size = maxPageSize;
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<AppVersion> result = appVersionRepository.findAll(pageable);
        
        log.debug("分页查询结果 - 页码: {}, 每页大小: {}, 总记录数: {}, 总页数: {}", 
            page, size, result.getTotalElements(), result.getTotalPages());
        
        return result;
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
            saveVersion(new AppVersion("1.0.0", LocalDate.now().plusDays(-20), "初始版本", "", false, "file1"));
            saveVersion(new AppVersion("1.1.0", LocalDate.now().plusDays(-10), "修复了登录问题", "", false, "file2"));
            saveVersion(new AppVersion("1.2.0", LocalDate.now().plusDays(-8), "新增功能", "", false, "file3"));
            saveVersion(new AppVersion("2.0.0", LocalDate.now().plusDays(-2), "全新UI设计", "", true, "file4"));
            saveVersion(new AppVersion("2.1.0", LocalDate.now(), "性能优化", "", false, "file5"));
        }
        log.info("Initialized default versions count: {}", appVersionRepository.count());
    }
}