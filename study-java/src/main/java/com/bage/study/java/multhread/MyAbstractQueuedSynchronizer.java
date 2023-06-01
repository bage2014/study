package com.bage.study.java.multhread;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * AQS
 * @author bage
 *
 */
public class MyAbstractQueuedSynchronizer extends AbstractQueuedSynchronizer {

    @Override
    protected boolean tryAcquire(int arg) {
        return super.tryAcquire(arg);
    }
}
