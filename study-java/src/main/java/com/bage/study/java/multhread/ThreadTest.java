package com.bage.study.java.multhread;

/**
 * 多线程顺序执行实现
 *
 * @author bage
 */
public class ThreadTest {

    public static void main(String[] args) throws Exception {
        Thread thread1 = new ThreadSeq1();
        thread1.start();
    }

}

