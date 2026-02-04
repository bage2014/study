package com.bage.study.best.practice.trial.classinit;

import lombok.extern.slf4j.Slf4j;

/**
 * 子类 - 用于验证类初始化顺序
 */
@Slf4j
public class SubClass extends ParentClass {

    // 静态变量
    public static String SUB_STATIC_VAR = "子类静态变量初始化";

    // 静态代码块
    static {
        log.info("子类静态代码块执行");
    }

    // 实例变量
    public String subInstanceVar = "子类实例变量初始化";

    // 实例代码块
    {
        log.info("子类实例代码块执行");
    }

    // 构造函数
    public SubClass() {
        log.info("子类构造函数执行");
    }

}
