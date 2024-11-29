package com.bage.study.springboot.event;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@RestController
public class HelloEventController {

    @RequestMapping("/hello/event")
    @ResponseBody
    public void hello() throws Exception {
        System.out.println("hello event");
        throw  new Exception("test event");
    }
    @RequestMapping("/hello/event2")
    @ResponseBody
    public String hello2() throws Exception {
        System.out.println("hello event2");
        return "hhhh";
    }
}
