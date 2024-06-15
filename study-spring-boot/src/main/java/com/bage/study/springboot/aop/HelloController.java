package com.bage.study.springboot.aop;

import com.bage.study.springboot.aop.annotation.rest.RestResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RestResult
    @RequestMapping("/hello")
    public Object hello(){
        return "hello";
    }

}