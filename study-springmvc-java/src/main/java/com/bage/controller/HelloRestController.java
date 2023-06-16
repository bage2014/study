package com.bage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloRestController {

    @RequestMapping("/world")
    public String index() {
        System.out.println("index");
        return "index";
    }

}
