package com.bage.study.java11;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientTest {

    public static void main(String[] args) throws Exception {
        HttpClient build = HttpClient.newBuilder()
//                .proxy() // 代理设置
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://github.com/bage2014/study/tree/master/study-todo"))
                .GET()
                .build();
        HttpResponse<String> send = build.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(send.body());


    }

}
