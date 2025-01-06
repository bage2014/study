package com.bage.study.best.practice.biz.template2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddOrderStep1 extends ComponentAbstract {
    @Override
    public void doHandler(Context contxt) {
        log.info("AddOrderStep1-顺序1-上下文内容为：{}", contxt);
        if(contxt instanceof AddOrderContext){
            ((AddOrderContext) contxt).setAddOrderContext("Test1");
        }
    }
}
