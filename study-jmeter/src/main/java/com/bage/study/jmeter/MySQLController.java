package com.bage.study.jmeter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/mysql")
public class MySQLController {


    @Resource
    private CustomerRepo repo;

    @RequestMapping("/query")
    public @ResponseBody
    Object query() {
        System.out.println(LocalDateTime.now());
        return repo.query();
    }

    @RequestMapping("/init")
    public @ResponseBody
    String init() {
        repo.init();
        System.out.println(LocalDateTime.now());
        return "pang";
    }

}
