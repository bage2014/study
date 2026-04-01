package com.familytree.config;

import com.familytree.model.User;
import com.familytree.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Component
@Slf4j
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 检查是否已有用户数据
        if (userRepository.count() == 0) {
            // 创建默认用户
            createDefaultUser("bage", "bage@qq.com", "bage1234");
            createDefaultUser("admin", "admin@qq.com", "admin");
            createDefaultUser("zhangsan", "zhangsan@qq.com", "zhangsan");
        }
    }

    private void createDefaultUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreatedAt(new Date());
        userRepository.save(user);
        log.info("Created default user: {}", username);
    }
}
