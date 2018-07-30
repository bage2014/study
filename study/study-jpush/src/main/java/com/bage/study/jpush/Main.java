package com.bage.study.jpush;

import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;

public class Main {

	private static final String MASTER_SECRET = "037dd9eef453bf5fd2cd97d4";
	private static final String ALERT = "你好世界";
	
	private static String APP_KEY = "";

	private static Log LOG = LogFactory.getLog(Main.class);
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String key = scanner.nextLine();
		scanner.close();
		 
		APP_KEY = key;
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());

	    // For push, all you need do is to build PushPayload object.
	    PushPayload payload = buildPushObject_all_all_alert();

	    try {
	        PushResult result = jpushClient.sendPush(payload);
	        LOG.info("Got result - " + result);

	    } catch (APIConnectionException e) {
	        // Connection error, should retry later
	        LOG.error("Connection error, should retry later", e);

	    } catch (APIRequestException e) {
	        // Should review the error, and fix the request
	        LOG.error("Should review the error, and fix the request", e);
	        LOG.info("HTTP Status: " + e.getStatus());
	        LOG.info("Error Code: " + e.getErrorCode());
	        LOG.info("Error Message: " + e.getErrorMessage());
	    }
	    
	   
	}
	
	  public static PushPayload buildPushObject_all_all_alert() {
	        return PushPayload.alertAll(ALERT);
	    }
	  
}
