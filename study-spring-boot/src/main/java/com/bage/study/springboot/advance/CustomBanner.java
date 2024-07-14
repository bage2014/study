package com.bage.study.springboot.advance;

import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

public class CustomBanner implements Banner {
    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        out.println("bage-bage-");
        out.println("bage-bage-");
        out.println("bage-bage-");
        out.println("bage-bage-");
        out.println("bage-bage-");
        out.println("bage-bage-");
        out.println("bage-bage-");
    }
}
