package com.bage.study.gc.jdk21;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/hello")
@RestController
@Slf4j
public class HelloController {

    @RequestMapping("/world")
    public Object gc(@PathVariable(value = "world", required = false) String world) {
        return "world" + world;
    }


}
