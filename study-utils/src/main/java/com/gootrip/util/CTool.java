package com.gootrip.util;

/**
 * �������ռ�Java�����WEB�������õ���һЩ���ߡ�
 * Ϊ�������ɴ����ʵ�������췽��������Ϊprivate���͵ġ�
 * @author 
 */
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Date;


public class CTool {
    /**
     * ˽�й��췽������ֹ���ʵ��������Ϊ�����಻��Ҫʵ������
     */
    private CTool() {
    }

    /**
      <pre>
     * ����
     * String strVal="This is a dog";
     * String strResult=CTools.replace(strVal,"dog","cat");
     * �����
     * strResult equals "This is cat"
     *
     * @param strSrc Ҫ�����滻�������ַ���
     * @param strOld Ҫ���ҵ��ַ���
     * @param strNew Ҫ�滻���ַ���
     * @return �滻����ַ���
      <pre>
     */
    public static final String replace(String strSrc, String strOld,
                                       String strNew) {
        if (strSrc == null || strOld == null || strNew == null)
            return "";

        int i = 0;
        
        if (strOld.equals(strNew)) //�����¾��ַ�һ��������ѭ��
        	return strSrc;
        
        if ((i = strSrc.indexOf(strOld, i)) >= 0) {
            char[] arr_cSrc = strSrc.toCharArray();
            char[] arr_cNew = strNew.toCharArray();

            int intOldLen = strOld.length();
            StringBuffer buf = new StringBuffer(arr_cSrc.length);
            buf.append(arr_cSrc, 0, i).append(arr_cNew);

            i += intOldLen;
            int j = i;

            while ((i = strSrc.indexOf(strOld, i)) > 0) {
                buf.append(arr_cSrc, j, i - j).append(arr_cNew);
                i += intOldLen;
                j = i;
            }

            buf.append(arr_cSrc, j, arr_cSrc.length - j);

            return buf.toString();
        }

        return strSrc;
    }

    /**
     * ���ڽ��ַ����е������ַ�ת����Webҳ�п��԰�ȫ��ʾ���ַ���
     * �ɶԱ����ݾݽ��д����һЩҳ�������ַ����д�����'<','>','"',''','&'
     * @param strSrc Ҫ�����滻�������ַ���
     * @return �滻�����ַ�����ַ���
     * @since  1.0
     */

    public static String htmlEncode(String strSrc) {
        if (strSrc == null)
            return "";

        char[] arr_cSrc = strSrc.toCharArray();
        StringBuffer buf = new StringBuffer(arr_cSrc.length);
        char ch;

        for (int i = 0; i < arr_cSrc.length; i++) {
            ch = arr_cSrc[i];

            if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            else if (ch == '"')
                buf.append("&quot;");
            else if (ch == '\'')
                buf.append("&#039;");
            else if (ch == '&')
                buf.append("&amp;");
            else
                buf.append(ch);
        }

        return buf.toString();
    }

    /**
     * ���ڽ��ַ����е������ַ�ת����Webҳ�п��԰�ȫ��ʾ���ַ���
     * �ɶԱ����ݾݽ��д����һЩҳ�������ַ����д�����'<','>','"',''','&'
     * @param strSrc Ҫ�����滻�������ַ���
     * @param quotes Ϊ0ʱ�����ź�˫���Ŷ��滻��Ϊ1ʱ���滻�����ţ�Ϊ2ʱ���滻˫���ţ�Ϊ3ʱ�����ź�˫���Ŷ����滻
     * @return �滻�����ַ�����ַ���
     * @since  1.0
     */
    public static String htmlEncode(String strSrc, int quotes) {

        if (strSrc == null)
            return "";
        if (quotes == 0) {
            return htmlEncode(strSrc);
        }

        char[] arr_cSrc = strSrc.toCharArray();
        StringBuffer buf = new StringBuffer(arr_cSrc.length);
        char ch;

        for (int i = 0; i < arr_cSrc.length; i++) {
            ch = arr_cSrc[i];
            if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            else if (ch == '"' && quotes == 1)
                buf.append("&quot;");
            else if (ch == '\'' && quotes == 2)
                buf.append("&#039;");
            else if (ch == '&')
                buf.append("&amp;");
            else
                buf.append(ch);
        }

        return buf.toString();
    }

    /**
     * ��htmlEncode�����෴
     * @param strSrc Ҫ����ת�����ַ���
     * @return ת������ַ���
     * @since  1.0
     */
    public static String htmlDecode(String strSrc) {
        if (strSrc == null)
            return "";
        strSrc = strSrc.replaceAll("&lt;", "<");
        strSrc = strSrc.replaceAll("&gt;", ">");
        strSrc = strSrc.replaceAll("&quot;", "\"");
        strSrc = strSrc.replaceAll("&#039;", "'");
        strSrc = strSrc.replaceAll("&amp;", "&");
        return strSrc;
    }

