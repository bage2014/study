package com.bage.study.mybatis.springboot.user.dao;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.bage.study.mybatis.springboot.user.domain.User;

@Repository
public class UserDaoImp implements UserDao {

	@Cacheable("books")
    public User getUserById(String id) {
        simulateSlowService();
        return new User(id, "缓存用户","1");
    }

    // Don't do this at home
    private void simulateSlowService() {
        try {
        	System.out.println("正在查询数据库 。。。 ");
            long time = 300L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}