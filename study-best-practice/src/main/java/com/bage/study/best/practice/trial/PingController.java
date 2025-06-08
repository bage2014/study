package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.rest.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RequestMapping("/ping")
@RestController
@Slf4j
public class PingController {


    @RequestMapping("/")
    public Object random() {

        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        log.info("PingController ping cost = {}", (end - start));
        return new RestResult(200, "pang");
    }

}
