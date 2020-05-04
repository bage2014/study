package com.bage.study.springboot.restresponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@RestController
public class AcmeController {

    @RequestMapping("/hello/exception")
    @ResponseBody
    public Object hello() {
        return new Helloj("test exception");
    }

    class Helloj{
        private String hello;

        public Helloj(String hello) {
            this.hello = hello;
        }

        public String getHello() {
            return hello;
        }

        public void setHello(String hello) {
            this.hello = hello;
        }
    }
}
