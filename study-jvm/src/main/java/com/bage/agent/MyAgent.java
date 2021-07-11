package com.bage.agent;

import java.lang.instrument.Instrumentation;


public class MyAgent {

    /**
     * 如果不存在 premain(String args, Instrumentation instrumentation)
     * 则会执行 premain(String args)
     * ref: sun.instrument.InstrumentationImpl
     * @param args
     * @param instrumentation
     */
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("hello MyAgent premain");
        ClassLogger transformer = new ClassLogger();
        instrumentation.addTransformer(transformer);
    }

}