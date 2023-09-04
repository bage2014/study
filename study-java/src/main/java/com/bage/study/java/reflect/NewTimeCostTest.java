package com.bage.study.java.reflect;

import java.util.concurrent.TimeUnit;

public class NewTimeCostTest {

    static class B {

    }

    public static long timeDiff(long old) {
        return System.nanoTime() - old;
    }

    public static void main(String args[]) throws Exception {

        int numTrials = 10000000;
        B[] bees = new B[numTrials];
        Class<B> c = B.class;
        for (int i = 0; i < numTrials; i++) {
            bees[i] = c.newInstance();
        }
        for (int i = 0; i < numTrials; i++) {
            bees[i] = new B();
        }

        long nanos;

        nanos = System.nanoTime();
        for (int i = 0; i < numTrials; i++) {
            bees[i] = c.newInstance();
        }
        System.out.println("Reflecting instantiation took:" + TimeUnit.NANOSECONDS.toMillis(timeDiff(nanos)) + "ms");

        nanos = System.nanoTime();
        for (int i = 0; i < numTrials; i++) {
            bees[i] = new B();
        }
        System.out.println("Normal instaniation took: " + TimeUnit.NANOSECONDS.toMillis(timeDiff(nanos)) + "ms");
    }


}