package com.bage.study.spring.boot.oauth2.server.v3;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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
