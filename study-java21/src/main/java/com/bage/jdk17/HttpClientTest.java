package com.bage.jdk17;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpClientTest {

    public static void main(String[] args) throws Exception {
        basic();
    }


    private static void basic() throws IOException, InterruptedException {
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

    private static void async() throws InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://github.com/bage2014/study/tree/master/study-todo"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{}"))
//                .POST(HttpRequest.BodyPublishers.ofFile(Paths.get("file.json")))
                .build();
        HttpClient client = HttpClient.newBuilder()
//                .proxy() // 代理设置
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .whenComplete((response,throwable)->{
                    System.out.println(response.body());
                });
        Thread.sleep(3000L);
    }

}
