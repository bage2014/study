package com.bage.study.spring.boot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RestController
public class InfoController {

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/admin/refresh")
    public String refresh(){
        return "PreAuthorize";
    }

    @Autowired
    MyDefaultFilterInvocationSecurityMetadataSource myDefaultFilterInvocationSecurityMetadataSource;

    @RequestMapping("/config/add")
    public Object add(){

        Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = myDefaultFilterInvocationSecurityMetadataSource.getRequestMap();

        RequestMatcher key = new AntPathRequestMatcher("/bage/*");
        Collection<ConfigAttribute> value = new ArrayList<>();
        ConfigAttribute e = new MyConfigAttribute("ROLE_BAGE");
        value.add(e);
        requestMap.put(key,value);
        // requestMap.put("","Ant [pattern='/']");

        return requestMap;
    }

    @RequestMapping("/config/all")
    public Object all(){

        Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = myDefaultFilterInvocationSecurityMetadataSource.getRequestMap();

        // requestMap.put("","Ant [pattern='/']");

        return requestMap;
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

    @RequestMapping("/bage/hello")
    public String bage(){
        return "bage";
    }
}
