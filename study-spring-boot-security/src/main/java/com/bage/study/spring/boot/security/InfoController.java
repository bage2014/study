package com.bage.study.spring.boot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/admin/refresh")
    public String refresh(){
        return "PreAuthorize";
    }


    @RequestMapping("/user/hello")
    public String user(){
        return "user";
    }

    @RequestMapping("/admin/hello")
    public String admin(){
        return "admin";
    }

    @RequestMapping("/db/hello")
    public String dba(){
        return "dba";
    }
}
