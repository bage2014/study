package com.bage.study.redis;

import com.bage.study.redis.lock.DistributeLock;
import com.bage.study.redis.lock.RedisDistributeLock;

/**
 * Unit test for simple App.
 */
public class AppTest {

    public static void main(String[] args) {
        DistributeLock lock = new RedisDistributeLock("com.bage");
        DistributeLock lock2 = new RedisDistributeLock("com.bage2");

        System.out.println(lock.tryLock());
        System.out.println(lock2.tryLock());

        lock.unlock();

        System.out.println(lock.tryLock());
        System.out.println(lock2.tryLock());



    }

}
