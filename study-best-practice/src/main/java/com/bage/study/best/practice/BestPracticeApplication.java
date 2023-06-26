package com.bage.study.best.practice;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@MapperScan("com.bage.study.best.practice.repo")
public class BestPracticeApplication extends SpringBootServletInitializer implements CommandLineRunner {

    public static void main(String args[]) {
        SpringApplication.run(BestPracticeApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BestPracticeApplication.class);
    }


//    @Autowired
//    private StringRedisTemplate template;
    @Resource(name="redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Override
    public void run(String... strings) throws Exception {

        System.out.println(("----- BestPracticeApplication is started ------"));


        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//        rule.setControlBehavior()
        // Set limit QPS to 20.
        rule.setCount(600);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);

        valueOperations.set("foo","bar bar");

        System.out.println(valueOperations.get("foo"));
    }

}