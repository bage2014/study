package com.bage.study.best.practice.trial;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RequestMapping("/redis")
@RestController
@Slf4j
public class RedisController {


    @RequestMapping("/write")
    public Object start(@RequestParam(value = "thread", required = false) Integer threadNumber, @RequestParam(value = "count", required = false) Integer count) {
        return "start";
    }

}
