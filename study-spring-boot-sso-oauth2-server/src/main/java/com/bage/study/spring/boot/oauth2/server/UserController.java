package com.bage.study.spring.boot.oauth2.server;


import java.security.Principal;
import java.util.List;

import com.bage.study.spring.boot.oauth2.temp.user.User;
import com.bage.study.spring.boot.oauth2.temp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

//    @Autowired
//    UserService userService;

    @RequestMapping("/user/me")
    public Principal user(Principal principal) {
        System.out.println(principal);
        return principal;
    }


//    @RequestMapping("/org/all")
//    public List<User> all() {
//        return userService.queryAll();
//    }

}
