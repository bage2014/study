//package com.bage.study.spring.boot3.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.access.expression.SecurityExpressionHandler;
//import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
//import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
//import org.springframework.security.authorization.AuthorityAuthorizationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
//import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig2 {
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("user")
//                .roles("USER")
//                .build());
//
//        manager.createUser(User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin")
//                .roles("ADMIN")
//                .build());
//        return manager;
//    }
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(authorize -> authorize
//
//                        .requestMatchers("/api/admin/**")
//                        .access(AuthorityAuthorizationManager.hasRole("ADMIN"))
//
//                        .requestMatchers("/api/user/**")
//                        .access(AuthorityAuthorizationManager.hasRole("USER"))
//
//                        .requestMatchers("/api/anonymous/**")
//                        .anonymous()
//
//                        .anyRequest().authenticated()
//                )
////                .expressionHandler(webExpressionHandler())
//                .formLogin(withDefaults())
//                .httpBasic(withDefaults());
//        return http.build();
//    }
//
//    @Bean
//    public AuthorityAuthorizationManager<RequestAuthorizationContext>
//    guestAuthorityAuthorizationManager() {
//        AuthorityAuthorizationManager<RequestAuthorizationContext>
//                objectAuthorityAuthorizationManager =
//                AuthorityAuthorizationManager.hasAuthority("GUEST");
//        objectAuthorityAuthorizationManager.setRoleHierarchy(roleHierarchy());
//        return objectAuthorityAuthorizationManager;
//    }
//
//    public SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
//        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
//        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
//        return defaultWebSecurityExpressionHandler;
//    }
//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
//        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_STAFF > ROLE_USER > ROLE_GUEST");
//        return hierarchy;
//    }
//}