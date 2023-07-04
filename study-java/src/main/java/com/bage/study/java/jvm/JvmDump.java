package com.bage.study.java.jvm;

import java.util.ArrayList;
import java.util.List;

/*

java  -Xms16M -Xmx64M -XX:+HeapDumpOnOutOfMemoryError JvmDump.java

 */
public class JvmDump {

    public static void main(String[] args) throws InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        printUsed(runtime, 0);

        List<Object> listObject = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            byte[] tenM = new byte[1024 * 1024 * 10];
            printUsed(runtime, i + 1);
            Thread.sleep(1000);
            listObject.add(tenM);
        }

    }

    private static void printUsed(Runtime runtime, int i) {
        System.out.print(i + "-----------------");

        System.out.print("maxMemory:" + runtime.maxMemory() / 1024 / 1024 + " MB;");
        System.out.print("freeMemory:" + runtime.freeMemory() / 1024 / 1024 + " MB;");
        System.out.println("totalMemory:" + runtime.totalMemory() / 1024 / 1024 + " MB");
    }

}
