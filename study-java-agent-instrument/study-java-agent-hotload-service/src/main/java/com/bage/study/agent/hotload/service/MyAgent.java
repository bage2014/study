package com.bage.study.agent.hotload.service;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;


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
        System.out.println("MyAgent agentmain started.....");
        try {
            String className = "com.bage.study.agent.demo.service.HelloService";
            CtClass ctClass = ClassPool.getDefault().get(className);// 使用全称,用于取得字节码类<使用javassist>
            CtMethod ctmethod = ctClass.getDeclaredMethod("sayHi");// 得到这方法实例
            ctmethod.insertBefore("System.out.println(\"hello,world\");");
            //重新加载类，实现热更新，注意这里通过Class.forName取得原来已经加载在JVM晨的类，然后热更新它
            instrumentation.redefineClasses(new ClassDefinition(Class.forName(className), ctClass.toBytecode()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}