package com.bage.study.springboot.aop.annotation.flow.copy;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class HelloFlowCopyConfigService implements FlowCopyConfigService{
    @Override
    public List<FlowCopyConfig> getFlowCopyConfigList() {
        FlowCopyConfig config = new FlowCopyConfig();
        config.setPercent(80);
        config.setTotal(100);
        config.setToClass(YouHelloService.class);
        config.setSync(false);
        config.setListener(Collections.singletonList(new FlowCopyFinishListener() {
            @Override
            public void onFinish(FlowCopyFinishParam finishParam) {
                System.out.println("onFinish++++");
            }

            @Override
            public void onException(FlowCopyFinishParam finishParam, Exception proceedException, Exception copyException) {

            }
        }));
        List<FlowCopyConfig> list = new ArrayList<>();
        list.add(config);
        return list;
    }
}
