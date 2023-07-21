package com.bage.study.springboot.inject.circur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
//@Scope("prototype")
public class BService {
    @Autowired
    AService aService;
//    public BService(AService aService) {
//        this.aService = aService;
//    }
    @Async
    public void hello() {

    }
}