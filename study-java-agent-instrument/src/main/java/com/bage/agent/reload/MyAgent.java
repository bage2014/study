package com.bage.agent.reload;

import com.bage.agent.transform.MyClassFileTransformer;
import com.bage.agent.model.MyClassInfo;

import java.lang.instrument.Instrumentation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class MyAgent {

    /**
     * 如果不存在 agentmain(String args, Instrumentation instrumentation)
     * 则会执行 agentmain(String args)
     * ref: sun.instrument.InstrumentationImpl
     *
     * @param agentArgs
     * @param instrumentation
     */
    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("hello MyAgent agentmain");
        try {

            MyClassInfo myClassInfo = new MyClassInfo("com.bage.demo.HelloService", Collections.singletonList("sayHi"));
            List<MyClassInfo> classInfoList = Collections.singletonList(myClassInfo);

            List<Class> list = new LinkedList<>();
            Class[] loadedClass = instrumentation.getAllLoadedClasses();
            for (MyClassInfo classInfo : classInfoList) {
                for (int i = 0; i < loadedClass.length; i++) {
                    if (loadedClass[i].getName().equals(classInfo.getClassName())) {
                        list.add(loadedClass[i]);
                    }
                }
            }
            com.bage.agent.transform.MyClassFileTransformer transformer = new com.bage.agent.transform.MyClassFileTransformer(classInfoList);
            instrumentation.addTransformer(transformer);
            instrumentation.retransformClasses(list.toArray(new Class[0]));
        } catch (Exception e) {

        }
        MyClassInfo myClassInfo = new MyClassInfo("com.bage.demo.HelloService", Collections.singletonList("sayHi"));
        com.bage.agent.transform.MyClassFileTransformer transformer = new MyClassFileTransformer(Collections.singletonList(myClassInfo));
        instrumentation.addTransformer(transformer);
    }

}