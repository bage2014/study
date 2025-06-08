package com.bage.study.best.practice.trial;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.bage.study.best.practice.metrics.LogCounterMetrics;
import com.bage.study.best.practice.metrics.MetricService;
import com.bage.study.best.practice.rest.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/limit")
@RestController
@Slf4j
public class LimitController {

    @RequestMapping("/query")
    public Object query(@RequestParam("phone") String phone) {
        log.info("UserController query users = {}", phone);
        return new RestResult(200, "OK");
    }

    @RequestMapping("/insert")
    public Object insert() {
        int resultCode = 0;
        String msg = "Success";
        try (Entry entry = SphU.entry("limit")) {
            // 被保护的逻辑
            try {
                long end = System.currentTimeMillis();
                log.info("LimitController insert when = {}", (end));
                resultCode = 200;
            } catch (Exception e) {
                resultCode = 300;
                msg = e.getMessage();
            }

        } catch (BlockException ex) {
            // 处理被流控的逻辑
            log.warn("block");
            resultCode = 600;
            msg = "limit block";
        }
        return new RestResult(resultCode, msg);
    }

}
