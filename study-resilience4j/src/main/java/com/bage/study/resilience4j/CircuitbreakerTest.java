package com.bage.study.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/**
 * https://resilience4j.readme.io/docs/examples
 */
public class CircuitbreakerTest {


    public static void main(String[] args) throws IOException {
        BackendService backendService = new BackendServiceImpl();
// Create a custom configuration for a CircuitBreaker
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .slidingWindowSize(2)
                .recordExceptions(IOException.class, TimeoutException.class)
//                .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                .build();

// Create a CircuitBreakerRegistry with a custom global configuration
        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);


        CircuitBreaker circuitBreaker = circuitBreakerRegistry
                .circuitBreaker("name");

        int n = 100;
        for (int i = 0; i < n; i++) {
            Supplier<String> decoratedSupplier = CircuitBreaker
                    .decorateSupplier(circuitBreaker,
                            backendService::doSomething);

//            String result = Try.ofSupplier(decoratedSupplier)
//                    .recover(throwable -> "Hello from Recovery").get();
//            System.out.println("result" + i + ":" + result);
        }
    }

}
