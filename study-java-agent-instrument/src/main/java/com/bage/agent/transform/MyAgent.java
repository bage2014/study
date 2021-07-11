package com.bage.agent.transform;

import com.bage.agent.model.MyClassInfo;

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
        System.out.println("hello MyAgent premain");
        MyClassInfo myClassInfo = new MyClassInfo("com.bage.demo.HelloService", Collections.singletonList("sayHi"));
        MyClassFileTransformer transformer = new MyClassFileTransformer(Collections.singletonList(myClassInfo));
        instrumentation.addTransformer(transformer);
    }
}