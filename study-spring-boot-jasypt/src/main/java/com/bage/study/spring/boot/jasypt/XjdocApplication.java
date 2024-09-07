package com.bage.study.spring.boot.jasypt;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties  //开启自动解密功能
public class XjdocApplication {
 public static void main(String[] args) {
  SpringApplication.run(XjdocApplication.class, args);
 }
}
