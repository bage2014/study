package com.bage.study.java.pattern.chain;

/**
 * 抽象的请假处理类
 */
public abstract class LeaveHandler {
    /**
     * 处理人姓名
     */
    private String handlerName;

    /**
     * 下一个处理人
     */
    private LeaveHandler nextHandler;

    public void setNextHandler(LeaveHandler leaveHandler) {
        this.nextHandler = leaveHandler;
    }

    public LeaveHandler(String handlerName) {
        this.handlerName = handlerName;
    }

    /**
     * 具体的处理操作
     * @param leaveRequest
     * @return
     */
    public abstract boolean process(LeaveRequest leaveRequest);

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public LeaveHandler getNextHandler() {
        return nextHandler;
    }
}
