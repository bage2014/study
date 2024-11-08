package com.bage.study.java.pattern.chain;

/**
 * https://juejin.cn/post/7402435366647758857
 *
 * https://juejin.cn/post/6956837578993565733?searchId=20241107225601ACF3D170EDFED2D862D1
 *
 * https://juejin.cn/post/7326920430589722639?searchId=20241107225601ACF3D170EDFED2D862D1
 *
 * https://juejin.cn/post/7280791145684189240?searchId=20241107225601ACF3D170EDFED2D862D1
 *
 * https://juejin.cn/post/7268260939066064952?searchId=20241107225601ACF3D170EDFED2D862D1
 *
 * https://juejin.cn/post/7122384707697770510?searchId=20241107225601ACF3D170EDFED2D862D1
 *
 *
 */
public class HelloChain {
    public static void main(String[] args) {
        DirectLeaveHandler directLeaveHandler = new DirectLeaveHandler("直属主管");
        ManagerLeaveHandler managerLeaveHandler = new ManagerLeaveHandler("部门经理");
        GeneralManagerLeavHandler generalManagerLeavHandler = new GeneralManagerLeavHandler("总经理");

        directLeaveHandler.setNextHandler(managerLeaveHandler);
        managerLeaveHandler.setNextHandler(generalManagerLeavHandler);

        System.out.println("========张三请假2天==========");
        LeaveRequest lxl = new LeaveRequest("张三", 2);
        directLeaveHandler.process(lxl);

        System.out.println("========李四请假6天==========");
        LeaveRequest wangxiao = new LeaveRequest("李四", 6);
        directLeaveHandler.process(wangxiao);


        System.out.println("========王五请假30天==========");
        LeaveRequest yongMing = new LeaveRequest("王五", 30);
        directLeaveHandler.process(yongMing);

    }
}
