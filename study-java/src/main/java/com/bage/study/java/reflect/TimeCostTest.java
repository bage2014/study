package com.bage.study.java.reflect;

class B {

}

/**
 * https://stackoverflow.com/questions/1392351/java-reflection-why-is-it-so-slow
 */
public class TimeCostTest {

    public static long timeDiff(long old) {
        return System.currentTimeMillis() - old;
    }

    public static void main(String args[]) throws Exception {

        long numTrials = (long) Math.pow(10, 7);

        long millis;

        millis = System.currentTimeMillis();

        for (int i=0; i<numTrials; i++) {
            new B();
        }
        System.out.println("Normal instaniation took: "
                 + timeDiff(millis) + "ms");

        millis = System.currentTimeMillis();

        Class<B> c = B.class;

        for (int i=0; i<numTrials; i++) {
            c.newInstance();
        }

        System.out.println("Reflecting instantiation took:" 
              + timeDiff(millis) + "ms");

    }
}