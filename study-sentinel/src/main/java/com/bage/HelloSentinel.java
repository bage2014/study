package com.bage;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class HelloSentinel {
    public static void main(String[] args) {
        // 配置规则.
        initFlowRules();

        int i = 0;
        while (i < 100) {
            System.out.print(i + ": ");
            // 1.5.0 版本开始可以直接利用 try-with-resources 特性
            // 异步
//            try (Entry entry = SphU.asyncEntry("HelloWorld")) {
            try (Entry entry = SphU.entry("HelloWorld")) {
                // 被保护的逻辑
                System.out.println("hello world");
            } catch (BlockException ex) {
                // 处理被流控的逻辑
                System.out.println("blocked!");
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i ++ ;
        }
    }

    private static void initFlowRules() {
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(2);


        List<FlowRule> rules = new ArrayList<>();
        rules.add(rule);
        /**
         * 做两件事：
         * 1、 更新配置
         * 2、 更定回调调用
         */
        FlowRuleManager.loadRules(rules);
    }

}
