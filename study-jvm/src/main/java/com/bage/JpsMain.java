package com.bage;

public class JpsMain {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("java.io.tmpdir = " + System.getProperty("java.io.tmpdir"));
        int n = 0;

        while (n < 100){

            n ++;

            System.out.println("jpsMain n = " + n);
            Thread.sleep(3000L);

        }

    }

}
