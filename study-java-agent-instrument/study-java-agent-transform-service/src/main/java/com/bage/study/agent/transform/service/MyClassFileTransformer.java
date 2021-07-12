package com.bage.study.agent.transform.service;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.List;

/**
 *
 */
public class MyClassFileTransformer implements ClassFileTransformer {

    private List<MyClassInfo> myClassInfoList;

    public MyClassFileTransformer(List<MyClassInfo> myClassInfoList) {
        this.myClassInfoList = myClassInfoList;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        for (MyClassInfo myClassInfo : myClassInfoList) {
            className = className.replace("/",".");
            if (className.equals(myClassInfo.getClassName())) {
                try {
                    CtClass ctClass = ClassPool.getDefault().get(className);// 使用全称,用于取得字节码类<使用javassist>
                    List<String> methodNames = myClassInfo.getMethodNames();
                    for (String methodName : methodNames) {
                        CtMethod ctmethod = ctClass.getDeclaredMethod(methodName);// 得到这方法实例
                        ctmethod.insertBefore("System.out.println(\"hello,world\");");
                    }
                    return ctClass.toBytecode();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("getMessage: " + e.getMessage());
                }
            }
        }
        return classfileBuffer;
    }
}