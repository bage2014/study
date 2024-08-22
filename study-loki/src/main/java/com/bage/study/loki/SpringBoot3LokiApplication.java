package com.bage.study.loki;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
@Slf4j
public class SpringBoot3LokiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot3LokiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Thread thread = new Thread(() -> {
            for (int i = 0; i < 200; i++) {
                try {
                    Thread.sleep(5000);

                    String traceId = UUID.randomUUID().toString();
                    MDC.put("traceId", traceId);
                    log.info("traceId = {}, bage-command-loki is running: {}", traceId, LocalDateTime.now().toString());
                    MDC.clear();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setName("bage-command-loki");
        thread.start();

    }
}
