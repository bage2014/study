package com.bage.study.baidu.asr;

import java.io.IOException;

import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.util.Util;

public class Sample {
    //设置APPID/AK/SK
    public static final String APP_ID = "xxx";
    public static final String API_KEY = "xxx";
    public static final String SECRET_KEY = "xxx";
    public static  AipSpeech client = null;

    static {
    	 // 初始化一个AipSpeech
        client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
    }


    public static void main(String[] args) {
       

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        System.setProperty("aip.log4j.conf", "log4j.properties");

        // 调用接口
		//String fromPath = "C:\\Users\\luruihua\\Downloads\\2019-01-10T01_12_27.486Z.wav";
		//String fromPath = "C:\\Users\\luruihua\\Downloads\\Java_iat1021_lfasr4.0_tts_online1021_5c35c6d9\\sample\\MscInvisibleDemo\\test.pcm";
		// String fromPath = "C:\\Users\\luruihua\\Downloads\\Java_iat1021_lfasr4.0_tts_online1021_5c35c6d9\\sample\\MscInvisibleDemo\\lfasr.wav";
		 String fromPath = "C:\\Users\\luruihua\\Documents\\Tencent Files\\893542907\\FileRecv\\MobileFile\\Rec_004.wav";
		// String fromPath = "C:\\Users\\luruihua\\Downloads\\2019-01-10T01_36_18.117Z.wav";

        JSONObject res = client.asr(fromPath, "wav", 16000, null);
        System.out.println(res.toString());
       // System.out.println(res.toString(2));
        
        asr();
        
    }


    public static void asr()
    {
        String fromPath = "C:\\Users\\luruihua\\Documents\\Tencent Files\\893542907\\FileRecv\\MobileFile\\Rec_004.wav";

        // 对本地语音文件进行识别
        // String path = "D:\\code\\java-sdk\\speech_sdk\\src\\test\\resources\\16k_test.pcm";
        JSONObject asrRes = client.asr(fromPath, "wav", 16000, null);
        System.out.println(asrRes);

        // 对语音二进制数据进行识别
        byte[] data = {};
        try {
            data = Util.readFileByBytes(fromPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }     //readFileByBytes仅为获取二进制数据示例
        JSONObject asrRes2 = client.asr(data, "wav", 16000, null);
        System.out.println(asrRes2);

    }
}
