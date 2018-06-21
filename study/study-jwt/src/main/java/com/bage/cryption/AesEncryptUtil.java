package com.bage.cryption;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryptUtil
{
  private static String KEY = "fkubiRCCWyl/p4XQ";

  private static String IV = "fkubiRCCWyl/p4XQ";

  public static String encrypt(String data, String key, String iv)
  {
    try
    {
      if (data == null) {
        return null;
      }
      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
      int blockSize = cipher.getBlockSize();

      byte[] dataBytes = data.getBytes();
      int plaintextLength = dataBytes.length;
      if (plaintextLength % blockSize != 0) {
        plaintextLength += blockSize - plaintextLength % blockSize;
      }

      byte[] plaintext = new byte[plaintextLength];
      System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

      SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
      IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

      cipher.init(1, keyspec, ivspec);
      byte[] encrypted = cipher.doFinal(plaintext);

      return Base64.getEncoder().encodeToString(encrypted);
    }
    catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  public static String desEncrypt(String data, String key, String iv)
  {
    try
    {
      if (data == null) {
        return null;
      }
      data = data.replace(" ", "+");
      byte[] encrypted = Base64.getDecoder().decode(data);

      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
      SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
      IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

      cipher.init(2, keyspec, ivspec);

      byte[] original = cipher.doFinal(encrypted);
      List<Byte> originals = new ArrayList<Byte>();
      for (int i = 0; i < original.length; i++) {
        if (original[i] != 0) {
          originals.add(Byte.valueOf(original[i]));
        }
      }
      original = new byte[originals.size()];
      for (int i = 0; i < originals.size(); i++) {
        original[i] = ((Byte)originals.get(i)).byteValue();
      }
      return new String(original, "UTF-8");
    }
    catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  public static byte[] parseHexStr2Byte(String hexStr) {
    if (hexStr.length() < 1)
      return null;
    byte[] result = new byte[hexStr.length() / 2];
    for (int i = 0; i < hexStr.length() / 2; i++) {
      int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
      int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
      result[i] = ((byte)(high * 16 + low));
    }
    return result;
  }

  public static String encrypt(String data)
  {
    return encrypt(data, KEY, IV);
  }

  public static String desEncrypt(String data)
  {
    return desEncrypt(data, KEY, IV);
  }
  
  
}
