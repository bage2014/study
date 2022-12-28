package com.bage.study.spring.cloud.versions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class Book2Service {

  private static final Logger LOG = LoggerFactory.getLogger(Book2Service.class);


  private final WebClient webClient;
  private final ReactiveCircuitBreaker readingListCircuitBreaker;

  public Book2Service(ReactiveCircuitBreakerFactory circuitBreakerFactory) {
    this.webClient = WebClient.builder().baseUrl("http://localhost:8090").build();
    this.readingListCircuitBreaker = circuitBreakerFactory.create("recommended2");
  }

  public Mono<String> readingList() {
    return readingListCircuitBreaker.run(webClient.get().uri("/recommended2").retrieve().bodyToMono(String.class), throwable -> {
      LOG.warn("Error making request to book service", throwable);
      return Mono.just("Cloud Native Java (O'Reilly)");
    });
  }

}