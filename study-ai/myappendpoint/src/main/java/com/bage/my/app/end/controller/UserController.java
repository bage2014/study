package com.bage.my.app.end.controller;

import com.bage.my.app.end.point.entity.User;
import com.bage.my.app.end.point.repository.UserRepository;
import com.google.code.kaptcha.Producer;
import jakarta.imageio.ImageIO;
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

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Producer kaptchaProducer;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, @RequestParam String captcha, HttpSession session) {
        // 验证码验证
        String storedCaptcha = (String) session.getAttribute("captcha");
        if (storedCaptcha == null || !storedCaptcha.equalsIgnoreCase(captcha)) {
            return "验证码错误或已过期";
        }
        
        // 用户验证
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // 验证成功后清除验证码
            session.removeAttribute("captcha");
            return "登录成功";
        } else {
            return "用户名或密码错误";
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