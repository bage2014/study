package com.bage.demo;

import com.bage.demo.entity.User;
import com.bage.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testSaveAndFindUser() {
        User user = new User();
        user.setName("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("testuser");

        User foundUser = userRepository.findByEmail("test@example.com").orElse(null);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");

        userRepository.delete(savedUser);
    }

    @Test
    void testDatabaseConnection() {
        long count = userRepository.count();
        assertThat(count).isGreaterThanOrEqualTo(0);
    }
}