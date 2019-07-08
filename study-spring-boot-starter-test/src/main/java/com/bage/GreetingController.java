package com.bage;

import com.bage.starter.MyExitsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class GreetingController {

    @Autowired
    MyExitsBean myExitsBean;

    @RequestMapping("/greeting")
    public @ResponseBody
    Object greeting() {
        return myExitsBean;
    }

}