package com.bage.study.cryption.github.lugeek;

import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 来自：https://github.com/lugeek/Encryption/
 * @author bage
 *
 */
public class HMAC {
    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.getDecoder().decode(key);
    }
    public static String encryptBASE64(byte[] key) throws Exception {
        return Base64.getEncoder().encodeToString(key);
    }

    /**
     * 初始化HMAC密钥
     *
     * @return
     * @throws Exception
     */
    public static String initMacKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");

        SecretKey secretKey = keyGenerator.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }

    /**
     * HMAC加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptHMAC(byte[] data, String key, String method) throws Exception {

        //SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), "HmacMD5");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), method);//直接用给定的字符串进行
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);

        return mac.doFinal(data);

    }
}
