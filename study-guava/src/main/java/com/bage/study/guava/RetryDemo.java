package com.bage.study.guava;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.google.common.base.Predicates;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class RetryDemo {

    private static  int count = 0;

    public static void main(String[] args) {

        Callable<Boolean> callable = new Callable<Boolean>() {
            public Boolean call() throws Exception {
                System.out.println(count ++);
                if(count <= 2){
                    throw new IOException();
                }
              return true; // do something useful here
            }
        };

        Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
                .retryIfResult(Predicates.<Boolean>isNull())
                .retryIfExceptionOfType(IOException.class)
                .retryIfRuntimeException()
                .withStopStrategy(StopStrategies.stopAfterAttempt(5))
                .build();

//        Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
//                .retryIfExceptionOfType(IOException.class)
//                .retryIfRuntimeException()
//                .withWaitStrategy(WaitStrategies.exponentialWait(100, 5, TimeUnit.MINUTES))
//                .withStopStrategy(StopStrategies.neverStop())
//                .build();

        try {
            retryer.call(callable);
        } catch (RetryException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
}
