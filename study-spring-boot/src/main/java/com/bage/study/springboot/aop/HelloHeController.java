package com.bage.study.springboot.aop;

import com.bage.study.springboot.aop.annotation.log.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloHeController {

    @RequestMapping("/hello/param")
    @ResponseBody
    @Logger
    public String hello(@RequestParam("param") String param) throws Exception {
        System.out.println("hello param = " + param);
        return "result:" + param;
    }
}
