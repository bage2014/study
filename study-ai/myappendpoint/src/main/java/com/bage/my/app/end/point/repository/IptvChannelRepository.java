package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.IptvChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IptvChannelRepository extends JpaRepository<IptvChannel, Integer> {
    
    // 根据标签查询频道
    List<IptvChannel> findByTagsContaining(String tag);
    
    // 根据分组查询频道
    List<IptvChannel> findByCategory(String category);
    
    // 根据名称包含关键词查询
    List<IptvChannel> findByNameContaining(String keyword);
    
    // 根据是否包含中文字符查询(可以通过数据库函数实现，这里先定义)
    List<IptvChannel> findByCategoryContaining(String keyword);
}