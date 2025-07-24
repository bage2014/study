package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.CaptchaInfo;
import com.google.code.kaptcha.Producer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CaptchaService {
    @Autowired
    private Producer kaptchaProducer;

    // 用于存储验证码的内存缓存
    private final Map<String, CaptchaInfo> captchaCache = new ConcurrentHashMap<>();


    // 定时清理过期的验证码
    @PostConstruct
    public void init() {
        // 每10分钟清理一次过期验证码
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(6000); // 1分钟
                    captchaCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    // 生成验证码文本并存储
    public String generateAndStoreCaptcha(String requestId) {
        // 生成验证码文本
        String captchaText = kaptchaProducer.createText();
        // 存储验证码到内存缓存，设置5分钟有效期
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);
        captchaCache.put(requestId, new CaptchaInfo(captchaText, expireTime));
        return captchaText;
    }

    // 方法用于验证验证码
    public boolean validateCaptcha(String requestId, String captcha) {
        CaptchaInfo captchaInfo = captchaCache.get(requestId);
        if (captchaInfo == null || captchaInfo.isExpired()) {
            return false;
        }
        return captchaInfo.getCaptcha().equalsIgnoreCase(captcha);
    }

    // 方法用于获取验证码
    public CaptchaInfo getCaptcha(String requestId) {
        return captchaCache.get(requestId);
    }

    // 方法用于获取邮箱验证码
    public CaptchaInfo getMailCaptcha(String requestId) {
        return captchaCache.get("email_" + requestId);
    }

    // 方法用于缓存邮箱验证码
    public void cacheMailCaptcha(String requestId, String captcha) {
        captchaCache.put("email_" + requestId, new CaptchaInfo(captcha, LocalDateTime.now().plusMinutes(5)));
    }

}