package com.bage.study.jmeter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Random;

@RestController
@RequestMapping("/mysql")
public class MySQLController {

    Random random = new Random();
    @Resource
    private CustomerRepo repo;

    @RequestMapping("/query")
    public @ResponseBody
    Object query() {
        String key = String.valueOf(System.currentTimeMillis()  - random.nextInt());
        System.out.println(LocalDateTime.now() + "; " + key);
        return repo.query(key);
    }

    @RequestMapping("/init")
    public @ResponseBody
    String init() {
        repo.init();
        System.out.println(LocalDateTime.now());
        return "pang";
    }

}
