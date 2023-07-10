package com.bage.study.best.practice.controller;

import com.bage.study.best.practice.cache.CacheService;
import com.bage.study.best.practice.cache.JvmCacheImpl;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.service.UserMockService;
import com.github.jsonzou.jmockdata.JMockData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cache")
@RestController
@Slf4j
public class CacheController {

    @Autowired
    private UserMockService userMockService;
    @Autowired
    private CacheService cacheService;

    @RequestMapping("/insert")
    public Object query() {
        User user = userMockService.mockOne();
        log.debug("CacheController insert user = {}", user);
        user.setId(JMockData.mock(Long.class));
        cacheService.cache(user.getId() + "", user);
        log.info("CacheController insert insert = {}", user.getId());
        return 1;
    }


    @RequestMapping("/size")
    public Object size() {
//        long size = cacheService.size();
//        log.info("CacheController size = {}", size);
        return 0;
    }


}
