package com.bage.study.spring.boot.starter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Hello2Controller {

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/wrapper")
    public Object wrapper(HttpServletRequest request, HttpServletResponse response) {
        String hhh = String.format("Hello request = %s!", request.getAttribute("X-Signature-request")) +
                String.format("Hello response = %s!", response.getHeaders("X-Signature"))
                ;
        System.out.println(hhh);
        return hhh;
    }
}