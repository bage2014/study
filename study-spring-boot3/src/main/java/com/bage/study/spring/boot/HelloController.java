package com.bage.study.spring.boot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/wrapper")
    public String wrapper(HttpServletRequest request, HttpServletResponse response) {
        return String.format("Hello %s!", request.getAttribute("X-Signature-request")) +
                String.format("Hello %s!", response.getHeaders("X-Signature"))
                ;
    }
}