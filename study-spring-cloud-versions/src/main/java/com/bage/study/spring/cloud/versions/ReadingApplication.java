package com.bage.study.spring.cloud.versions;

import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@SpringBootApplication
public class ReadingApplication {

    @Autowired
    private BookService bookService;
    @Autowired
    private Book2Service book2Service;

    @RequestMapping("/to-read")
    public Mono<String> toRead() {
        return bookService.readingList();
    }

    @RequestMapping("/to-read2")
    public Mono<String> toRead2() {
        return book2Service.readingList();
    }

    public static void main(String[] args) {
        SpringApplication.run(ReadingApplication.class, args);
    }
}