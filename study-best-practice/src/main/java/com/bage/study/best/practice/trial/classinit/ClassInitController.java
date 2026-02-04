package com.bage.study.best.practice.trial.classinit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类初始化顺序验证控制器
 */
@RequestMapping("/class/init")
@RestController
@Slf4j
public class ClassInitController {

    /**
     * 触发类初始化过程
     */
    @RequestMapping("/test")
    public Object testClassInit() {
        log.info("开始验证类初始化顺序");
        
        // 第一次访问时会触发类的初始化
        log.info("ParentClass.PARENT_STATIC_VAR = {}", ParentClass.PARENT_STATIC_VAR);
        log.info("SubClass.SUB_STATIC_VAR = {}", SubClass.SUB_STATIC_VAR);
        
        // 创建实例
        log.info("\n开始创建SubClass实例");
        SubClass subClass = new SubClass();
        
        log.info("\n类初始化顺序验证完成");
        return "类初始化顺序验证完成，请查看日志输出";
    }

    /**
     * 单独测试父类初始化
     */
    @RequestMapping("/parent")
    public Object testParentInit() {
        log.info("开始验证父类初始化顺序");
        
        // 触发父类初始化
        log.info("ParentClass.PARENT_STATIC_VAR = {}", ParentClass.PARENT_STATIC_VAR);
        
        // 创建父类实例
        log.info("\n开始创建ParentClass实例");
        ParentClass parentClass = new ParentClass();
        
        log.info("\n父类初始化顺序验证完成");
        return "父类初始化顺序验证完成，请查看日志输出";
    }

}
