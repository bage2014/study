package com.bage.study.springboot.aop.annotation.flow.copy;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class FlowCopyTraceLogicTest {

    @Autowired
    private FlowCopyTraceLogic logic;

    @Test
    public void check() {
        //  A-B B-A
        System.out.println(check(Application.class, FlowCopy.class));
        System.out.println(check(Application.class, FlowCopyFinishListener.class));
        System.out.println(check(FlowCopyFinishListener.class, FlowCopy.class));
        System.out.println(check(FlowCopy.class, Application.class));

        // a-b-c-d 然后添加 d-a
        System.out.println(check(FlowCopyConfigService.class, FlowCopyFinishListener.class));
        System.out.println(check(FlowCopyFinishListener.class, FlowCopyFinishParam.class));
        System.out.println(check(FlowCopyFinishParam.class, FlowCopyLogic.class));
        System.out.println(check(FlowCopyLogic.class, FlowCopyConfigService.class));

    }

    private boolean check(Class fromCls, Class toCls) {
        return logic.check(fromCls.getName() + ".a()", toCls.getName() + ".a()");
    }
}
