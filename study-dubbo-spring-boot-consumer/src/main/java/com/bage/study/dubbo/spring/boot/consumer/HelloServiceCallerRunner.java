/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bage.study.dubbo.spring.boot.consumer;

import java.util.Date;
import java.util.Random;

import com.bage.study.dubbo.spring.boot.api.HelloParam;
import com.bage.study.dubbo.spring.boot.api.HelloResult;
import com.bage.study.dubbo.spring.boot.api.HelloService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HelloServiceCallerRunner implements CommandLineRunner {
    @DubboReference
    private HelloService demoService;
    private Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        HelloParam param = getParam();
        System.out.println("Send param ======> " + param);
        HelloResult result = demoService.sayHello(param);
        System.out.println("Receive result ======> " + result);

        new Thread(()-> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    HelloParam param2 = getParam();
                    System.out.println(new Date() + "Send param ======> " + param);
                    System.out.println(new Date() + " Receive result ======> " + demoService.sayHello(param2));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private HelloParam getParam() {
        HelloParam param = new HelloParam();
        param.setId(random.nextInt());
        param.setName("hhhh");
        return param;
    }
}