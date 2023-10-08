package com.bage.study.gc.jdk21;

import com.bage.study.gc.biz.GcSafePointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * https://zhuanlan.zhihu.com/p/286110609
 * <p>
 * <p>
 * -XX:+PrintSafepointStatistics  等待1分钟停止程序
 * <p>
 * -XX:+PrintGCDetails 每次GC发生时打印GC详细信息
 */
@RequestMapping("/gc/safe/point")
@RestController
@Slf4j
public class GcSafePointController {

    @Autowired
    private GcSafePointService service;

    @RequestMapping("/start")
    public Object start(@RequestParam(value = "count", required = false) Long count) throws InterruptedException {
        return service.start();
    }
}
