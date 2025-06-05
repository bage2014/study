package com.bage.study.best.practice.trial.gc;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JvmGcService {

    public void youngGc(Integer iterations) {
        // Print JVM memory information
        printMemoryInfo();

        // Number of iterations - adjust based on your JVM settings
        if (iterations <= 0) {
            iterations = 1000; // Default to 100 iterations if not specified
        }
        for (int i = 0; i < iterations; i++) {
            // Allocate 1MB on each iteration
            byte[] largeObject = new byte[1024 * 1024];

            // Print memory info every 10 iterations
            if (i % 10 == 0) {
                printMemoryInfo();
                try {
                    Thread.sleep(100); // Small delay to observe the output
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        System.out.println("Young GC Demo completed");
    }
    /**
     *
     * @param iterations   Number of iterations - adjust based on your JVM settings
     */
    public void fullGc(Integer iterations) {
        // Print JVM memory information
        printMemoryInfo();

        // Create a list to hold our objects
        List<byte[]> list = new ArrayList<>();

        // Number of iterations - adjust based on your JVM settings
        if (iterations <= 0) {
            iterations = 1000; // Default to 100 iterations if not specified
        }
        for (int i = 0; i < iterations; i++) {
            // Allocate 1MB on each iteration
            byte[] largeObject = new byte[1024 * 1024];

            // Add to list to prevent GC from collecting it immediately
            list.add(largeObject);

            // Print memory info every 10 iterations
            if (i % 10 == 0) {
                printMemoryInfo();
                try {
                    Thread.sleep(100); // Small delay to observe the output
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        System.out.println("Full GC Demo completed");
    }

    private static void printMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        System.out.println("-- Memory Info --");
        System.out.println("Max memory: " + bytesToMegabytes(maxMemory) + "MB");
        System.out.println("Allocated memory: " + bytesToMegabytes(allocatedMemory) + "MB");
        System.out.println("Free memory: " + bytesToMegabytes(freeMemory) + "MB");
        System.out.println("Total free memory: " +
                bytesToMegabytes(freeMemory + (maxMemory - allocatedMemory)) + "MB");
        System.out.println("----------------");
    }

    private static long bytesToMegabytes(long bytes) {
        return bytes / (1024 * 1024);
    }

}
