package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.AppFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppFileRepository extends JpaRepository<AppFile, Long> {
    
    // 分页获取文件列表
    Page<AppFile> findAll(Pageable pageable);
    
    // 根据文件名分页搜索文件
    @Query("SELECT f FROM AppFile f WHERE f.fileName LIKE %:keyword% OR f.originalFileName LIKE %:keyword%")
    Page<AppFile> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // 根据文件类型查找文件
    Page<AppFile> findByFileType(String fileType, Pageable pageable);
}