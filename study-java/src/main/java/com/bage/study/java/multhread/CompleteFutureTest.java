package com.bage.study.java.multhread;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CompleteFutureTest {

    public static void main(String[] args) {

        // supplyAsync 基本用法，异步响应,有响应结果
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "ABC";
        }).whenComplete((item,e) -> {
            System.out.println(item);
        });

        // runAsync 无返回值
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String abc = "ABCD";
            System.out.println(abc);
        }).whenComplete((item,e) -> {
            System.out.println(item);
        });

        // supplyAsync 基本用法，异步响应,有响应结果
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "3000L";
        }).whenComplete((item, e) -> {
            System.out.println(item);
        });
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(4000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "4000L";
        }).whenComplete((item, e) -> {
            System.out.println(item);
        });
        // 所有结束，才结束，无返回值
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(cf1, cf2).whenComplete((item, e) -> {
            System.out.println("allOf:" + item);
        });
        // 任意结束，即结束
        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(cf1, cf2).whenComplete((item, e) -> {
            System.out.println("anyOf:" + item);
        });

        // 合并
        CompletableFuture.supplyAsync(()->{
            return "hello ";
        }).thenApplyAsync(item -> {return item + ",world";})
        .whenComplete((res,e) -> {
            System.out.println("res::" + res);
        });


        // 等待结束
        try {
            Thread.sleep(6000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        Runnable run = () -> {};
//        CompletableFuture.runAsync(run);
//        Supplier<String> supplier = () -> "";
//        CompletableFuture.supplyAsync(supplier);

    }

}
