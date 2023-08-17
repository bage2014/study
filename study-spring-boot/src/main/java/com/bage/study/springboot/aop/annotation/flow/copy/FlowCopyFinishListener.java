package com.bage.study.springboot.aop.annotation.flow.copy;

public interface FlowCopyFinishListener {

    void onFinish(FlowCopyFinishParam finishParam);

    void onException(FlowCopyFinishParam finishParam, Exception proceedException, Exception copyException);

}
