package com.bage.study.gc.jdk21;

import com.bage.study.gc.biz.GoodsSecKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * wrk -t10 -c100 -d5s -T3s --latency http://localhost:8000/goods/buy
 */
@RequestMapping("/goods")
@RestController
@Slf4j
public class GoodsSecKillController {

    @Autowired
    private GoodsSecKillService service;

    @RequestMapping("/buy")
    public Object buy(@RequestParam(value = "userId", required = false) Long userId) {
        return service.buy(userId);
    }


    @RequestMapping("/query/all")
    public Object queryAll() {
        return service.queryAll();
    }


}
