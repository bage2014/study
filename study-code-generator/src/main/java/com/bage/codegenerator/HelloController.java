package com.bage.codegenerator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/{msg}")
    public Object all(@PathVariable(required = false, value = "msg") String msg) {
        return String.format("hello %s, welcome to " + getClass().getName(), msg == null ? "world" : msg);
    }

}
