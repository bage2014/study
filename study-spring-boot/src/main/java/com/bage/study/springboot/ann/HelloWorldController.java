package com.bage.study.springboot.ann;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/query")
public class HelloWorldController {

    @GetMapping("/page")
    public String page(@PageRequestAnno PageRequestParam param) {
        System.out.println(param);
        return param == null ? "null" : param.toString();
    }

}
