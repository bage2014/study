//package com.bage.study.xfyun.lfasr;
//
//import com.alibaba.fastjson.JSON;
//import com.iflytek.msp.cpdb.lfasr.client.LfasrClientImp;
//import com.iflytek.msp.cpdb.lfasr.exception.LfasrException;
//import com.iflytek.msp.cpdb.lfasr.model.LfasrType;
//import com.iflytek.msp.cpdb.lfasr.model.Message;
//import com.iflytek.msp.cpdb.lfasr.model.ProgressStatus;
//
//import java.io.File;
//import java.util.HashMap;
//
///**
// * 非实时转写SDK调用demo
// * 此demo只是一个简单的调用示例, 不适合用到实际生产环境中
// *
// * @author white
// *
// */
//public class LfasrSDKDemo {
//
//    // 原始音频存放地址
//    private static final String local_file = "E:\\workspace\\IdeaProjects\\study\\study\\study-xfyun-lfasr\\target\\classes\\audio\\lfasr.wav";
//
//    /*
//     * 转写类型选择：标准版和电话版(旧版本, 不建议使用)分别为：
//     * LfasrType.LFASR_STANDARD_RECORDED_AUDIO 和 LfasrType.LFASR_TELEPHONY_RECORDED_AUDIO
//     * */
//    private static final LfasrType type = LfasrType.LFASR_STANDARD_RECORDED_AUDIO;
//
//    // 等待时长（毫秒）
//    private static int sleepMillis = 500;
//
//    public static void main(String[] args) {
//        // 初始化LFASRClient实例
//        LfasrClientImp lc = null;
//        try {
//            lc = LfasrClientImp.initLfasrClient();
//        } catch (LfasrException e) {
//            // 初始化异常，解析异常描述信息
//            Message initMsg = JSON.parseObject(e.getMessage(), Message.class);
//            System.out.println("ecode=" + initMsg.getErr_no());
//            System.out.println("failed=" + initMsg.getFailed());
//        }
//
//        // 获取上传任务ID
//        String task_id = "";
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("has_participle", "true");
//        //合并后标准版开启电话版功能
//        //params.put("has_seperate", "true");
//        try {
//            // 上传音频文件
//            Message uploadMsg = lc.lfasrUpload(local_file, type, params);
//
//            // 判断返回值
//            int ok = uploadMsg.getOk();
//            if (ok == 0) {
//                // 创建任务成功
//                task_id = uploadMsg.getData();
//                System.out.println("task_id=" + task_id);
//            } else {
//                // 创建任务失败-服务端异常
//                System.out.println("ecode=" + uploadMsg.getErr_no());
//                System.out.println("failed=" + uploadMsg.getFailed());
//            }
//        } catch (LfasrException e) {
//            // 上传异常，解析异常描述信息
//            Message uploadMsg = JSON.parseObject(e.getMessage(), Message.class);
//            System.out.println("ecode=" + uploadMsg.getErr_no());
//            System.out.println("failed=" + uploadMsg.getFailed());
//        }
//
//        // 循环等待音频处理结果
//        while (true) {
//            try {
//                // 等待sleepMillis在获取任务进度
//                Thread.sleep(sleepMillis);
//                System.out.println("waiting ...");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            try {
//                // 获取处理进度
//                Message progressMsg = lc.lfasrGetProgress(task_id);
//
//                // 如果返回状态不等于0，则任务失败
//                if (progressMsg.getOk() != 0) {
//                    System.out.println("task was fail. task_id:" + task_id);
//                    System.out.println("ecode=" + progressMsg.getErr_no());
//                    System.out.println("failed=" + progressMsg.getFailed());
//
//                    return;
//                } else {
//                    ProgressStatus progressStatus = JSON.parseObject(progressMsg.getData(), ProgressStatus.class);
//                    if (progressStatus.getStatus() == 9) {
//                        // 处理完成
//                        System.out.println("task was completed. task_id:" + task_id);
//                        break;
//                    } else {
//                        // 未处理完成
//                        System.out.println("task is incomplete. task_id:" + task_id + ", status:" + progressStatus.getDesc());
//                        continue;
//                    }
//                }
//            } catch (LfasrException e) {
//                // 获取进度异常处理，根据返回信息排查问题后，再次进行获取
//                Message progressMsg = JSON.parseObject(e.getMessage(), Message.class);
//                System.out.println("ecode=" + progressMsg.getErr_no());
//                System.out.println("failed=" + progressMsg.getFailed());
//            }
//        }
//
//        // 获取任务结果
//        try {
//            Message resultMsg = lc.lfasrGetResult(task_id);
//            // 如果返回状态等于0，则获取任务结果成功
//            if (resultMsg.getOk() == 0) {
//                // 打印转写结果
//                System.out.println(JsonParser.parseMessage(resultMsg.getData()));
//            } else {
//                // 获取任务结果失败
//                System.out.println("ecode=" + resultMsg.getErr_no());
//                System.out.println("failed=" + resultMsg.getFailed());
//            }
//        } catch (LfasrException e) {
//            // 获取结果异常处理，解析异常描述信息
//            Message resultMsg = JSON.parseObject(e.getMessage(), Message.class);
//            System.out.println("ecode=" + resultMsg.getErr_no());
//            System.out.println("failed=" + resultMsg.getFailed());
//        }
//    }
//}