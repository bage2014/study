package com.bage.study.resilience4j;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;

import java.util.function.Supplier;

public class IntegrationAllTest {

    public static void main(String[] args) {
        BackendService backendService = new BackendServiceImpl();

        // Create a CircuitBreaker with default configuration
        CircuitBreaker circuitBreaker = CircuitBreaker
                .ofDefaults("backendService");

// Create a Retry with default configuration
// 3 retry attempts and a fixed time interval between retries of 500ms
        Retry retry = Retry
                .ofDefaults("backendService");

// Create a Bulkhead with default configuration
        Bulkhead bulkhead = Bulkhead
                .ofDefaults("backendService");

        Supplier<String> supplier = () -> backendService
                .doSomething();

// Decorate your call to backendService.doSomething()
// with a Bulkhead, CircuitBreaker and Retry
// **note: you will need the resilience4j-all dependency for this
        Supplier<String> decoratedSupplier = Decorators.ofSupplier(supplier)
                .withCircuitBreaker(circuitBreaker)
                .withBulkhead(bulkhead)
                .withRetry(retry)
                .decorate();

        for (int i = 0; i < 100; i++) {
            try {
                System.out.println(i + ";" + decoratedSupplier.get());
            }catch (Exception e){

            }
        }
    }
}
