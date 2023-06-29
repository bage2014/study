package com.bage.study.best.practice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/ping")
@RestController
@Slf4j
public class PingController {

    @RequestMapping("/ping")
    public Object query(@RequestParam("phone") String phone) {
        log.info("UserController query users = {}", phone);
        return 1;
    }


}
