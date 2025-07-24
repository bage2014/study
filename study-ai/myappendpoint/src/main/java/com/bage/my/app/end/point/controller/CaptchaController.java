package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.CaptchaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.code.kaptcha.Producer;
import javax.imageio.ImageIO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import com.bage.my.app.end.point.service.CaptchaService;

@RestController
public class CaptchaController {
    @Autowired
    private Producer kaptchaProducer;
    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, 
                          @RequestParam String requestId) throws Exception {
        // 设置响应头
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);

        // 生成验证码文本并存储
        String captchaText = captchaService.generateAndStoreCaptcha(requestId);

        // 生成验证码图片
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(kaptchaProducer.createImage(captchaText), "jpg", out);

        // 关闭流
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    // 方法用于验证验证码
    public boolean validateCaptcha(String requestId, String captcha) {
        return captchaService.validateCaptcha(requestId, captcha);
    }

    // 方法用于获取验证码
    public CaptchaInfo getCaptcha(String requestId) {
        return captchaService.getCaptcha(requestId);
    }

    // 方法用于获取邮箱验证码
    public CaptchaInfo getMailCaptcha(String requestId) {
        return captchaService.getMailCaptcha(requestId);
    }

    // 方法用于缓存邮箱验证码
    public void cacheMailCaptcha(String requestId, String captcha) {
        captchaService.cacheMailCaptcha(requestId, captcha);
    }
}