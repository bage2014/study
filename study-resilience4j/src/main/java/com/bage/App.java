package com.bage;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

// Create a CircuitBreaker with default configuration
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("backendService");

// Create a Retry with default configuration
// 3 retry attempts and a fixed time interval between retries of 500ms
        Retry retry = Retry.ofDefaults("backendService");

// Create a Bulkhead with default configuration
        Bulkhead bulkhead = Bulkhead.ofDefaults("backendService");

        Supplier<String> supplier = () -> backendService
                .doSomething(param1, param2);

// Decorate your call to backendService.doSomething()
// with a Bulkhead, CircuitBreaker and Retry
// **note: you will need the resilience4j-all dependency for this
        Supplier<String> decoratedSupplier = Decorators.ofSupplier(supplier)
                .withCircuitBreaker(circuitBreaker)
                .withBulkhead(bulkhead)
                .withRetry(retry)
                .decorate();

// Execute the decorated supplier and recover from any exception
        String result = Try.ofSupplier(decoratedSupplier)
                .recover(throwable -> "Hello from Recovery").get();

// When you don't want to decorate your lambda expression,
// but just execute it and protect the call by a CircuitBreaker.
        String result = circuitBreaker
                .executeSupplier(backendService::doSomething);

// You can also run the supplier asynchronously in a ThreadPoolBulkhead
        ThreadPoolBulkhead threadPoolBulkhead = ThreadPoolBulkhead
                .ofDefaults("backendService");

// The Scheduler is needed to schedule a timeout on a non-blocking CompletableFuture
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
        TimeLimiter timeLimiter = TimeLimiter.of(Duration.ofSeconds(1));

        CompletableFuture<String> future = Decorators.ofSupplier(supplier)
                .withThreadPoolBulkhead(threadPoolBulkhead)
                .withTimeLimiter(timeLimiter, scheduler)
                .withCircuitBreaker(circuitBreaker)
                .withFallback(asList(TimeoutException.class, CallNotPermittedException.class, BulkheadFullException.class),
                        throwable -> "Hello from Recovery")
                .get().toCompletableFuture();

    }
}
