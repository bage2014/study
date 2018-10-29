package com.bage.study.sso.baeldung.client1;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/user/me")
    public String user(Principal principal) {
        System.out.println(principal);
        return principal.getName();
    }
}
