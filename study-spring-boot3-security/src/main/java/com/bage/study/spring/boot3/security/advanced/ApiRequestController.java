
package com.bage.study.spring.boot3.security.advanced;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiRequestController {

    @RequestMapping("/api/user/hello")
    public Object roleUser(){
        return "hello user";
    }

    @RequestMapping("/api/admin/hello")
    public Object roleAdmin(){
        return "hello admin";
    }

    @RequestMapping("/api/anonymous/hello")
    public Object anonymous(){
        return "hello anonymous";
    }

}