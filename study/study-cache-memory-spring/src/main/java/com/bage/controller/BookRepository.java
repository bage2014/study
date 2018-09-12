package com.bage.controller;

public interface BookRepository {

    Book getByIsbn(String isbn);

}