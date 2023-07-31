package com.bage.study.best.practice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证内存泄漏
 *
 */
@RequestMapping("/thread/local")
@RestController
@Slf4j
public class ThreadLocalController {



}
