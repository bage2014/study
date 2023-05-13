package com.bage.study.springboot.aop.order;

import com.bage.study.springboot.aop.annotation.log.Logger;
import org.springframework.stereotype.Component;

@Component
public class HelloAopOrderService {

    public String hello(String msg) throws Exception {
//        throw new Exception("hello");
        return "hello," + msg;
    }

    public String hello2(String msg) {
        return "hello," + msg;
    }

    @Logger
    public Helo hello3(String msg) {
        return new Helo(msg);
    }

    public class Helo implements Hhhh{
        private String msg;

        public Helo(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "Helo{" +
                    "msg='" + msg + '\'' +
                    '}';
        }
    }

    public interface Hhhh{

    }

}
