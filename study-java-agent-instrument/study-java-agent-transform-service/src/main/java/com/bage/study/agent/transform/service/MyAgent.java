package com.bage.study.agent.transform.service;

import java.lang.instrument.Instrumentation;
import java.util.Collections;


public class MyAgent {

    /**
     * 如果不存在 premain(String agentArgs, Instrumentation instrumentation)
     * 则会执行 premain(String agentArgs)
     * ref: sun.instrument.InstrumentationImpl
     *
     * @param agentArgs
     * @param instrumentation
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("MyAgent premain started....");
        MyClassInfo myClassInfo = new MyClassInfo("com.bage.study.agent.demo.service.HelloService", Collections.singletonList("sayHi"));
        MyClassFileTransformer transformer = new MyClassFileTransformer(Collections.singletonList(myClassInfo));
        instrumentation.addTransformer(transformer);
    }
}