package com.bage.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

public class ClassLogger implements ClassFileTransformer {

    private List<ClassInfo> classInfoList;

    public ClassLogger(List<ClassInfo> classInfoList) {
        this.classInfoList = classInfoList;
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        for (ClassInfo classInfo : classInfoList) {
            className = className.replace("/", ".");
            if (className.equals(classInfo.getClassName())) {
                CtClass ctClass;
                Long start = System.nanoTime();
                try {
                    ctClass = ClassPool.getDefault().get(className);// 使用全称,用于取得字节码类<使用javassist>
                    List<String> methodNames = classInfo.getMethodNames();
                    for (String methodName : methodNames) {
                        CtMethod ctmethod = ctClass.getDeclaredMethod(methodName);// 得到这方法实例
                        ctmethod.insertBefore("System.out.println(111);");

                        ctmethod.insertAfter("System.out.println(222);");
                    }
                    return ctClass.toBytecode();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                } finally {
                    System.out.println("className  took:" + (System.nanoTime() - start));
                }
            }
        }
        return null;
    }
}