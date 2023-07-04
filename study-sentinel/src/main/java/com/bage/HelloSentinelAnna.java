package com.bage;

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
public class HelloSentinelAnna {

    // spring boot ？？
    public static void main(String[] args) {
        // 配置规则.
        initFlowRules();

        HelloSentinelAnna ann = new HelloSentinelAnna();
        int i = 0;
        while (i < 100) {
            System.out.print(i + ": ");
            // 1.5.0 版本开始可以直接利用 try-with-resources 特性

            String hh = ann.getUserById("hh");
            System.out.println(hh);
            i ++ ;
        }


    }

    // 原本的业务方法.
    @SentinelResource(value = "HelloWorld",blockHandler = "blockHandlerForGetUser")
    public String getUserById(String id) {
        return "user-" + id;
//        throw new RuntimeException("getUserById command failed");
    }

    // blockHandler 函数，原方法调用被限流/降级/系统保护的时候调用
    public String blockHandlerForGetUser(String id, BlockException ex) {
        return "admin";
    }

    private static void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//        rule.setGrade(RuleConstant.DEFAULT_WINDOW_INTERVAL_MS);
        // Set limit QPS to 20.
        rule.setCount(2);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

}