    /**
     * �ڽ����ݴ������ݿ�ǰת��
     * @param strVal Ҫת�����ַ���
     * @return �ӡ�ISO8859_1������GBK���õ����ַ���
     * @since  1.0
     */
    public static String toChinese(String strVal) {
        try {
            if (strVal == null) {
                return "";
            } else {
                strVal = strVal.trim();
                strVal = new String(strVal.getBytes("ISO8859_1"), "GBK");
                return strVal;
            }
        } catch (Exception exp) {
            return "";
        }
    }
    /**
     * ����ת�� ��UTF-8��GBK
     * @param strVal
     * @return
     */
    public static String toGBK(String strVal) {
        try {
            if (strVal == null) {
                return "";
            } else {
                strVal = strVal.trim();
                strVal = new String(strVal.getBytes("UTF-8"), "GBK");
                return strVal;
            }
        } catch (Exception exp) {
            return "";
        }
    }

    /**
     * �����ݴ����ݿ���ȡ����ת��   *
     * @param strVal Ҫת�����ַ���
     * @return �ӡ�GBK������ISO8859_1���õ����ַ���
     * @since  1.0
     */
    public static String toISO(String strVal) {
        try {
            if (strVal == null) {
                return "";
            } else {
                strVal = new String(strVal.getBytes("GBK"), "ISO8859_1");
                return strVal;
            }
        } catch (Exception exp) {
            return "";
        }
    }
    public static String gbk2UTF8(String strVal) {
        try {
            if (strVal == null) {
                return "";
            } else {
                strVal = new String(strVal.getBytes("GBK"), "UTF-8");
                return strVal;
            }
        } catch (Exception exp) {
            return "";
        }
    }
    public static String ISO2UTF8(String strVal) {
       try {
           if (strVal == null) {
               return "";
           } else {
               strVal = new String(strVal.getBytes("ISO-8859-1"), "UTF-8");
               return strVal;
           }
       } catch (Exception exp) {
           return "";
       }
   }
   public static String UTF82ISO(String strVal) {
       try {
           if (strVal == null) {
               return "";
           } else {
               strVal = new String(strVal.getBytes("UTF-8"), "ISO-8859-1");
               return strVal;
           }
       } catch (Exception exp) {
           return "";
       }
   }



    /**
     *��ʾ���ı��鴦��(���ַ���ת��ISO)
     *@deprecated
     *@param str Ҫ����ת�����ַ���
     *@return ת����html����������ʾ���ַ���
     */
    public static String toISOHtml(String str) {
        return toISO(htmlDecode(null2Blank((str))));
    }

    /**
     *ʵ�ʴ��� return toChineseNoReplace(null2Blank(str));
     *��ҪӦ������ţ����Ϣ����
     *@param str Ҫ���д�����ַ���
     *@return ת������ַ���
     *@see fs_com.utils.CTools#toChinese
     *@see fs_com.utils.CTools#null2Blank
     */
    public static String toChineseAndHtmlEncode(String str, int quotes) {
        return htmlEncode(toChinese(str), quotes);
    }

    /**
     *��nullֵ��""ֵת����&nbsp;
     *��ҪӦ����ҳ��������ʾ
     *@param str Ҫ���д�����ַ���
     *@return ת������ַ���
     */
    public static String str4Table(String str) {
        if (str == null)
            return "&nbsp;";
        else if (str.equals(""))
            return "&nbsp;";
        else
            return str;
    }

    /**
     * String�ͱ���ת����int�ͱ���
     * @param str Ҫ����ת�����ַ���
     * @return intVal ���str������ת����int�����ݣ�����-1��ʾ�쳣,���򷵻�ת�����ֵ
     * @since  1.0
     */
    public static int str2Int(String str) {
        int intVal;

        try {
            intVal = Integer.parseInt(str);
        } catch (Exception e) {
            intVal = 0;
        }

        return intVal;
    }

    public static double str2Double(String str) {
        double dVal = 0;

        try {
            dVal = Double.parseDouble(str);
        } catch (Exception e) {
            dVal = 0;
        }

        return dVal;
    }


    public static long str2Long(String str) {
        long longVal = 0;

        try {
            longVal = Long.parseLong(str);
        } catch (Exception e) {
            longVal = 0;
        }

        return longVal;
    }

    public static float stringToFloat(String floatstr) {
        Float floatee;
        floatee = Float.valueOf(floatstr);
        return floatee.floatValue();
    }

