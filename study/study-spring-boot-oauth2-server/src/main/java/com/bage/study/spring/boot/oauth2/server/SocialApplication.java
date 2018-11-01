package com.bage.study.spring.boot.oauth2.server;

import java.security.Principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableOAuth2Client
@EnableAuthorizationServer
public class SocialApplication extends WebSecurityConfigurerAdapter {

	@RequestMapping("/login")
	public String user() {
		return "curl acme:acmesecret@localhsot:8088/oauth/token -d grant_type=client_credentials\r\n" + 
				"";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SocialApplication.class, args);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  http.antMatcher("/**")                                       //(1)
	    .authorizeRequests()
	      .antMatchers("/", "/login**", "/webjars/**").permitAll() //(2)
	      .anyRequest().authenticated()                            //(3)
	    .and().exceptionHandling()
	      .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/")) //(4)
	    ;
	}

}