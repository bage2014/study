package com.bage.study.best.practice.repo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * 自定义UserMapper，开启二级缓存
 */
@CacheNamespace(size = 512, flushInterval = 60000) // 缓存大小512，60秒刷新
public interface UserMapper extends BaseMapper<UserEntity> {

}
