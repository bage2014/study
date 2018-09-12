package com.bage.cache.memory.springboot;

public interface BookRepository {

    Book getByIsbn(String isbn);

}