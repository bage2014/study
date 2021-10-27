//package com.bage.study.xfyun.lfasr;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//
//public class JsonParser {
//
//	public static String parseMessage(String data) {
//		 JSONArray jsonArray = JSON.parseArray(data);
//		 StringBuilder sb = new StringBuilder();
//		 JSONObject item = null;
//		 for (int i = 0; i < jsonArray.size(); i++) {
//			 item = jsonArray.getJSONObject(i);
//			 if(item != null) {
//				 sb.append(item.getString("onebest"));
//			 }
//		}
//		return sb.toString();
//	}
//
//}
