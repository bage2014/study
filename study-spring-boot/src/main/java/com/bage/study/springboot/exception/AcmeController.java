package com.bage.study.springboot.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@RestController
public class AcmeController {

    @RequestMapping("/hello/exception")
    @ResponseBody
    public void hello() throws Exception {
        System.out.println("hello exception");
        throw  new Exception("test exception");
    }
}
