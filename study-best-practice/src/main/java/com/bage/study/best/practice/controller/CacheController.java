package com.bage.study.best.practice.controller;

import com.bage.study.best.practice.cache.LocalCache;
import com.bage.study.best.practice.metrics.UserCounterMetrics;
import com.bage.study.best.practice.metrics.UserTimerMetrics;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.rest.RestResult;
import com.bage.study.best.practice.service.UserMockService;
import com.bage.study.best.practice.service.UserService;
import com.github.jsonzou.jmockdata.JMockData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequestMapping("/cache")
@RestController
@Slf4j
public class CacheController {

    @Autowired
    private UserMockService userMockService;
    @Autowired
    private LocalCache localCache;

    @RequestMapping("/insert")
    public Object query() {
        User user = userMockService.mockOne();
        log.debug("CacheController insert user = {}", user);
        user.setId(JMockData.mock(Long.class));
        localCache.cache(user.getId() + "", user);
        log.info("CacheController insert insert = {}", user.getId());
        return 1;
    }


    @RequestMapping("/size")
    public Object size() {
        long size = localCache.size();
        log.info("CacheController size = {}", size);
        return size;
    }


}
