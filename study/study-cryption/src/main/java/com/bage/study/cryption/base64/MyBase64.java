package com.bage.study.cryption.base64;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * 原理参考：https://www.cnblogs.com/diligenceday/p/6002382.html
 * 实现参考链接：https://baike.baidu.com/item/base64/8545775?fr=aladdin
 * @author bage
 *
 */
public class MyBase64 {

	public static void main(String[] args) {

		// import java.util.Base64;
		String data = "abc";
		System.out.println(":" + data);

		try {
			// 加密为字符串使用
			String encodedString = Base64.getEncoder().encodeToString(data.getBytes("UTF-8"));
			System.out.println(":" + encodedString);

			// 加密为字节数组使用
			// byte[] encodedBytes =
			// Base64.getEncoder().encode(data.getBytes("UTF-8"));

			// 解密使用
			String decodedString = new String(Base64.getDecoder().decode(encodedString.getBytes()), "UTF-8");
			// 对于URL安全或MIME的Base64，只需将上述getEncoder()getDecoder()更换为getUrlEncoder()getUrlDecoder()
			// 或getMimeEncoder()和getMimeDecoder()即可。
		
			System.out.println(":" + decodedString);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		source();
	}
	
	/**
	 * 把abc这三个字符转换为Base64的过程
	 */
	private static void source(){
//		字符串      a       b        c
//		ASCII      97      98       99
//		8bit   01100001 01100010 01100011
//		6bit   011000   010110   001001   100011
//		十进制      24      22        9        35
//		对应编码    Y        W        J        j
	}

}
