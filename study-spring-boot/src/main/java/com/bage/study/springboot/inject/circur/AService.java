package com.bage.study.springboot.inject.circur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
//@Scope("prototype")
public class AService {
    @Autowired
    BService bService;

//    public AService(BService bService) {
//        this.bService = bService;
//    }

    @Async
    public void hello() {

    }
}
