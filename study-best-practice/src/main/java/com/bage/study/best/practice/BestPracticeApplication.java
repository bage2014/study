package com.bage.study.best.practice;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.bage.study.best.practice.controller.UserController;
import com.bage.study.best.practice.trial.JvmGcController;
import com.bage.study.best.practice.trial.RedisController;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@MapperScan("com.bage.study.best.practice.repo")
@Slf4j
@EnableAsync
public class BestPracticeApplication implements CommandLineRunner {

    @Autowired
    private UserController userController;
    @Autowired
    private RedisController redisController;
    @Autowired
    private JvmGcController jvmGcController;

    public static void main(String args[]) {
//        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(BestPracticeApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        log.info(("----- BestPracticeApplication run ------"));
        limitFlow();

        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1; i++) {
                try {
                    Thread.sleep(1000);

                    jvmGcController.add(1); // 每 N 秒，自动放

                    Thread.sleep(1000);
                    redisController.randomSet();
                    Object random = redisController.randomGet();
                    System.out.println("BestPracticeApplication randomGet random = " + random);

                    Thread.sleep(1000);
                    userController.insert();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("BestPracticeApplication command is running:" + i);
            }
            log.info(("----- BestPracticeApplication started ------"));
        });
        log.info(("----- BestPracticeApplication stared ------"));
        thread.start();
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
        log.setResource("limit");
        log.setGrade(RuleConstant.FLOW_GRADE_QPS);
        log.setCount(200);
        rules.add(log);

        FlowRuleManager.loadRules(rules);
    }

}