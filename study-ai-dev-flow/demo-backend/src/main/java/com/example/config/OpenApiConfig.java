package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("订单管理 API")
                        .version("1.0")
                        .description("订单管理的增删改查功能")
                        .contact(new Contact().name("开发者"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}