package com.bage.study.cryption.github.lugeek;

import java.security.MessageDigest;

/**
 * 来自：https://github.com/lugeek/Encryption/
 * @author bage
 *
 */
public class MD5 {
    public static byte[] encryptMD5(byte[] data) throws Exception {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();

    }
}
