package com.bage.study.best.practice.trial.classload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 类加载过程验证控制器
 */
@RequestMapping("/class/load")
@RestController
@Slf4j
public class ClassLoadController {

    /**
     * 验证类加载过程
     */
    @RequestMapping("/test")
    public Object testClassLoad() {
        log.info("开始验证类加载过程");
        
        Map<String, Object> result = new HashMap<>();
        
        // 1. 首次访问静态常量 - 不会触发类初始化
        log.info("\n1. 访问静态常量");
        String staticConstant = TestClass.STATIC_CONSTANT;
        log.info("TestClass.STATIC_CONSTANT = {}", staticConstant);
        
        // 检查类加载状态
        ClassLoadMonitor.ClassLoadInfo info1 = ClassLoadMonitor.getClassLoadInfo(TestClass.class.getName());
        result.put("afterAccessStaticConstant", info1);
        
        // 2. 访问静态变量 - 触发类初始化
        log.info("\n2. 访问静态变量");
        String staticVar = TestClass.staticVar;
        log.info("TestClass.staticVar = {}", staticVar);
        
        // 检查类加载状态
        ClassLoadMonitor.ClassLoadInfo info2 = ClassLoadMonitor.getClassLoadInfo(TestClass.class.getName());
        result.put("afterAccessStaticVar", info2);
        
        // 3. 调用静态方法 - 已初始化，不会重复初始化
        log.info("\n3. 调用静态方法");
        TestClass.staticMethod();
        
        // 4. 创建实例
        log.info("\n4. 创建第一个实例");
        TestClass instance1 = new TestClass();
        
        // 5. 创建第二个实例
        log.info("\n5. 创建第二个实例");
        TestClass instance2 = new TestClass();
        
        // 检查实例数量
        int instanceCount = ClassLoadMonitor.getInstanceCount(TestClass.class.getName());
        result.put("instanceCount", instanceCount);
        
        // 6. 调用实例方法
        log.info("\n6. 调用实例方法");
        instance1.instanceMethod();
        
        log.info("\n类加载过程验证完成");
        return result;
    }

    /**
     * 获取类加载信息
     */
    @RequestMapping("/info")
    public Object getClassLoadInfo(@RequestParam("className") String className) {
        ClassLoadMonitor.ClassLoadInfo info = ClassLoadMonitor.getClassLoadInfo(className);
        if (info != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("classLoadInfo", info);
            result.put("instanceCount", ClassLoadMonitor.getInstanceCount(className));
            return result;
        } else {
            return "Class not found: " + className;
        }
    }

    /**
     * 测试不同类加载器
     */
    @RequestMapping("/loader/test")
    public Object testClassLoader() {
        log.info("开始测试不同类加载器");
        
        Map<String, Object> result = new HashMap<>();
        
        // 测试JDK类 - Bootstrap ClassLoader
        Class<?> stringClass = String.class;
        result.put("String", Map.of(
                "className", stringClass.getName(),
                "loader", stringClass.getClassLoader()
        ));
        
        // 测试应用类 - AppClassLoader
        Class<?> testClass = TestClass.class;
        result.put("TestClass", Map.of(
                "className", testClass.getName(),
                "loader", testClass.getClassLoader() != null ? testClass.getClassLoader().getClass().getName() : "null"
        ));
        
        log.info("类加载器测试完成");
        return result;
    }

}
