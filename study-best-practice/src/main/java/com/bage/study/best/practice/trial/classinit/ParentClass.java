package com.bage.study.best.practice.trial.classinit;

import lombok.extern.slf4j.Slf4j;

/**
 * 父类 - 用于验证类初始化顺序
 */
@Slf4j
public class ParentClass {

    // 静态变量
    public static String PARENT_STATIC_VAR = "父类静态变量初始化";

    // 静态代码块
    static {
        log.info("父类静态代码块执行");
    }

    // 实例变量
    public String parentInstanceVar = "父类实例变量初始化";

    // 实例代码块
    {
        log.info("父类实例代码块执行");
    }

    // 构造函数
    public ParentClass() {
        log.info("父类构造函数执行");
    }

}
