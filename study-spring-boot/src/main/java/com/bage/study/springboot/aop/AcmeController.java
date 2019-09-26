package com.bage.study.springboot.aop;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AcmeController {

    @RequestMapping("/hello/exception")
    public void hello() throws Exception {
        System.out.println("hello exception");
        throw  new Exception("test exception");
    }
}
