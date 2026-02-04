package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.service.UserMockService;
import com.bage.study.best.practice.trial.mysql.MySQLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 参考 com.bage.study.best.practice.controller.MySQLController
 */
@RequestMapping("/mysql")
@RestController
@Slf4j
public class MySQLController {

    @Autowired
    private MySQLService mySQLService;
    @Autowired
    private UserMockService userMockService;

    /**
     * 无索引查询 - 慢SQL
     */
    @RequestMapping("/sql/slow")
    public Object sqlSlow(@RequestParam("key") String key) {
        long start = System.currentTimeMillis();
        List<User> users = mySQLService.sqlSlow(key);
        long end = System.currentTimeMillis();
        log.info("MySQLController sqlSlow cost = {}, users = {}", (end - start), users);
        return users;
    }

    /**
     * 高IO场景 - 大量数据查询
     */
    @RequestMapping("/io/high")
    public Object ioHigh(@RequestParam("key") String key) {
        long start = System.currentTimeMillis();
        List<User> users = mySQLService.ioHigh(key);
        long end = System.currentTimeMillis();
        log.info("MySQLController ioHigh cost = {}, users = {}", (end - start), users);
        return users;
    }

    /**
     * 高内存场景 - 大结果集
     */
    @RequestMapping("/memory/high")
    public Object memoryHigh(@RequestParam("key") String key) {
        long start = System.currentTimeMillis();
        List<User> users = mySQLService.memoryHigh(key);
        long end = System.currentTimeMillis();
        log.info("MySQLController memoryHigh cost = {}, users = {}", (end - start), users);
        return users;
    }

    /**
     * 高活跃线程场景 - 并发查询
     */
    @RequestMapping("/thread/active/high")
    public Object highActiveThread(@RequestParam("key") String key) {
        long start = System.currentTimeMillis();
        List<User> users = mySQLService.highActiveThread(key);
        long end = System.currentTimeMillis();
        log.info("MySQLController highActiveThread cost = {}, users = {}", (end - start), users);
        return users;
    }

    /**
     * 全表扫描 - 慢SQL
     */
    @RequestMapping("/full/table/scan")
    public Object fullTableScan() {
        long start = System.currentTimeMillis();
        List<User> users = mySQLService.fullTableScan();
        long end = System.currentTimeMillis();
        log.info("MySQLController fullTableScan cost = {}, users = {}", (end - start), users);
        return users;
    }

    /**
     * 复杂JOIN查询 - 慢SQL
     */
    @RequestMapping("/complex/join")
    public Object complexJoin() {
        long start = System.currentTimeMillis();
        List<User> users = mySQLService.complexJoin();
        long end = System.currentTimeMillis();
        log.info("MySQLController complexJoin cost = {}, users = {}", (end - start), users);
        return users;
    }

    /**
     * 排序操作 - 无索引
     */
    @RequestMapping("/order/by/no/index")
    public Object orderByNoIndex() {
        long start = System.currentTimeMillis();
        List<User> users = mySQLService.orderByNoIndex();
        long end = System.currentTimeMillis();
        log.info("MySQLController orderByNoIndex cost = {}, users = {}", (end - start), users);
        return users;
    }

    /**
     * LIKE查询 - 前缀模糊匹配
     */
    @RequestMapping("/like/prefix")
    public Object likePrefix(@RequestParam("prefix") String prefix) {
        long start = System.currentTimeMillis();
        List<User> users = mySQLService.likePrefix(prefix);
        long end = System.currentTimeMillis();
        log.info("MySQLController likePrefix cost = {}, users = {}", (end - start), users);
        return users;
    }

    /**
     * LIKE查询 - 中缀模糊匹配
     */
    @RequestMapping("/like/infix")
    public Object likeInfix(@RequestParam("keyword") String keyword) {
        long start = System.currentTimeMillis();
        List<User> users = mySQLService.likeInfix(keyword);
        long end = System.currentTimeMillis();
        log.info("MySQLController likeInfix cost = {}, users = {}", (end - start), users);
        return users;
    }

    /**
     * 函数操作 - 索引失效
     */
    @RequestMapping("/function/operation")
    public Object functionOperation() {
        long start = System.currentTimeMillis();
        List<User> users = mySQLService.functionOperation();
        long end = System.currentTimeMillis();
        log.info("MySQLController functionOperation cost = {}, users = {}", (end - start), users);
        return users;
    }

    /**
     * 查询100条记录
     */
    @RequestMapping("/query/100")
    public Object query100(@RequestParam("key") String key) {
        long start = System.currentTimeMillis();
        List<User> users = mySQLService.query100(key);
        long end = System.currentTimeMillis();
        log.info("MySQLController query100 cost = {}, users = {}", (end - start), users);
        return users;
    }

    /**
     * 查询10条记录
     */
    @RequestMapping("/query/10")
    public Object query10(@RequestParam("key") String key) {
        long start = System.currentTimeMillis();
        List<User> users = mySQLService.query10(key);
        long end = System.currentTimeMillis();
        log.info("MySQLController query10 cost = {}, users = {}", (end - start), users);
        return users;
    }

}
