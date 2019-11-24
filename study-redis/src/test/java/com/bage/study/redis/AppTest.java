package com.bage.study.redis;

import com.bage.study.redis.lock.DistributeLock;
import com.bage.study.redis.lock.DistributeLockBuilder;
import com.bage.study.redis.lock.RedisDistributeLock;
import com.sun.media.jfxmedia.logging.Logger;

import java.util.Objects;

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

    @Test
    public void test() {
        DistributeLock lock = new DistributeLockBuilder().getLock("com.bage");
        try {
            if(lock.tryLock()){
                // doSomething();
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        } finally {
            if(Objects.nonNull(lock)){
                lock.unlock();
            }
        }
    }

}
