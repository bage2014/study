package com.bage.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.stereotype.Component;

@Component
public class SimpleBookRepository implements BookRepository {

    @Cacheable("books")
    public Book getByIsbn(String isbn) {
        simulateSlowService();
        CacheAspectSupport vv;
        return new Book(isbn, "Some book");
    }

    // Don't do this at home
    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}