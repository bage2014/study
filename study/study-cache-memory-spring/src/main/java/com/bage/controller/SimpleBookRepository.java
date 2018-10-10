package com.bage.controller;

import org.springframework.stereotype.Component;

import com.bage.cache.annotation.MyCacheable;

@Component
public class SimpleBookRepository implements BookRepository {

    @Override
    @MyCacheable(key="books")
    public Book getByIsbn(String isbn) {
        simulateSlowService();
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

    @MyCacheable(key="'getBook'")
	@Override
	public Book get(Object param) {
		System.out.println("currentTimeMillis ：" + System.currentTimeMillis());
		return new Book(String.valueOf(param), "Some book");
	}

}