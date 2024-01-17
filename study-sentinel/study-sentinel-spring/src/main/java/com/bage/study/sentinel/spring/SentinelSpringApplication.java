package com.bage.study.sentinel.spring;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动时加入 JVM 参数 -Dcsp.sentinel.dashboard.server=consoleIp:port 指定控制台地址和端口。
 * -Dcsp.sentinel.dashboard.server=localhost:port
 * 若启动多个应用，则需要通过 -Dcsp.sentinel.api.port=xxxx 指定客户端监控 API 的端口（默认是 8719）。
 * 控制台下载 https://github.com/alibaba/Sentinel/releases
 *
 */
@SpringBootApplication
@RestController
public class SentinelSpringApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SentinelSpringApplication.class, args);
    }

    @RequestMapping("/hello")
    @SentinelResource("HelloWorld")
    public Object hello() {
        System.out.println("hello");
        return "hello";
    }

    @RequestMapping("/hello2")
    public Object hello2() {
        // 1.5.0 版本开始可以直接利用 try-with-resources 特性
        try (Entry entry = SphU.entry("HelloWorld")) {
            // 被保护的逻辑
            System.out.println("hello world");
        } catch (BlockException ex) {
            // 处理被流控的逻辑
            System.out.println("blocked!");
        }

        return "default";
    }


    @Override
    public void run(String... args) throws Exception {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(1);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}