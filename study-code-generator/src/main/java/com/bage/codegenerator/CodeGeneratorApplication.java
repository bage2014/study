package com.bage.codegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = CodeGeneratorApplication.class)
public class CodeGeneratorApplication {

    public static void main(String[] args) {
    	SpringApplication.run(CodeGeneratorApplication.class, args);
    }

}