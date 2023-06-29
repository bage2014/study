package com.bage.cache.memory.springboot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @RequestMapping("/ping")
    public Object query(@RequestParam("phone") String phone) {
        System.out.println("UserController query users = " + phone);
        return phone;
    }


}
