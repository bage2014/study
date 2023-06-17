package com.bage.study.resilience4j;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;

import java.time.Duration;

public class LimitTest {

    public static void main(String[] args) {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMillis(1))
                .limitForPeriod(10)
                .timeoutDuration(Duration.ofMillis(25))
                .build();

// Create registry
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);

// Use registry
        RateLimiter rateLimiter = rateLimiterRegistry
                .rateLimiter("name2", config);
        rateLimiterRegistry.getEventPublisher()
                .onEntryAdded(entryAddedEvent -> {
                    RateLimiter addedRateLimiter = entryAddedEvent.getAddedEntry();
                    System.out.println("RateLimiter {} added" + addedRateLimiter.getName());
                })
                .onEntryRemoved(entryRemovedEvent -> {
                    RateLimiter removedRateLimiter = entryRemovedEvent.getRemovedEntry();
                    System.out.println("RateLimiter {} removed" + removedRateLimiter.getName());
                });

        rateLimiter.getEventPublisher()
                .onSuccess(event ->    System.out.println("RateLimiter {} onSuccess" + event.getRateLimiterName()))
                .onFailure(event ->    System.out.println("RateLimiter {} onFailure" + event.getRateLimiterName()));

        BackendService backendService = new BackendServiceImpl();


        // Decorate your call to BackendService.doSomething()
        CheckedRunnable restrictedCall = RateLimiter
                .decorateCheckedRunnable(rateLimiter, backendService::doSomething);

        for (int i = 0; i < 100; i++) {
            Try<Void> voids = Try.run(restrictedCall)
                    .andThenTry(restrictedCall);
        }


    }

}
