package com.bage;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;

/**
 * Hello world!
 *
 * 新街口：
 * https://leancloud.cn/docs/rest_sms_api.html#hash875634106
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        // 往 18612345678 这个手机号码发送短信，使用预设的模板（「Register_Notice」参数）
        try {

            // AVOSCloud.initialize(applicationId,clientKey,masterKey);
            AVOSCloud.requestSMSCode("17512500150","deviceReport", null);


        } catch (AVException e) {
            e.printStackTrace();
        }

    }
}
