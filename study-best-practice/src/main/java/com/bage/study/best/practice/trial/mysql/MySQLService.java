package com.bage.study.best.practice.trial.mysql;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class MySQLService {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 无索引查询 - 慢SQL
     */
    public List<User> sqlSlow(String key) {
        // 地址查询 不会命中索引
        return userService.queryByAddress(key);
    }

    /**
     * 高IO场景 - 大量数据查询
     */
    public List<User> ioHigh(String key) {
        // 模拟大量数据查询，导致高IO
        String sql = "SELECT * FROM user WHERE phone LIKE ? LIMIT 10000";
        return jdbcTemplate.query(sql, new Object[]{"%" + key + "%"}, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
            return user;
        });
    }

    /**
     * 高内存场景 - 大结果集
     */
    public List<User> memoryHigh(String key) {
        // 模拟大结果集，占用大量内存
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
            return user;
        });
    }

    /**
     * 高活跃线程场景 - 并发查询
     */
    public List<User> highActiveThread(String key) {
        // 模拟并发查询，增加活跃线程数
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    String sql = "SELECT * FROM user WHERE id = ?";
                    jdbcTemplate.query(sql, new Object[]{index % 100 + 1}, (rs, rowNum) -> {
                        User user = new User();
                        user.setId(rs.getLong("id"));
                        user.setName(rs.getString("name"));
                        user.setPhone(rs.getString("phone"));
                        user.setAddress(rs.getString("address"));
                        return user;
                    });
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        executor.shutdown();
        
        // 返回一个结果
        return userService.queryByAddress(key);
    }

    /**
     * 全表扫描 - 慢SQL
     */
    public List<User> fullTableScan() {
        // 全表扫描，不使用索引
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
            return user;
        });
    }

    /**
     * 复杂JOIN查询 - 慢SQL
     */
    public List<User> complexJoin() {
        // 模拟复杂JOIN查询
        String sql = "SELECT u.* FROM user u JOIN user u2 ON u.phone = u2.phone";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
            return user;
        });
    }

    /**
     * 排序操作 - 无索引
     */
    public List<User> orderByNoIndex() {
        // 无索引排序
        String sql = "SELECT * FROM user ORDER BY address";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
            return user;
        });
    }

    /**
     * LIKE查询 - 前缀模糊匹配
     */
    public List<User> likePrefix(String prefix) {
        // LIKE前缀模糊匹配
        String sql = "SELECT * FROM user WHERE name LIKE ?";
        return jdbcTemplate.query(sql, new Object[]{prefix + "%"}, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
            return user;
        });
    }

    /**
     * LIKE查询 - 中缀模糊匹配
     */
    public List<User> likeInfix(String keyword) {
        // LIKE中缀模糊匹配 - 不会使用索引
        String sql = "SELECT * FROM user WHERE name LIKE ?";
        return jdbcTemplate.query(sql, new Object[]{"%" + keyword + "%"}, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
            return user;
        });
    }

    /**
     * 函数操作 - 索引失效
     */
    public List<User> functionOperation() {
        // 函数操作导致索引失效
        String sql = "SELECT * FROM user WHERE LENGTH(name) > 5";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setPhone(rs.getString("phone"));
            user.setAddress(rs.getString("address"));
            return user;
        });
    }

    /**
     * 查询指定数量的记录
     */
    public List<User> queryByKey(String key) {
        return userService.queryByAddress(key);
    }

    /**
     * 查询100条记录
     */
    public List<User> query100(String key) {
        // 地址查询 不会命中索引
        return userService.queryPhoneLike(key);
    }

    /**
     * 查询10条记录
     */
    public List<User> query10(String key) {
        // 地址查询 不会命中索引
        return userService.queryPhoneLike(key);
    }
}
