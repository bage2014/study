package com.bage.study.spring.boot.oauth2.server;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@RestController
@EnableOAuth2Client
@EnableAuthorizationServer
public class SocialApplication extends WebSecurityConfigurerAdapter {

	@RequestMapping({ "/user", "/me" })
	public Map<String, String> user(Principal principal) {
	  Map<String, String> map = new LinkedHashMap<String, String>();
	  map.put("name", principal.getName());
	  return map;
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(SocialApplication.class, args);
	}
	
	 @Autowired
	  private MyAuthenticationProvider provider;//自定义验证


	  @Autowired
	  public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception{
		  auth.authenticationProvider(provider);
	  }
	


	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  
//		 http.requestMatchers()
//         .antMatchers("/login", "/oauth/authorize","/")
//         .and()
//         .authorizeRequests()
//         .anyRequest()
//         .authenticated()
//         .and()
//         .formLogin()
//         .permitAll();
		http.antMatcher("/**")                                       //(1)
	    .authorizeRequests()
	      .antMatchers("/", "/home", "/webjars/**").permitAll() //(2)
	      .anyRequest().authenticated()                            //(3)
//	    .and().exceptionHandling()
//	      .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/")) //(4)
	      .and().formLogin()
          .loginPage("/login")
          .permitAll()
          .and()
      .logout()
          .permitAll();
	}

	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(
	    OAuth2ClientContextFilter filter) {
	  FilterRegistrationBean registration = new FilterRegistrationBean();
	  registration.setFilter(filter);
	  registration.setOrder(-100);
	  return registration;
	}
	
	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration
	    extends ResourceServerConfigurerAdapter {
	  @Override
	  public void configure(HttpSecurity http) throws Exception {
	    http
	      .antMatcher("/me")
	      .authorizeRequests().anyRequest().authenticated();
	  }
	}
}

