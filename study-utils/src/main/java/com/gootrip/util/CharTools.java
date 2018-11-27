package com.gootrip.util;
import java.io.UnsupportedEncodingException;

/**
* <p>Title:�ַ����빤���� </p>
* <p>Description:  </p>
* <p>Copyright:  Copyright (c) 2007</p>
* <p>Company:  </p>
* @author: 
* @version 1.0
*/
public class CharTools {

  /**
   * ת������ ISO-8859-1��GB2312
   * @param text
   * @return
   */
  public static final String ISO2GB(String text) {
    String result = "";
    try {
      result = new String(text.getBytes("ISO-8859-1"), "GB2312");
    }
    catch (UnsupportedEncodingException ex) {
      result = ex.toString();
    }
    return result;
  }

  /**
   * ת������ GB2312��ISO-8859-1
   * @param text
   * @return
   */
  public static final String GB2ISO(String text) {
    String result = "";
    try {
      result = new String(text.getBytes("GB2312"), "ISO-8859-1");
    }
    catch (UnsupportedEncodingException ex) {
      ex.printStackTrace();
    }
    return result;
  }
  /**
   * Utf8URL����
   * @param s
   * @return
   */
  public static final String Utf8URLencode(String text) {
    StringBuffer result = new StringBuffer();

    for (int i = 0; i < text.length(); i++) {

      char c = text.charAt(i);
      if (c >= 0 && c <= 255) {
        result.append(c);
      }else {

        byte[] b = new byte[0];
        try {
          b = Character.toString(c).getBytes("UTF-8");
        }catch (Exception ex) {
        }

        for (int j = 0; j < b.length; j++) {
          int k = b[j];
          if (k < 0) k += 256;
          result.append("%" + Integer.toHexString(k).toUpperCase());
        }

      }
    }

    return result.toString();
  }

  /**
   * Utf8URL����
   * @param text
   * @return
   */
  public static final String Utf8URLdecode(String text) {
    String result = "";
    int p = 0;

    if (text!=null && text.length()>0){
      text = text.toLowerCase();
      p = text.indexOf("%e");
      if (p == -1) return text;

      while (p != -1) {
        result += text.substring(0, p);
        text = text.substring(p, text.length());
        if (text == "" || text.length() < 9) return result;

        result += CodeToWord(text.substring(0, 9));
        text = text.substring(9, text.length());
        p = text.indexOf("%e");
      }

    }

    return result + text;
  }

  /**
   * utf8URL����ת�ַ�
   * @param text
   * @return
   */
  private static final String CodeToWord(String text) {
    String result;

    if (Utf8codeCheck(text)) {
      byte[] code = new byte[3];
      code[0] = (byte) (Integer.parseInt(text.substring(1, 3), 16) - 256);
      code[1] = (byte) (Integer.parseInt(text.substring(4, 6), 16) - 256);
      code[2] = (byte) (Integer.parseInt(text.substring(7, 9), 16) - 256);
      try {
        result = new String(code, "UTF-8");
      }catch (UnsupportedEncodingException ex) {
        result = null;
      }
    }
    else {
      result = text;
    }

    return result;
  }

  /**
   * �����Ƿ���Ч
   * @param text
   * @return
   */
  private static final boolean Utf8codeCheck(String text){
    String sign = "";
    if (text.startsWith("%e"))
      for (int i = 0, p = 0; p != -1; i++) {
        p = text.indexOf("%", p);
        if (p != -1)
          p++;
        sign += p;
      }
    return sign.equals("147-1");
  }

  /**
   * �ж��Ƿ�Utf8Url����
   * @param text
   * @return
   */
  public static final boolean isUtf8Url(String text) {
    text = text.toLowerCase();
    int p = text.indexOf("%");
    if (p != -1 && text.length() - p > 9) {
      text = text.substring(p, p + 9);
    }
    return Utf8codeCheck(text);
  }

  /**
   * ����
   * @param args
   */
  public static void main(String[] args) {

    //CharTools charTools = new CharTools();

    String url;

    url = "http://www.google.com/search?hl=zh-CN&newwindow=1&q=%E4%B8%AD%E5%9B%BD%E5%A4%A7%E7%99%BE%E7%A7%91%E5%9C%A8%E7%BA%BF%E5%85%A8%E6%96%87%E6%A3%80%E7%B4%A2&btnG=%E6%90%9C%E7%B4%A2&lr=";
    if(CharTools.isUtf8Url(url)){
      System.out.println(CharTools.Utf8URLdecode(url));
    }else{
      //System.out.println(URLDecoder.decode(url));
    }

    url = "http://www.baidu.com/baidu?word=%D6%D0%B9%FA%B4%F3%B0%D9%BF%C6%D4%DA%CF%DF%C8%AB%CE%C4%BC%EC%CB%F7&tn=myie2dg";
    if(CharTools.isUtf8Url(url)){
      System.out.println(CharTools.Utf8URLdecode(url));
    }else{
      //System.out.println(URLDecoder.decode(url));
    }

  }

}
