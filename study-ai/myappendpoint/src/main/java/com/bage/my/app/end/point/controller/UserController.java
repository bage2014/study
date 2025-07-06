package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.User;
import com.bage.my.app.end.point.repository.UserRepository;
import com.google.code.kaptcha.Producer;
import javax.imageio.ImageIO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Producer kaptchaProducer;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, 
                       @RequestParam(required = false) String captcha, HttpSession session) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "用户不存在";
        }

        // 检查账号是否锁定
        if (user.getLockTime() != null && LocalDateTime.now().isBefore(user.getLockTime())) {
            long minutesLeft = ChronoUnit.MINUTES.between(LocalDateTime.now(), user.getLockTime());
            return "账号已锁定，请" + minutesLeft + "分钟后再试";
        }

        // 登录失败1次后需要验证码
        boolean needCaptcha = user.getLoginAttempts() >= 1;
        if (needCaptcha) {
            String storedCaptcha = (String) session.getAttribute("captcha");
            if (storedCaptcha == null || !storedCaptcha.equalsIgnoreCase(captcha)) {
                return "验证码错误或已过期";
            }
        }

        // 验证密码
        if (user.getPassword().equals(password)) {
            // 登录成功，重置失败次数
            user.setLoginAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
            session.removeAttribute("captcha");
            return "登录成功";
        } else {
            // 登录失败，更新失败次数
            int attempts = user.getLoginAttempts() + 1;
            user.setLoginAttempts(attempts);

            // 失败5次锁定账号1天
            if (attempts >= 5) {
                user.setLockTime(LocalDateTime.now().plusDays(1));
                userRepository.save(user);
                return "密码错误次数过多，账号已锁定1天";
            }

            userRepository.save(user);
            return "用户名或密码错误，还有" + (5 - attempts) + "次机会";
        }
    }

    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, HttpSession session) throws Exception {
        // 设置响应头
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);

        // 生成验证码文本
        String captchaText = kaptchaProducer.createText();
        // 存储验证码到session
        session.setAttribute("captcha", captchaText);

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

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        if (userRepository.findByUsername(username) != null) {
            return "用户名已存在";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
        return "注册成功";
    }
}