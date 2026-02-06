package com.bage.study.best.practice.trial.gc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * JVM垃圾回收器验证控制器
 */
@RequestMapping("/gc")
@RestController
@Slf4j
public class GCController {

    /**
     * 触发年轻代GC
     */
    @RequestMapping("/young/gc")
    public Object triggerYoungGC() {
        log.info("开始触发年轻代GC");
        
        // 创建大量小对象，触发年轻代GC
        List<byte[]> objects = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            // 每个对象1MB
            byte[] obj = new byte[1024 * 1024];
            objects.add(obj);
        }
        
        // 清空引用，让对象可回收
        objects.clear();
        
        // 建议JVM进行GC
        System.gc();
        
        log.info("年轻代GC触发完成");
        return "年轻代GC触发完成，请查看GC日志";
    }

    /**
     * 触发老年代GC
     */
    @RequestMapping("/old/gc")
    public Object triggerOldGC() {
        log.info("开始触发老年代GC");
        
        // 创建大量对象，使其进入老年代
        List<byte[]> objects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // 每个对象5MB，多次创建触发晋升
            byte[] obj = new byte[5 * 1024 * 1024];
            objects.add(obj);
            // 短暂休眠，让对象存活时间更长
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // 建议JVM进行GC
        System.gc();
        
        log.info("老年代GC触发完成");
        return "老年代GC触发完成，请查看GC日志";
    }

    /**
     * 测试内存分配策略
     */
    @RequestMapping("/memory/allocation")
    public Object testMemoryAllocation(@RequestParam("size") int size) {
        log.info("开始测试内存分配策略，分配大小: {}MB", size);
        
        try {
            // 分配大内存
            byte[] obj = new byte[size * 1024 * 1024];
            log.info("内存分配成功，大小: {}MB", size);
            return "内存分配成功，大小: " + size + "MB";
        } catch (OutOfMemoryError e) {
            log.error("内存分配失败: {}", e.getMessage());
            return "内存分配失败: " + e.getMessage();
        }
    }

    /**
     * 测试G1 GC的行为
     */
    @RequestMapping("/g1/test")
    public Object testG1GC(@RequestParam("count") int count) {
        log.info("开始测试G1 GC行为，创建对象数量: {}", count);
        
        List<byte[]> objects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            try {
                // 随机大小的对象
                int size = (i % 10 + 1) * 1024 * 1024; // 1-10MB
                byte[] obj = new byte[size];
                objects.add(obj);
                
                // 每创建100个对象清空一次，模拟对象生命周期
                if (i % 100 == 0) {
                    objects.clear();
                    System.gc();
                }
            } catch (OutOfMemoryError e) {
                log.error("G1 GC测试失败: {}", e.getMessage());
                return "G1 GC测试失败: " + e.getMessage();
            }
        }
        
        log.info("G1 GC测试完成");
        return "G1 GC测试完成，请查看GC日志";
    }

    /**
     * 测试垃圾回收性能指标
     */
    @RequestMapping("/performance/test")
    public Object testGCPerformance(@RequestParam("count") int count, @RequestParam("interval") int interval) {
        log.info("开始测试垃圾回收性能指标，创建对象数量: {}, 间隔时间: {}ms", count, interval);
        
        // 初始化GC监控器
        GCMonitor gcMonitor = new GCMonitor();
        gcMonitor.start();
        
        List<byte[]> objects = new ArrayList<>();
        
        try {
            for (int i = 0; i < count; i++) {
                // 创建对象
                int size = (i % 10 + 1) * 1024 * 1024; // 1-10MB
                byte[] obj = new byte[size];
                objects.add(obj);
                
                // 每创建一定数量的对象后清空，模拟对象生命周期
                if (i % 50 == 0) {
                    objects.clear();
                    System.gc();
                }
                
                // 短暂休眠，模拟实际应用中的时间间隔
                if (interval > 0) {
                    Thread.sleep(interval);
                }
            }
        } catch (Exception e) {
            log.error("GC性能测试失败: {}", e.getMessage());
            return "GC性能测试失败: " + e.getMessage();
        }
        
        // 停止监控并获取结果
        GCMonitor.GCMonitorResult result = gcMonitor.stop();
        
        log.info("GC性能测试完成，结果: {}", result);
        return result;
    }

    /**
     * 比较不同垃圾回收器的性能
     */
    @RequestMapping("/performance/compare")
    public Object compareGCPerformance(@RequestParam("count") int count) {
        log.info("开始比较不同垃圾回收器的性能，创建对象数量: {}", count);
        
        // 这里只是一个示例，实际比较需要通过不同的JVM参数启动应用
        // 这里我们通过模拟不同的对象创建模式来测试
        
        // 测试场景1: 大量小对象
        log.info("\n=== 测试场景1: 大量小对象 ===");
        testGCScenario(count, 1024 * 1024, 5); // 1MB对象，每5个清空一次
        
        // 测试场景2: 少量大对象
        log.info("\n=== 测试场景2: 少量大对象 ===");
        testGCScenario(count / 10, 10 * 1024 * 1024, 2); // 10MB对象，每2个清空一次
        
        // 测试场景3: 混合大小对象
        log.info("\n=== 测试场景3: 混合大小对象 ===");
        testGCScenario(count, 5 * 1024 * 1024, 10); // 5MB对象，每10个清空一次
        
        return "不同垃圾回收器性能比较测试完成，请查看日志";
    }

    /**
     * 测试GC场景
     */
    private void testGCScenario(int count, int objectSize, int clearInterval) {
        GCMonitor gcMonitor = new GCMonitor();
        gcMonitor.start();
        
        List<byte[]> objects = new ArrayList<>();
        
        try {
            for (int i = 0; i < count; i++) {
                byte[] obj = new byte[objectSize];
                objects.add(obj);
                
                if (i % clearInterval == 0) {
                    objects.clear();
                    System.gc();
                }
                
                // 短暂休眠
                Thread.sleep(1);
            }
        } catch (Exception e) {
            log.error("GC场景测试失败: {}", e.getMessage());
        }
        
        GCMonitor.GCMonitorResult result = gcMonitor.stop();
        log.info("场景测试结果: 对象大小={}MB, 清空间隔={}, 结果={}", 
                objectSize / (1024 * 1024), clearInterval, result);
    }

} 
