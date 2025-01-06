package com.bage.study.best.practice.biz.template2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddOrderStep2 extends ComponentAbstract {
    @Override
    public void doHandler(Context contxt) {
        log.info("AddOrderSte2-顺序2-上下文内容为：{}", contxt);
        if(contxt instanceof AddOrderContext){
            ((AddOrderContext) contxt).setAddOrderContext("Test2");
        }
    }
}
