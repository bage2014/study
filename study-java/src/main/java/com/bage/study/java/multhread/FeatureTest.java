package com.bage.study.java.multhread;

import java.util.concurrent.*;

public class FeatureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000);
                return "Callable hello world";
            }
        });
        new Thread(future).start();
        String result = future.get();
        System.out.println(result);

    }

}
