/**
 * 
 */
package com.gootrip.util;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * @author advance
 *
 */
public class DealString {
	
	public DealString() {
	}

	//�ж��ַ����Ƿ�Ϊ�գ���ɾ����β�ո�
	public static String convertNullCode(String tempSql){
		if (tempSql==null) tempSql="";
		return tempSql;
	}
	/**
	 * �ַ����滻����
	 * @param originString ԭ�ַ���
	 * @param oldString ���滻�ַ���
	 * @param newString �滻�ַ���
	 * @return �滻��������ַ���
	 */
	public static String replace(String originString,String oldString,String newString){
		String getstr = originString;
		while(getstr.indexOf(oldString)>-1){
			getstr = getstr.substring(0,getstr.indexOf(oldString)) + newString + getstr.substring(getstr.indexOf(oldString)+oldString.length(),getstr.length());
		}
		return getstr;
	}
	/**
	 * ����ת����GBKת��ΪISO-8859-1
	 * @param tempSql Ҫת�����ַ���
	 * @return
	 */
	public static String ISOCode(String tempSql){
	
		String returnString = convertNullCode(tempSql);
		
		try{
			byte[] ascii = returnString.getBytes("GBK");
			returnString = new String(ascii,"ISO-8859-1");
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnString;
	}

	/**
	 * ����ת����ISO-8859-1ת��ΪGBK
	 * @param tempSql Ҫת�����ַ���
	 * @return
	 */
	public static String GBKCode(String tempSql){
	    String returnString = convertNullCode(tempSql);
	    try{
	        byte[] ascii = returnString.getBytes("ISO-8859-1");
	        returnString = new String(ascii,"GBK");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return returnString;
	}
	/**
	 * ����ת�� ��srcCodeת��ΪdestCode
	 * @param srcCode ԭ����
	 * @param destCode Ŀ�����
	 * @param strTmp Ҫת�����ַ���
	 * @return
	 */
	public static String convertCode(String srcCode,String destCode,String strTmp){
	    String returnString = convertNullCode(strTmp);
	    try{
	    	byte[] ascii=returnString.getBytes(srcCode);
	        returnString =new String(ascii,destCode);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return returnString;
	}
	/**
	 * ����ת����GBKת��Ϊbig5
	 * @param tempSql Ҫת�����ַ���
	 * @return
	 */
	public static String GBK2BIG5Code(String tempSql){
	    String returnString = convertNullCode(tempSql);
	    try{
	        byte[] ascii=returnString.getBytes("GBK");
	        returnString =new String(ascii,"big5");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return returnString;
	}
	//�滻�Ƿ��ַ�
	public static String convertHtml(String input){
		StringBuffer returnString = new StringBuffer(input.length());

	    char ch = ' ';
	    for (int i = 0;i<input.length();i++){

	    	ch = input.charAt( i);

	    	if (ch == '<'){
	    		returnString = returnString.append("&lt");
	    	}else if (ch == '>'){
	    		returnString = returnString.append("&gt");
	    	}else if (ch == ' '){
	    		returnString = returnString.append("&nbsp");
	    	}else if (ch == '\\'){
	    		returnString = returnString.append("&acute");
	    	}else{
	    		returnString = returnString.append(ch);
	    	}
	    }
	    return returnString.toString();
	}

	/*
	 *
	 */
	private String delSQlString(String sql){
	    String delSql = "in(";
	    StringTokenizer Tokenizer = new StringTokenizer(sql,"|");

	    // ��Ǳ�����ڷָ������������
	    delSql += Tokenizer.nextToken().toString();
	    while (Tokenizer.hasMoreTokens()) {
	    	delSql += Tokenizer.nextToken() + ",";
	    }
	    delSql = delSql.substring(0,delSql.length()-1) + ")";
	    return delSql;
	}

	/*
	 * format selectedIDs to sql language
	 * in (...)
	 * second of methods bt own idea
	 */
	private String delNewSQlString(String sql){
		return "in (" + sql.replace('|',',') + ")";
	}

	private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
	private static final char[] AMP_ENCODE = "&amp;".toCharArray();
	private static final char[] LT_ENCODE = "&lt;".toCharArray();
	private static final char[] GT_ENCODE = "&gt;".toCharArray();

	/**
	 * This method takes a string which may contain HTML tags (ie, &lt;b&gt;,
	 * &lt;table&gt;, etc) and converts the '&lt'' and '&gt;' characters to
	 * their HTML escape sequences.
	 *
	 * @param in the text to be converted.
	 * @return the input string with the characters '&lt;' and '&gt;' replaced
	 *  with their HTML escape sequences.
	 */
	public static final String escapeHTMLTags(String in) {
		if (in == null) {
			return null;
		}
		char ch;
		int i=0;
		int last=0;
		char[] input = in.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int)(len*1.3));
		for (; i < len; i++) {
			ch = input[i];

			if (ch > '>') {
				continue;
			} else if (ch == '<') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(LT_ENCODE);
			} else if (ch == '>') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(GT_ENCODE);
			}
		}
		if (last == 0) {
			return in;
		}
		if (i > last) {
			out.append(input, last, i - last);
		}
		return out.toString();
	}

	public static String filterString(String allstr)
	{
		StringBuffer returnString = new StringBuffer(allstr.length());
		char ch = ' ';
		for (int i = 0; i < allstr.length(); i++)
		{
			ch = allstr.charAt(i);
			String lsTemp = "'";
			char lcTemp = lsTemp.charAt(0);
			if (ch == lcTemp)
			{
				returnString.append("''");
			}
			else
			{
				returnString.append(ch);
			}
		}
		return returnString.toString();
	}
	/**
	 * ���ֵĽ����ʽ
	 * @param num
	 * @return
	 */
	public static String convertNumToMoney(int num){
		NumberFormat formatc = NumberFormat.getCurrencyInstance(Locale.CHINA);
		String strcurr = formatc.format(num);
		System.out.println(strcurr);
		//num = NumberFormat.getInstance().setParseIntegerOnly(true));
		return strcurr;
	}

	public static void main(String args[]){
		DealString.convertNumToMoney(1234566);
	}
}
