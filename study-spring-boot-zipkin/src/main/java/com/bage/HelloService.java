package com.bage;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class HelloService {

    public String hhh(){
        try {
            Thread.sleep(new Random().nextInt(3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hjdhgjdhg");
        return "hjhdjhgd";
    }
}
