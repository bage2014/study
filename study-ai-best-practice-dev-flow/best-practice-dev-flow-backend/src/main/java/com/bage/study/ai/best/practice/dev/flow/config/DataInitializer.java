package com.bage.study.ai.best.practice.dev.flow.config;

import com.bage.study.ai.best.practice.dev.flow.entity.User;
import com.bage.study.ai.best.practice.dev.flow.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("初始化默认用户数据...");
            
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            userRepository.save(admin);
            log.info("创建默认用户: admin");

            User user1 = new User();
            user1.setUsername("user1");
            user1.setEmail("user1@example.com");
            user1.setPassword(passwordEncoder.encode("user123"));
            userRepository.save(user1);
            log.info("创建默认用户: user1");

            User user2 = new User();
            user2.setUsername("user2");
            user2.setEmail("user2@example.com");
            user2.setPassword(passwordEncoder.encode("user234"));
            userRepository.save(user2);
            log.info("创建默认用户: user2");

            log.info("默认用户数据初始化完成");
        } else {
            log.info("数据库已有用户数据，跳过初始化");
        }
    }
}