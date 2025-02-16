package com.bage.study.springboot.limiter;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.Random;

/**
 * https://juejin.cn/post/7445505687001186355
 * localhost:8080/api/log/stream
 */
@RestController
@RequestMapping("/api/log")
public class LogController {

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter streamLogs() {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        // 异步处理数据并发送
        new Thread(() -> {
            try {
                while (true) {
                    String logEntry = getLatestLogEntry();
                    if (logEntry != null) {
                        emitter.send(logEntry);
                    }
                    Thread.sleep(1000); // 每秒检查一次
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }

    private String getLatestLogEntry() {
        // 模拟从日志文件中获取最新日志条目
        return "2023-10-01 12:00:"+new Random().nextInt(100) +" - INFO: User logged in successfully.\n";
    }
}
