package com.bage.study.cryption.github.lugeek;

import java.util.Base64;

/**
 * 来自：https://github.com/lugeek/Encryption/
 * @author bage
 *
 */
public class BASE64 {
	
	
    /**
     * BASE64解密
     *
     * @param key the String to be decrypted
     * @return byte[] the data which is decrypted
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.getDecoder().decode(key);
    }
    /**
     * BASE64加密
     *
     * @param key the String to be encrypted
     * @return String the data which is encrypted
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return Base64.getEncoder().encodeToString(key);
    }
}
