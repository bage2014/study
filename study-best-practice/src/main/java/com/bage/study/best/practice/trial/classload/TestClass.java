package com.bage.study.best.practice.trial.classload;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试类 - 用于验证类加载过程
 */
@Slf4j
public class TestClass {

    // 静态变量 - 类初始化时执行
    public static final String STATIC_CONSTANT = "static constant";
    public static String staticVar = initStaticVar();

    // 静态代码块 - 类初始化时执行
    static {
        log.info("TestClass static block executed");
        // 记录类初始化
        ClassLoadMonitor.recordClassInit(TestClass.class.getName());
    }

    // 实例变量 - 实例化时执行
    private String instanceVar = initInstanceVar();

    // 实例代码块 - 实例化时执行
    {
        log.info("TestClass instance block executed");
    }

    // 构造函数 - 实例化时执行
    public TestClass() {
        log.info("TestClass constructor executed");
        // 记录实例创建
        ClassLoadMonitor.recordInstanceCreation(TestClass.class.getName());
    }

    // 静态方法 - 调用时才执行
    public static void staticMethod() {
        log.info("TestClass staticMethod executed");
    }

    // 实例方法 - 实例化后调用时执行
    public void instanceMethod() {
        log.info("TestClass instanceMethod executed");
    }

    // 初始化静态变量
    private static String initStaticVar() {
        log.info("TestClass initStaticVar executed");
        return "initialized static var";
    }

    // 初始化实例变量
    private String initInstanceVar() {
        log.info("TestClass initInstanceVar executed");
        return "initialized instance var";
    }

}
