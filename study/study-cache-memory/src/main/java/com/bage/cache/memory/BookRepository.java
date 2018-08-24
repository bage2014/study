package com.bage.cache.memory;

public interface BookRepository {

    Book getByIsbn(String isbn);

}