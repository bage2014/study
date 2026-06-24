package com.bage.study.grayvalidator.demo;

import com.bage.study.grayvalidator.adapter.aop.GrayRoute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    @GrayRoute
    @GetMapping("/users")
    public DemoResponse getUser(@RequestParam String uid) {
        return DemoResponse.original("user:" + uid);
    }

    @GrayRoute
    @GetMapping("/orders")
    public DemoResponse getOrder(@RequestParam String orderId) {
        return DemoResponse.original("order:" + orderId);
    }

    @GrayRoute
    @GetMapping("/profile")
    public DemoResponse getProfile(
            @RequestHeader(value = "X-Gray-User", defaultValue = "anonymous") String userId) {
        return DemoResponse.original("profile:" + userId);
    }

    @GrayRoute
    @PostMapping("/events")
    public DemoResponse publishEvent(@RequestParam String uid,
                                     @RequestBody(required = false) Object payload) {
        return DemoResponse.original("event:uid=" + uid);
    }
}