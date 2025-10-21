package com.bage.my.app.end.point.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import com.bage.my.app.end.point.entity.User;
import com.bage.my.app.end.point.entity.UserToken;
import com.bage.my.app.end.point.repository.UserRepository;
import com.bage.my.app.end.point.entity.ApiResponse;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import com.bage.my.app.end.point.dto.LoginRequest;
import com.bage.my.app.end.point.dto.RegisterRequest;
import com.bage.my.app.end.point.dto.EmailCaptchaRequest;

import com.bage.my.app.end.point.entity.CaptchaInfo;
import com.bage.my.app.end.point.dto.ResetPasswordRequest;
import com.bage.my.app.end.point.dto.LoginResponse;
import com.bage.my.app.end.point.repository.UserTokenRepository;
import com.bage.my.app.end.point.util.AuthUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bage.my.app.end.point.config.MailConfig;
import com.bage.my.app.end.point.dto.CheckTokenRequest;
import com.bage.my.app.end.point.dto.RefreshTokenRequest;
import com.bage.my.app.end.point.dto.QueryUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private CaptchaController captchaController;
    @Autowired
    private MailConfig mailConfig;

    @RequestMapping("/profile")
    public ApiResponse<User> profile() {
        try {
            // 获取当前登录用户ID
            Long userId = AuthUtil.getCurrentUserId();
            if (userId == null) {
                return new ApiResponse<>(401, "未登录", null);
            }

            // 查询用户信息
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return new ApiResponse<>(404, "用户不存在", null);
            }

            // 清除密码字段
            user.setPassword(null);

            return new ApiResponse<>(200, "成功", user);
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage());
            return new ApiResponse<>(500, "获取用户信息失败", null);
        }
    }

    @RequestMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("loginRequest: {}", loginRequest);
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String captcha = loginRequest.getCaptcha();
        User user = userRepository.findByEmail(username);

        if (user == null) {
            log.info("user not found");
            return new ApiResponse<>(404, "用户不存在", null);
        }
        // 检查账号是否锁定
        if (user.getLockTime() != null && LocalDateTime.now().isBefore(user.getLockTime())) {
            long minutesLeft = ChronoUnit.MINUTES.between(LocalDateTime.now(), user.getLockTime());
            log.info("account is locked, {} minutes left", minutesLeft);
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
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setUser(user);
                loginResponse.setUserToken(null);
                log.info("captcha is expired");
                return new ApiResponse<>(400, "验证码已过期，请重新获取", loginResponse);   
            }

            if (!captchaInfo.getCaptcha().equalsIgnoreCase(captcha)) {
                User rtn = new User();
                rtn.setLoginAttempts(user.getLoginAttempts());
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setUser(user);
                loginResponse.setUserToken(null);
                log.info("captcha is not match");
                return new ApiResponse<>(400, "验证码错误", loginResponse);
            }
        }

        // 验证密码
        if (user.getPassword().equals(password)) {
            // 登录成功逻辑
            // 生成token
            String token = user.getId() + "-" + UUID.randomUUID().toString();
            String refreshToken = user.getId() + "-refresh-" + UUID.randomUUID().toString();
            // LocalDateTime expireTime = LocalDateTime.now().plusDays(7);
            LocalDateTime expireTime = LocalDateTime.now().plusMinutes(7);
            LocalDateTime refreshExpireTime = LocalDateTime.now().plusDays(14);

            // 更新用户token信息
            UserToken userToken = new UserToken();

            userToken.setToken(token);
            userToken.setRefreshToken(refreshToken);
            userToken.setTokenExpireTime(expireTime);
            userToken.setRefreshTokenExpireTime(refreshExpireTime);
            userToken.setUserId(user.getId());
            userTokenRepository.save(userToken);

            user.setLoginAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);

            // 构建返回数据
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUser(user);
            loginResponse.setUserToken(userToken);
            return new ApiResponse<>(200, "登录成功", loginResponse);
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
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setUser(user);
                loginResponse.setUserToken(null);
                log.info("login failed 5 times, account is locked");
                return new ApiResponse<>(403, "用户名或密码错误，账号已锁定1天", loginResponse);
            }

            userRepository.save(user);

            User rtn = new User();
            rtn.setLoginAttempts(user.getLoginAttempts());
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUser(user);
            loginResponse.setUserToken(null);
            log.info("login failed, {} times", attempts);
            return new ApiResponse<>(401, "用户名或密码错误，还有" + (5 - attempts) + "次机会", loginResponse );
        }
    }

    @RequestMapping("/register")
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

    @RequestMapping("/sendEmailCaptcha")
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
        message.setFrom(mailConfig.getMailUsername());
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
            user.setAvatarUrl("https://avatars.githubusercontent.com/u/18094768?s=400&u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&v=4");
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
            user.setPassword("lisi123");
            user.setLoginAttempts(0);
            user.setAvatarUrl("https://avatars.githubusercontent.com/u/18094768?s=400&u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&v=4");
            user.setLockTime(null);
            userRepository.save(user);
            System.out.println("默认用户 lisi 创建成功");
        }

        // 初始化wangwu
        if (userRepository.findByUsername("wangwu") == null) {
            User user = new User();
            user.setUsername("wangwu");
            user.setEmail("wangwu@qq.com");
            user.setPassword("wangwu123");
            user.setAvatarUrl("https://avatars.githubusercontent.com/u/18094768?s=400&u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&v=4");
            user.setLoginAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
            System.out.println("默认用户 wangwu 创建成功");
        }

        if(userRepository.findByUsername("hello") == null){
            User user = new User();
            user.setUsername("hello");
            user.setEmail("hello@qq.com");
            user.setPassword("hello123");
            user.setAvatarUrl("https://avatars.githubusercontent.com/u/18094768?s=400&u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&v=4");
            user.setLoginAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
            System.out.println("默认用户 hello 创建成功");
        }
    }

    // 校验token是否有效
    @RequestMapping("/checkToken")
    public ApiResponse<Boolean> checkToken(@RequestBody CheckTokenRequest request) {
        log.info("checkToken request:{}", request);
        UserToken user = userTokenRepository.findByToken(request.getToken());
        if (user == null || user.getTokenExpireTime() == null) {
            return new ApiResponse<>(401, "无效token", false);
        }

        boolean isValid = LocalDateTime.now().isBefore(user.getTokenExpireTime());
        log.info("checkToken isValid:{}", isValid);
        if (!isValid) {
            return new ApiResponse<>(401, "token已过期", false);
        }
        return new ApiResponse<>(200, "校验成功", isValid);
    }

    // 刷新token有效期
    @RequestMapping("/refreshToken")
    public ApiResponse<UserToken> refreshToken(@RequestBody RefreshTokenRequest request) {
        // 将原有代码中直接使用refreshToken的地方替换为request.getRefreshToken()
        String refreshToken = request.getRefreshToken();
        UserToken user = userTokenRepository.findByRefreshToken(refreshToken);
        if (user == null) {
            return new ApiResponse<>(401, "无效token", null);
        }
        if (user.getRefreshTokenExpireTime() == null) {
            return new ApiResponse<>(401, "无效token", null);
        }

        // 校验刷新token是否过期
        if (LocalDateTime.now().isAfter(user.getRefreshTokenExpireTime())) {
            return new ApiResponse<>(401, "刷新token已过期", null);
        }

        // 生成新token
        String token = user.getId() + "-" + UUID.randomUUID().toString();
        String newRefreshToken = user.getId() + "-refresh-" + UUID.randomUUID().toString();
        LocalDateTime expireTime = LocalDateTime.now().plusDays(7);

        user.setToken(token);
        user.setRefreshToken(newRefreshToken);
        user.setTokenExpireTime(expireTime);
        log.info("refreshToken user:{}", user);
        userTokenRepository.save(user);

        return new ApiResponse<>(200, "token刷新成功", user);
    }

    // 新增方法：完善用户信息
    @RequestMapping("/updateUserInfo")
    public ApiResponse<User> updateUserInfo(@RequestBody User userInfo) {
        Long userId = AuthUtil.getCurrentUserId();
        log.info("updateUserInfo userId:{}", userId);
        // 1. 检查用户是否存在
        User user = userRepository.findById(userId).orElse(null);
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
    @RequestMapping("/resetPassword")
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

    /**
     * 查询用户列表（支持分页和关键词搜索）
     */
    @RequestMapping("/queryUsers")
    public ApiResponse<QueryUserResponse> queryUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Page<User> userPage = null;
            Pageable pageable = PageRequest.of(page, size);
            if (keyword != null && !keyword.isEmpty()) {
                userPage = userRepository.findByKeyword(keyword, pageable);
            } else {
                userPage = userRepository.findAll(pageable);
            }
            
            QueryUserResponse response = new QueryUserResponse(
                userPage.getContent(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.getNumber(),
                userPage.getSize()
            );
            return new ApiResponse<>(200, "查询成功", response);
            
        } catch (Exception e) {
            log.error("查询用户列表失败: {}", e.getMessage());
            return new ApiResponse<>(500, "查询用户列表失败", null);
        }
    }
}