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
        boolean add = logic.check(Application.class, FlowCopy.class);
        boolean add1 = logic.check(Application.class, FlowCopyFinishListener.class);
        boolean add2 = logic.check(FlowCopyFinishListener.class, FlowCopy.class);
        boolean add3 = logic.check(FlowCopy.class, Application.class);

        // a-b-c-d 然后添加 d-a
        boolean add4 = logic.check(FlowCopyConfigService.class, FlowCopyFinishListener.class);
        boolean add5 = logic.check(FlowCopyFinishListener.class, FlowCopyFinishParam.class);
        boolean add6 = logic.check(FlowCopyFinishParam.class, FlowCopyLogic.class);
        boolean add7 = logic.check(FlowCopyLogic.class, FlowCopyConfigService.class);

    }
}
