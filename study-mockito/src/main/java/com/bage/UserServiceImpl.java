package com.bage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private FileService fileService;

    public String hello() {
        return "UserServiceImpl";
    }

    public String append() {
        return hello() + ":" + fileService.hello();
    }

}
