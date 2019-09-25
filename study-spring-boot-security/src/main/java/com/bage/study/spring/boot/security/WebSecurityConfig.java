package com.bage.study.spring.boot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	  @Autowired
	  private MyAuthenticationProvider provider;// 自定义验证

    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

	  @Autowired
	  public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception{
		  auth.authenticationProvider(provider);
	  }
	


	@Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .authorizeRequests()
                .antMatchers("/", "/home").permitAll()

				.antMatchers("/org/**").hasRole("USER")
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")

				.anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
        http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
    }

    // 角色继承

    @Bean
    public RoleHierarchyVoter roleHierarchyVoter(){
        return new RoleHierarchyVoter(roleHierarchyImpl());
    }

    @Bean
    public RoleHierarchyImpl roleHierarchyImpl(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        DefaultFilterInvocationSecurityMetadataSource dd;
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }




}