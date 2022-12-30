package com.bage.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class MySentinel {

    public static void main(String[] args) {

        final String resourceName = "hello-bage";
        // init flow rule
        List<FlowRule> rules = new ArrayList<>();
        FlowRule flowRule = new FlowRule();
        flowRule.setResource(resourceName);
        flowRule.setCount(1);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS); // 默认就是 这个
        rules.add(flowRule); // https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8#%E8%A7%84%E5%88%99%E7%9A%84%E7%A7%8D%E7%B1%BB

        FlowRuleManager.loadRules(rules);

        // test
        ThreadPoolExecutor executor = new ThreadPoolExecutor(20,20,60,TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
        int count = 20;
        while (count-- > 0) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Entry entry = null;
                    try {
                        entry = SphU.entry(resourceName);
                        System.out.println("entry ok");

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("entry end");
                    } catch (BlockException e) {
                        System.out.println("entry block : " + e.getMessage());
                    } finally {
                        if (entry != null) {
                            entry.exit();
                        }
                    }

                }
            });
        }


    }

}
