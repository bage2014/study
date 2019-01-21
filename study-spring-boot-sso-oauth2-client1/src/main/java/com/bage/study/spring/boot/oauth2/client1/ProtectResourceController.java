package com.bage.study.spring.boot.oauth2.client1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class ProtectResourceController {

    @RequestMapping("/user")
    @ResponseBody
    public Principal user(Principal principal) {
        return principal;
    }


    @RequestMapping("/protectresource")
    public String protectresource() {
        return "protectresource";
    }

}
