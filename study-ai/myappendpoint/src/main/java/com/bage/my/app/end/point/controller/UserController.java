package com.bage.my.app.end.point.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import com.bage.my.app.end.point.entity.User;
import com.bage.my.app.end.point.repository.UserRepository;
import com.bage.my.app.end.point.entity.ApiResponse;
import com.google.code.kaptcha.Producer;
import javax.imageio.ImageIO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import com.bage.my.app.end.point.dto.LoginRequest;
import com.bage.my.app.end.point.dto.RegisterRequest;
import com.bage.my.app.end.point.dto.EmailCaptchaRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Producer kaptchaProducer;
    @Autowired
    private JavaMailSender mailSender;
    
    @PostMapping("/login")
    public ApiResponse<User> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        log.info("loginRequest: {}", loginRequest);
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String captcha = loginRequest.getCaptcha();
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            return new ApiResponse<>(404, "用户不存在", null);
        }
        
        // 检查账号是否锁定
        if (user.getLockTime() != null && LocalDateTime.now().isBefore(user.getLockTime())) {
            long minutesLeft = ChronoUnit.MINUTES.between(LocalDateTime.now(), user.getLockTime());
            return new ApiResponse<>(403, "账号已锁定，请" + minutesLeft + "分钟后再试", null);
        }
        
        // 登录失败1次后需要验证码
        boolean needCaptcha = user.getLoginAttempts() >= 1;
        if (needCaptcha) {
            String storedCaptcha = (String) session.getAttribute("captcha");
            LocalDateTime captchaTime = (LocalDateTime) session.getAttribute("captcha_time");
            
            // 检查验证码是否过期（5分钟）
            if (storedCaptcha == null || captchaTime == null || 
                ChronoUnit.MINUTES.between(captchaTime, LocalDateTime.now()) > 5) {
                return new ApiResponse<>(400, "验证码已过期，请重新获取", null);
            }
            
            if (!storedCaptcha.equalsIgnoreCase(captcha)) {
                return new ApiResponse<>(400, "验证码错误", null);
            }
        }
        
        // 验证密码
        if (user.getPassword().equals(password)) {
            // 生成token
            String token = UUID.randomUUID().toString();
            LocalDateTime expireTime = LocalDateTime.now().plusDays(7);
            
            // 更新用户token信息
            user.setToken(token);
            user.setTokenExpireTime(expireTime);
            user.setLoginAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
            
            // 构建返回数据
            session.removeAttribute("captcha");
            user.setPassword("");
            return new ApiResponse<>(200, "登录成功", user);
        } else {
            // 登录失败，更新失败次数
            int attempts = user.getLoginAttempts() + 1;
            user.setLoginAttempts(attempts);
    
            // 失败5次锁定账号1天
            if (attempts >= 5) {
                user.setLockTime(LocalDateTime.now().plusDays(1));
                userRepository.save(user);
                return new ApiResponse<>(403, "用户名或密码错误，账号已锁定1天", null);
            }
    
            userRepository.save(user);
            return new ApiResponse<>(401, "用户名或密码错误，还有" + (5 - attempts) + "次机会", null);
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
        // 存储验证码到session，设置5分钟有效期
        session.setAttribute("captcha", captchaText);
        session.setAttribute("captcha_time", LocalDateTime.now());
    
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
    public ApiResponse<String> register(@RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        // 添加新字段赋值
        user.setEmail(registerRequest.getEmail());
        user.setGender(registerRequest.getGender());
        user.setBirthDate(registerRequest.getBirthDate());
        user.setAvatarUrl(registerRequest.getAvatarUrl());
        userRepository.save(user);
        return new ApiResponse<>(200, "注册成功", null);
    }

    @PostMapping("/sendEmailCaptcha")
    public ApiResponse<String> sendEmailCaptcha(@RequestBody EmailCaptchaRequest request, HttpSession session) {
        // 验证码校验
        String storedCaptcha = (String) session.getAttribute("captcha");
        LocalDateTime captchaTime = (LocalDateTime) session.getAttribute("captcha_time");
        
        if (storedCaptcha == null || captchaTime == null || 
            ChronoUnit.MINUTES.between(captchaTime, LocalDateTime.now()) > 5) {
            return new ApiResponse<>(400, "验证码已过期，请重新获取", null);
        }
        
        if (!storedCaptcha.equalsIgnoreCase(request.getCaptcha())) {
            return new ApiResponse<>(400, "验证码错误", null);
        }
        
        // 生成6位随机验证码
        String emailCaptcha = String.valueOf((int)(Math.random() * 900000) + 100000);
        
        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("您的验证码");
        message.setText("您的验证码是: " + emailCaptcha + "，有效期为5分钟");
        mailSender.send(message);
        
        // 存储邮箱验证码到session
        session.setAttribute("email_captcha", emailCaptcha);
        session.setAttribute("email_captcha_time", LocalDateTime.now());
        session.setAttribute("email", request.getEmail());
        
        return new ApiResponse<>(200, "验证码已发送", null);
    }

    // 应用启动时自动创建默认用户
    @PostConstruct
    public void initDefaultUser() {
        // 初始化zhangsan
        if (userRepository.findByUsername("zhangsan") == null) {
            User user = new User();
            user.setUsername("zhangsan");
            user.setPassword("zhangsan123");
            user.setLoginAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
            System.out.println("默认用户 zhangsan 创建成功");
        }
        
        // 初始化lisi
        if (userRepository.findByUsername("lisi") == null) {
            User user = new User();
            user.setUsername("lisi");
            user.setPassword("lisi1234");
            user.setLoginAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
            System.out.println("默认用户 lisi 创建成功");
        }
        
        // 初始化wangwu
        if (userRepository.findByUsername("wangwu") == null) {
            User user = new User();
            user.setUsername("wangwu");
            user.setPassword("wangwu12345");
            user.setLoginAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
            System.out.println("默认用户 wangwu 创建成功");
        }
    }
    
    // 校验token是否有效
    @GetMapping("/checkToken")
    public ApiResponse<Boolean> checkToken(@RequestParam String token) {
        User user = userRepository.findByToken(token);
        if (user == null || user.getTokenExpireTime() == null) {
            return new ApiResponse<>(401, "无效token", false);
        }
        
        boolean isValid = LocalDateTime.now().isBefore(user.getTokenExpireTime());
        return new ApiResponse<>(200, "校验成功", isValid);
    }
    
    // 刷新token有效期
    @PostMapping("/refreshToken")
    public ApiResponse<String> refreshToken(@RequestParam String token) {
        User user = userRepository.findByToken(token);
        if (user == null) {
            return new ApiResponse<>(401, "无效token", null);
        }
        
        // 生成新token（可选）
        // String newToken = UUID.randomUUID().toString();
        // user.setToken(newToken);
        
        // 延长有效期7天
        user.setTokenExpireTime(LocalDateTime.now().plusDays(7));
        userRepository.save(user);
        
        return new ApiResponse<>(200, "token刷新成功", user.getToken());
    }
}