package com.gootrip.util;

import java.util.*;
import java.util.regex.*;

/**
 * ���Ǹ�������ʽӦ���࣬����ƥ����滻�ִ��õ�
 * @author 
 * @version 
 */

public class RegExUtil {

  /**
   * Ҫ���Сд��ƥ��������ʽ
   * @param pattern ������ʽģʽ
   * @param str Ҫƥ����ִ�
   * @return booleanֵ
   * @since  1.0
   */
  public static final boolean ereg(String pattern, String str)  throws PatternSyntaxException
  {
    try
    {
      Pattern p = Pattern.compile(pattern);
      Matcher m = p.matcher(str);
      return m.find();
    }
    catch (PatternSyntaxException e)
    {
      throw e;
    }
  }

  /**
   * ƥ�����滻�ִ�
   * @param pattern ������ʽģʽ
   * @param newstr Ҫ�滻ƥ�䵽�����ִ�
   * @param str ԭʼ�ִ�
   * @return ƥ�����ַ���
   * @since  1.0
   */

  public static final String ereg_replace(String pattern, String newstr, String str)  throws PatternSyntaxException
  {
    try
    {
      Pattern p = Pattern.compile(pattern);
      Matcher m = p.matcher(str);
      return m.replaceAll(newstr);
    }
    catch (PatternSyntaxException e)
    {
      throw e;
    }
  }

  /**
   * ��Ҫ����ģ����ģ���Ƿ������� �Ѳ��ҵ���Ԫ�ؼӵ�vector��
   * @param patternΪ������ʽģʽ
   * @param str ԭʼ�ִ�
   * @return vector
   * @since  1.0
   */
  public static final Vector splitTags2Vector(String pattern, String str) throws PatternSyntaxException
  {
    Vector vector = new Vector();
    try {
      Pattern p = Pattern.compile(pattern);
      Matcher m = p.matcher(str);
      while (m.find())
      {
        vector.add(ereg_replace("(\\[\\#)|(\\#\\])", "", m.group()));
      }
      return vector;
    }
    catch (PatternSyntaxException e) {
      throw e;
    }
  }
  /**
   * ģ���Ƿ�������
   * ������Ҫ�ǰѲ��ҵ���Ԫ�ؼӵ�vector��
   * @param patternΪ������ʽģʽ
   * @param str ԭʼ�ִ�
   * @since  1.0
   */
  public static final String[] splitTags(String pattern, String str)
  {
    try {
      Pattern p = Pattern.compile(pattern);
      Matcher m = p.matcher(str);
      String[] array = new String[m.groupCount()];
      int i = 0;
      while (m.find())
      {
        array[i] = ereg_replace("(\\[\\#)|(\\#\\])", "", m.group());
        i++;
      }
      return array;
    }
    catch (PatternSyntaxException e) {
      throw e;
    }
  }


  /**
   * ƥ�����з���ģʽҪ����ִ����ӵ�ʸ��vector������
   * @param patternΪ������ʽģʽ
   * @param str ԭʼ�ִ�
   * @return vector
   * @since  1.0
   */
  public static final Vector regMatchAll2Vector(String pattern, String str) throws PatternSyntaxException
  {
    Vector vector = new Vector();
    try
    {
      Pattern p = Pattern.compile(pattern);
      Matcher m = p.matcher(str);
      while (m.find())
      {
        vector.add(m.group());
      }
      return vector;
    }
    catch (PatternSyntaxException e)
    {
      throw e;
    }
  }

  /**
   * ƥ�����з���ģʽҪ����ִ����ӵ��ַ���������
   * @param patternΪ������ʽģʽ
   * @param str ԭʼ�ִ�
   * @return array
   * @since  1.0
   */
  public static final String[] regMatchAll2Array(String pattern, String str) throws PatternSyntaxException
  {
    try
    {
      Pattern p = Pattern.compile(pattern);
      Matcher m = p.matcher(str);
      String[] array = new String[m.groupCount()];
      int i = 0;
      while (m.find())
      {
        array[i] = m.group();
        i++;
      }
      return array;
    }
    catch (PatternSyntaxException e)
    {
      throw e;
    }
  }
  /**
   * ת��������ʽ�ַ�(֮������Ҫ��\��$�ַ���escapeDollarBackslash�����ķ�ʽ����Ϊ��repalceAll�ǲ��еģ��򵥵�����"$".repalceAll("\\$","\\\\$")��ᷢ��������ûᵼ������Խ�����)
   * @param patternΪ������ʽģʽ
   * @param str ԭʼ�ִ�
   * @return array
   * @since  1.0
   */
  public static String escapeDollarBackslash(String original) {
      StringBuffer buffer=new StringBuffer(original.length());
      for (int i=0;i<original.length();i++) {
        char c=original.charAt(i);
        if (c=='\\'||c=='$') {
          buffer.append("\\").append(c);
        } else{
          buffer.append(c);
        }
      }
      return buffer.toString();
    }

    /**
     * ��ȡָ���ִ��ĺ���
     * ������Ҫ�ǰѲ��ҵ���Ԫ��
     * @param patternΪ������ʽģʽ
     * @param str ԭʼ�ִ�
     * @since  1.0
     */
    public static final String fetchStr(String pattern, String str) {
        String returnValue = null;
        try {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(str);
            while (m.find()) {
                returnValue = m.group();
            }
            return returnValue;
        } catch (PatternSyntaxException e) {
            return returnValue;
        }
  }
}
