package com.bage.my.app.end.point.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import com.bage.my.app.end.point.entity.User;
import com.bage.my.app.end.point.repository.UserRepository;
import com.bage.my.app.end.point.entity.ApiResponse;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import com.bage.my.app.end.point.dto.LoginRequest;
import com.bage.my.app.end.point.dto.RegisterRequest;
import com.bage.my.app.end.point.dto.EmailCaptchaRequest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import com.bage.my.app.end.point.entity.CaptchaInfo;
import com.bage.my.app.end.point.dto.ResetPasswordRequest;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private CaptchaController captchaController;

    @PostMapping("/login")
    public ApiResponse<User> login(@RequestBody LoginRequest loginRequest) {
        log.info("loginRequest: {}", loginRequest);
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String captcha = loginRequest.getCaptcha();
        User user = userRepository.findByEmail(username);

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
            CaptchaInfo captchaInfo = captchaController.getCaptcha(loginRequest.getRequestId());

            // 检查验证码是否过期（5分钟）
            if (captchaInfo == null || captchaInfo.getCaptcha() == null) {
                User rtn = new User();
                rtn.setLoginAttempts(user.getLoginAttempts());
                return new ApiResponse<>(400, "验证码已过期，请重新获取", rtn);
            }

            if (!captchaInfo.getCaptcha().equalsIgnoreCase(captcha)) {
                User rtn = new User();
                rtn.setLoginAttempts(user.getLoginAttempts());
                return new ApiResponse<>(400, "验证码错误", rtn);
            }
        }

        // 验证密码
        if (user.getPassword().equals(password)) {
            // 登录成功逻辑
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
            return new ApiResponse<>(200, "登录成功", user);
        } else {
            // 登录失败，更新失败次数
            int attempts = user.getLoginAttempts() + 1;
            user.setLoginAttempts(attempts);

            // 失败5次锁定账号1天
            if (attempts >= 5) {
                user.setLockTime(LocalDateTime.now().plusDays(1));
                userRepository.save(user);
                User rtn = new User();
                rtn.setLoginAttempts(user.getLoginAttempts());
                return new ApiResponse<>(403, "用户名或密码错误，账号已锁定1天", rtn);
            }

            userRepository.save(user);

            User rtn = new User();
            rtn.setLoginAttempts(user.getLoginAttempts());
            return new ApiResponse<>(401, "用户名或密码错误，还有" + (5 - attempts) + "次机会", rtn );
        }
    }

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody RegisterRequest registerRequest) {
        // 1. 验证验证码
        String captcha = registerRequest.getCaptcha();

        if (captcha == null) {
            return new ApiResponse<>(400, "验证码不能为空", null);
        }

        CaptchaInfo captchaInfo = captchaController.getMailCaptcha(registerRequest.getEmail());

        if (captchaInfo == null || captchaInfo.isExpired()) {
            return new ApiResponse<>(400, "验证码已过期，请重新获取", null);
        }

        if (!captchaInfo.getCaptcha().equalsIgnoreCase(captcha)) {
            return new ApiResponse<>(400, "验证码错误", null);
        }

        // 2. 验证通过后，创建用户并保存到数据库
        User user = new User();
        user.setUsername(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        // 添加新字段赋值
        user.setEmail(registerRequest.getEmail());
        userRepository.save(user);

        return new ApiResponse<>(200, "注册成功", null);
    }

    @PostMapping("/sendEmailCaptcha")
    public ApiResponse<String> sendEmailCaptcha(@RequestBody EmailCaptchaRequest request) {
        // 验证码校验
        CaptchaInfo captchaInfo = captchaController.getCaptcha(request.getRequestId());

        if (captchaInfo == null || captchaInfo.isExpired()) {
            return new ApiResponse<>(400, "验证码已过期，请重新获取", null);
        }

        if (!captchaInfo.getCaptcha().equalsIgnoreCase(request.getCaptcha())) {
            return new ApiResponse<>(400, "验证码错误", null);
        }

        // 生成6位随机验证码
        String emailCaptcha = String.valueOf((int)(Math.random() * 900000) + 100000);

        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("您的验证码");
        message.setText("您的验证码是: " + emailCaptcha + "，有效期为5分钟");
        log.info("sendEmailCaptcha emailCaptcha:{}", emailCaptcha);
        // todo bage fix 邮件发送
        mailSender.send(message);

        // 存储邮箱验证码到缓存
        captchaController.cacheMailCaptcha(request.getEmail(), emailCaptcha);

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
            user.setEmail("zhangsan@qq.com");
            user.setLoginAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
            System.out.println("默认用户 zhangsan 创建成功");
        }

        // 初始化lisi
        if (userRepository.findByUsername("lisi") == null) {
            User user = new User();
            user.setUsername("lisi");
            user.setEmail("lisi@qq.com");
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
            user.setEmail("wangwu@qq.com");
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

        // 生成新token
        String newToken = UUID.randomUUID().toString();
        user.setToken(newToken);

        // 延长有效期7天
        user.setTokenExpireTime(LocalDateTime.now().plusDays(7));
        userRepository.save(user);

        return new ApiResponse<>(200, "token刷新成功", user.getToken());
    }

    // 新增方法：完善用户信息
    @PostMapping("/updateUserInfo")
    public ApiResponse<User> updateUserInfo(@RequestBody User userInfo) {
        // 1. 检查用户是否存在
        User user = userRepository.findById(userInfo.getId()).orElse(null);
        if (user == null) {
            return new ApiResponse<>(404, "用户不存在", null);
        }

        // 2. 更新用户信息
        if (userInfo.getUsername() != null) {
            user.setUsername(userInfo.getUsername());
        }
        if (userInfo.getGender() != null) {
            user.setGender(userInfo.getGender());
        }
        if (userInfo.getBirthDate() != null) {
            user.setBirthDate(userInfo.getBirthDate());
        }

        // 3. 保存更新后的用户信息
        User updatedUser = userRepository.save(user);

        // 4. 返回更新后的用户信息
        return new ApiResponse<>(200, "用户信息更新成功", updatedUser);
    }

    // 新增方法：重置密码
    @PostMapping("/resetPassword")
    public ApiResponse<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        // 1. 检查参数是否有效
        if (request.getEmail() == null || request.getCaptcha() == null || request.getNewPassword() == null) {
            return new ApiResponse<>(400, "参数不能为空", null);
        }

        // 2. 根据邮箱查找用户
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return new ApiResponse<>(404, "用户不存在", null);
        }

        // 3. 验证验证码
        CaptchaInfo captchaInfo = captchaController.getMailCaptcha(request.getEmail());
        if (captchaInfo == null || captchaInfo.isExpired()) {
            return new ApiResponse<>(400, "验证码已过期，请重新获取", null);
        }

        if (!captchaInfo.getCaptcha().equalsIgnoreCase(request.getCaptcha())) {
            return new ApiResponse<>(400, "验证码错误", null);
        }

        // 4. 重置密码
        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return new ApiResponse<>(200, "密码重置成功", null);
    }

}