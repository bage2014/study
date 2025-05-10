package com.bage.study.spring.boot.uri;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class UriUtils {

    public static void main(String[] args) {
        // 构建URI
        URI uri = UriComponentsBuilder.fromHttpUrl("http://example.com")
                .path("/products")
                .queryParam("category", "books")
                .queryParam("sort", "price")
                .build()
                .toUri();

        System.out.println(uri.toString());
    }
}