    //change the float type to the string type
    public static String floatToString(float value) {
        Float floatee = new Float(value);
        return floatee.toString();
    }

    /**
     *int�ͱ���ת����String�ͱ���
     *@param intVal Ҫ����ת��������
     *@return str ���intVal������ת����String�����ݣ����ؿ�ֵ��ʾ�쳣,���򷵻�ת�����ֵ
     */
    /**
     *int�ͱ���ת����String�ͱ���
     *@param intVal Ҫ����ת��������
     *@return str ���intVal������ת����String�����ݣ����ؿ�ֵ��ʾ�쳣,���򷵻�ת�����ֵ
     */
    public static String int2Str(int intVal) {
        String str;

        try {
            str = String.valueOf(intVal);
        } catch (Exception e) {
            str = "";
        }

        return str;
    }

    /**
     *long�ͱ���ת����String�ͱ���
     *@param longVal Ҫ����ת��������
     *@return str ���longVal������ת����String�����ݣ����ؿ�ֵ��ʾ�쳣,���򷵻�ת�����ֵ
     */

    public static String long2Str(long longVal) {
        String str;

        try {
            str = String.valueOf(longVal);
        } catch (Exception e) {
            str = "";
        }

        return str;
    }

    /**
     *null ����
     *@param str Ҫ����ת�����ַ���
     *@return ���strΪnullֵ�����ؿմ�"",���򷵻�str
     */
    public static String null2Blank(String str) {
        if (str == null)
            return "";
        else
            return str;
    }

    /**
     *null ����
     *@param d Ҫ����ת�������ڶ���
     *@return ���dΪnullֵ�����ؿմ�"",���򷵻�d.toString()
     */

    public static String null2Blank(Date d) {
        if (d == null)
            return "";
        else
            return d.toString();
    }

    /**
     *null ����
     *@param str Ҫ����ת�����ַ���
     *@return ���strΪnullֵ�����ؿմ�����0,���򷵻���Ӧ������
     */
    public static int null2Zero(String str) {
        int intTmp;
        intTmp = str2Int(str);
        if (intTmp == -1)
            return 0;
        else
            return intTmp;
    }
    /**
     * ��nullת��Ϊ�ַ���"0"
     * @param str
     * @return
     */
    public static String null2SZero(String str) {
        str = CTool.null2Blank(str);
        if (str.equals(""))
            return "0";
        else
            return str;
    }

    /**
     * sql��� ����
     * @param sql Ҫ���д����sql���
     * @param dbtype ���ݿ�����
     * @return ������sql���
     */
    public static String sql4DB(String sql, String dbtype) {
        if (!dbtype.equalsIgnoreCase("oracle")) {
            sql = CTool.toISO(sql);
        }
        return sql;
    }

    /**
     * ���ַ�������md5����
     * @param s Ҫ���ܵ��ַ���
     * @return md5���ܺ���ַ���
     */
    public final static String MD5(String s) {
        char hexDigits[] = {
                           '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                           'a', 'b', 'c', 'd',
                           'e', 'f'};
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * �ַ�����GBK����ת��ΪUnicode����
     * @param text
     * @return
     */
    public static String StringToUnicode(String text) {
        String result = "";
        int input;
        StringReader isr;
        try {
            isr = new StringReader(new String(text.getBytes(), "GBK"));
        } catch (UnsupportedEncodingException e) {
            return "-1";
        }
        try {
            while ((input = isr.read()) != -1) {
                result = result + "&#x" + Integer.toHexString(input) + ";";

            }
        } catch (IOException e) {
            return "-2";
        }
        isr.close();
        return result;

    }
    /**
     * 
     * @param inStr
     * @return
     */
    public static String gb2utf(String inStr) {
        char temChr;
        int ascInt;
        int i;
        String result = new String("");
        if (inStr == null) {
            inStr = "";
        }
        for (i = 0; i < inStr.length(); i++) {
            temChr = inStr.charAt(i);
            ascInt = temChr + 0;
            //System.out.println("1=="+ascInt);
            //System.out.println("1=="+Integer.toBinaryString(ascInt));
            if( Integer.toHexString(ascInt).length() > 2 ) {
                result = result + "&#x" + Integer.toHexString(ascInt) + ";";
            }
            else
            {
                result = result + temChr;
            }

        }
        return result;
    }
    /**
     * This method will encode the String to unicode.
     *
     * @param gbString
     * @return
     */

    //����:--------------------------------------------------------------------------------
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        System.out.println("unicodeBytes is: " + unicodeBytes);
        return unicodeBytes;
    }

    /**
     * This method will decode the String to a recognized String
     * in ui.
     * @param dataStr
     * @return
     */
    public static StringBuffer decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16����parse�����ַ�����
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer;
    }

}
