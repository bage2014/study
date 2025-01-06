package com.bage.study.best.practice.biz.template2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

/**
 * 组件抽象类
 */
@Slf4j
public abstract class ComponentAbstract {

    public void handlerRequest(Context contxt) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //     执行子类业务逻辑
        this.doHandler(contxt);
        stopWatch.stop();
        long cost = stopWatch.getTotalTimeMillis();
        if (cost <= 10) {
            log.info("-----------监控统方法执行时间，执行     {}     方法,     用时优秀:     {}     ms     -----------", getClass(), cost);
        } else if (cost <= 50) {
            log.info("-----------监控统方法执行时间，执行     {}     方法,     用时一般:     {}     ms     -----------", getClass(), cost);
        } else if (cost <= 500) {
            log.info("-----------监控统方法执行时间，执行     {}     方法,     用时延迟:     {}     ms     -----------", getClass(), cost);
        } else if (cost <= 1000) {
            log.info("-----------监控统法执行时间，执行     {}     方法,     用时缓慢:     {}     ms     -----------", getClass(), cost);
        } else {
            log.info("-----------监控方法执行时间，执行     {}     方法,     用时卡顿:     {}     ms     -----------", getClass(), cost);
        }
    }

    abstract public void doHandler(Context contxt);
}
