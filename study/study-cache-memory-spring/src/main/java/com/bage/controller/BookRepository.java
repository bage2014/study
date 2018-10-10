package com.bage.controller;

public interface BookRepository {

    Book getByIsbn(String isbn);
    Book get(Object param);
	Book getNoKey(Object param);
	void cacheEvict();

}