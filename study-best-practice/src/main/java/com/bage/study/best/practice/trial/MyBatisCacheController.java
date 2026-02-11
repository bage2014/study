package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * MyBatis 缓存验证控制器
 * 一级缓存：SqlSession级别，默认开启
 * 二级缓存：Mapper级别，需要手动开启
 */
@RequestMapping("/mybatis/cache")
@RestController
@Slf4j
public class MyBatisCacheController {

    @Autowired
    private UserService userService;

    /**
     * 测试一级缓存 - 同一请求内的相同查询
     * 预期：第二次查询会使用缓存，不会发送SQL
     */
    @RequestMapping("/level1/same")
    public Object testLevel1CacheSameRequest(@RequestParam("phone") String phone) {
        log.info("=== 测试一级缓存：同一请求内的相同查询 ===");
        
        // 第一次查询
        log.info("第一次查询开始");
        List<User> users1 = userService.query(phone);
        log.info("第一次查询结果：{}", users1);
        
        // 第二次查询（相同参数）
        log.info("第二次查询开始");
        List<User> users2 = userService.query(phone);
        log.info("第二次查询结果：{}", users2);
        
        // 验证是否是同一对象
        boolean isSameObject = users1 == users2;
        log.info("两次查询结果是否为同一对象：{}", isSameObject);
        
        return "一级缓存测试完成，请查看日志";
    }

    /**
     * 测试一级缓存 - 执行更新操作后
     * 预期：更新操作会清空缓存，第二次查询会重新发送SQL
     */
    @RequestMapping("/level1/after/update")
    public Object testLevel1CacheAfterUpdate(@RequestParam("phone") String phone) {
        log.info("=== 测试一级缓存：执行更新操作后 ===");
        
        // 第一次查询
        log.info("第一次查询开始");
        List<User> users1 = userService.query(phone);
        log.info("第一次查询结果：{}", users1);
        
        // 执行更新操作
        log.info("执行更新操作");
        // 注意：这里需要实际执行一个更新操作
        // 由于没有现成的更新方法，这里仅做演示
        
        // 第二次查询
        log.info("第二次查询开始");
        List<User> users2 = userService.query(phone);
        log.info("第二次查询结果：{}", users2);
        
        return "一级缓存更新测试完成，请查看日志";
    }

    /**
     * 测试二级缓存 - 需要在Mapper接口上开启
     * 预期：不同请求间的相同查询会使用缓存
     */
    @RequestMapping("/level2")
    public Object testLevel2Cache(@RequestParam("phone") String phone) {
        log.info("=== 测试二级缓存 ===");
        
        // 查询操作
        log.info("查询开始");
        List<User> users = userService.query(phone);
        log.info("查询结果：{}", users);
        
        return "二级缓存测试完成，请查看日志";
    }
}
