package com.bage.study.best.practice;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@MapperScan("com.bage.study.best.practice.repo")
@Slf4j
@EnableAsync
public class BestPracticeApplication implements CommandLineRunner {

    public static void main(String args[]) {
        SpringApplication.run(BestPracticeApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println(("----- BestPracticeApplication is started ------"));
        log.info(("----- BestPracticeApplication is started ------"));
        limitFlow();

    }

    private void limitFlow() {
        List<FlowRule> rules = new ArrayList<>();

//        FlowRule rule = new FlowRule();
//        rule.setResource("HelloWorld");
//        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
////        rule.setControlBehavior()
//        // Set limit QPS to 20.
//        rule.setCount(600);
//        rules.add(rule);

        FlowRule log = new FlowRule();
        log.setResource("log");
        log.setGrade(RuleConstant.FLOW_GRADE_QPS);
        log.setCount(600);
        rules.add(log);

        FlowRuleManager.loadRules(rules);
    }

}