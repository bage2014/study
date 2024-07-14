package com.bage.study.springboot.advance;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * https://juejin.cn/post/7390336287956615194
 */
@SpringBootApplication
public class AdvanceApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AdvanceApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF); // 关闭 banner
        springApplication.setBanner(new CustomBanner()); // 自定义 banner
        springApplication.run(args);
    }

//    public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
//        this.beanNameGenerator = beanNameGenerator;
//    }

}
