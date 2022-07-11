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
public class HelloSentinelAnna {

    // spring boot ？？
    public static void main(String[] args) {
        // 配置规则.
        String hh = new HelloSentinelAnna().getUserById("hh");

        initFlowRules();
    }

    // 原本的业务方法.
    @SentinelResource(blockHandler = "blockHandlerForGetUser")
    public String getUserById(String id) {
        throw new RuntimeException("getUserById command failed");
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
