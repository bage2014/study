package com.bage.study.loki;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        String result = String.format("Hello %s!", name);
        log.info("traceId = {}, result = {}", traceId, result);
        MDC.clear();
        return result;
    }
}