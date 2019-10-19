package com.bage.study.spring.boot.oauth2.server.v3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    //@Autowired
    //private MyUserDetailsService myUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.authorizeRequests()
                .antMatchers("/oauth/**","/login/**","/webjars/**",  "/logout").permitAll()
                .anyRequest().authenticated()   // 其他地址的访问均需验证权限
                .and()
                .formLogin()
//                .loginPage("/login")
                .and()
                .logout().logoutSuccessUrl("/")
                .and().httpBasic()
                .and().csrf().disable().exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**");
    }

//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //@formatter:off
//        auth.inMemoryAuthentication()
//                .withUser("john").password(passwordEncoder.encode("123")).authorities("ROLE_USER", "ROLE_ADMIN")
//                .and()
//                .withUser("izzy").password(passwordEncoder.encode("password")).authorities("ROLE_USER");
//        //@formatter:on
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder);


        auth.inMemoryAuthentication()
                .withUser("john")
                .password(passwordEncoder.encode("123"))
                .roles("USER")
        .and().withUser("bage")
                .password(passwordEncoder.encode("bage"))
                .roles("ADMIN")
                ;

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}