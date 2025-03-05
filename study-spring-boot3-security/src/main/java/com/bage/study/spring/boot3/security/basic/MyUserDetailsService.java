package com.bage.study.spring.boot3.security.basic;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if("zhangsan".equals(username)){
            return User.withDefaultPasswordEncoder()
                    .username("zhangsan")
                    .password("admin")
                    .roles("ADMIN")
                    .build();
        }
        if("lisi".equals(username)){
            return User.withDefaultPasswordEncoder()
                    .username("lisi")
                    .password("admin")
                    .roles("USER")
                    .build();
        }
        return null;
    }
}
