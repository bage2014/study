package com.bage.study.best.practice;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.bage.study.best.practice.trial.JvmGcController;
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
    private JvmGcController jvmGcController;

    public static void main(String args[]) {
        SpringApplication.run(BestPracticeApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println(("----- BestPracticeApplication is started ------"));
//        log.info(("----- BestPracticeApplication is started ------"));
        limitFlow();

        Thread thread = new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < 200; i++) {
                    try {
                        Thread.sleep(2000);
                        jvmGcController.add(1); // 每 N 秒，自动放
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("bage-command-hhhhhh is running-hotfix:" + i);
                }
            }
        };
        thread.setName("bage-command-hhhhhh");
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



        // JHHGHGHGH HSHGSHGJKHGKJ


        FlowRule log = new FlowRule();
        log.setResource("limit");
        log.setGrade(RuleConstant.FLOW_GRADE_QPS);
        log.setCount(200);
        rules.add(log);

        FlowRuleManager.loadRules(rules);
    }

}