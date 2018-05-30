package com.bage.study.cryption.github.lugeek;

public class Test {

	public static void main(String[] args) {

		aes();

	}

	private static void aes() {
		// 初始化密钥
		String aesKey = "bage";
		try {
			aesKey = AES.initKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String inputStr = "很费劲啊回复国家恢复";
		byte[] inputData = inputStr.getBytes();
		byte[] outputData = new byte[0];
		String outputStr = "";
		try {
			outputData = AES.encrypt(inputData, aesKey);
			System.out.println(outputData);
			outputStr = AES.encryptBASE64(outputData);
			
			System.out.println(outputData);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			outputData = AES.decrypt(AES.decryptBASE64(outputStr), aesKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String outputStr2 = new String(outputData);
		System.out.println(outputStr2);
	}

}
