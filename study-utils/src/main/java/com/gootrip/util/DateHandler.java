package com.gootrip.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author <a href="mailto:royiwu@hotmail.com">advance.wu</a>
 * @version 1.0
 */
import java.util.*;
import java.text.*;

public class DateHandler {

  public DateHandler() {
  }
  public static int openDay=5;
  private String iDate="";
  private int iYear;
  private int iMonth;
  private int iDay;
//  iDateTime = 2002-01-01 23:23:23
  public void setDate(String iDateTime){
    this.iDate=iDateTime.substring(0,10);
  }
  public String getDate(){
    return this.iDate;
  }
  public int getYear(){
    iYear=Integer.parseInt(iDate.substring(0,4));
    return iYear;
  }
  public int getMonth(){
    iMonth=Integer.parseInt(iDate.substring(5,7));
    return iMonth;
  }
  public int getDay(){
    iDay=Integer.parseInt(iDate.substring(8,10));
    return iDay;
  }

  public static String subDate(String date){
    return date.substring(0,10);
  }
  
  /**
   * �����Ƿ��Ǽ���ĩ
   * @param date
   * @return
   */
  public static boolean isSeason(String date){
    int getMonth = Integer.parseInt(date.substring(5,7));
    boolean sign = false;
    if (getMonth==3)
      sign = true;
    if (getMonth==6)
      sign = true;
    if (getMonth==9)
      sign = true;
    if (getMonth==12)
      sign = true;
    return sign;
  }
  
  /**
   * ��������ڿ�ʼ������ʱ��
   * @param afterDay
   * @return
   */
  public static String getDateFromNow(int afterDay){
     GregorianCalendar calendar = new GregorianCalendar();
     Date date = calendar.getTime();

     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

     calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+afterDay);
     date = calendar.getTime();

     return df.format(date);
  }

  /**
   * ����ʽ
   * @param afterDay
   * @param format_string
   * @return
   */
  public static String getDateFromNow(int afterDay, String format_string)
  {
      Calendar calendar = Calendar.getInstance();
      Date date = null;

      DateFormat df = new SimpleDateFormat(format_string);

      calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + afterDay);
      date = calendar.getTime();

      return df.format(date);
  }

  /**
   * �õ���ǰʱ�䣬�����ļ�����û�������ַ���ʹ��yyyyMMddHHmmss��ʽ
   * @param afterDay
   * @return
   * by tim
   */
  public static String getNowForFileName(int afterDay){
    GregorianCalendar calendar = new GregorianCalendar();
//    Date date = calendar.getTime();

    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+afterDay);
    Date date = calendar.getTime();

    return df.format(date);
 }

//==============================================================================
//�Ƚ����ڣ�������-N������ڶԱ�  -1 0 1
//==============================================================================
  public int getDateCompare(String limitDate,int afterDay){
     GregorianCalendar calendar = new GregorianCalendar();
     Date date = calendar.getTime();
     calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+afterDay);
     date = calendar.getTime();//date����������������������ȵ�����

     iDate=limitDate;
     calendar.set(getYear(),getMonth()-1,getDay());
     Date dateLimit = calendar.getTime();
     return dateLimit.compareTo(date);
  }
//==============================================================================
//�Ƚ����ڣ������ڵ����ڶԱ�     
//==============================================================================
  public int getDateCompare(String limitDate){
     iDate=limitDate;
     GregorianCalendar calendar = new GregorianCalendar();
     calendar.set(getYear(),getMonth()-1,getDay());
     Date date = calendar.getTime();

     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     Date nowDate = new Date();
     return date.compareTo(nowDate);
  }
//==============================================================================
//�Ƚ����ڣ������ڵ����ڶԱ�  �õ�����
//==============================================================================
  public long getLongCompare(String limitDate){

     iDate=limitDate;
     GregorianCalendar calendar = new GregorianCalendar();
     calendar.set(getYear(),getMonth()-1,getDay());
     Date date = calendar.getTime();
     long datePP=date.getTime();
     Date nowDate = new Date();
     long dateNow = nowDate.getTime();
     return ((dateNow-datePP)/(24*60*60*1000));

  }
//==============================================================================
//�Ƚ����ڣ������ڵ����ڶԱ�  �õ���Ϣ
//==============================================================================
  public String getStringCompare(String limitDate,int openDay){
         iDate=limitDate;
         GregorianCalendar calendar = new GregorianCalendar();
         calendar.set(getYear(),getMonth()-1,getDay());
         Date date = calendar.getTime();
         long datePP=date.getTime();
         Date nowDate = new Date();
         long dateNow = nowDate.getTime();
         long overDay = ((dateNow-datePP)/(24*60*60*1000));
         String info="";
         return info;

  }
//==============================================================================
//�Ƚ����ڣ������ڵ����ڶԱ�  �õ���Ϣ
//==============================================================================
  public String getPicCompare(String limitDate,int openDay){

       iDate=limitDate;
       GregorianCalendar calendar = new GregorianCalendar();
       calendar.set(getYear(),getMonth()-1,getDay());
       Date date = calendar.getTime();
       long datePP=date.getTime();
       Date nowDate = new Date();
       long dateNow = nowDate.getTime();
       long overDay = ((dateNow-datePP)/(24*60*60*1000));
       String info="";
       if (overDay>0){
           info="plaint1.gif";
       }
       if (overDay==0){
           info="plaint2.gif";
       }
       if (overDay<0&&overDay>=-openDay){
           info="plaint2.gif";
       }
       if (overDay<-openDay){
           info="plaint3.gif";
       }
       if (overDay<-150){
           info="plaint3.gif";
       }
       return info;

  }
  /**
   * method diffdate �����������ڼ����������
   * @param beforDate ��ʽ:2005-06-20
   * @param afterDate ��ʽ:2005-06-21
   * @return
   */
  public static int diffDate(String beforeDate,String afterDate){
	  String[] tt = beforeDate.split("-");
	  Date firstDate = new Date(Integer.parseInt(tt[0]),Integer.parseInt(tt[1])-1,Integer.parseInt(tt[2]));
	  
	  tt = afterDate.split("-");
	  Date nextDate = new Date(Integer.parseInt(tt[0]),Integer.parseInt(tt[1])-1,Integer.parseInt(tt[2]));
	  return (int)(nextDate.getTime()-firstDate.getTime())/(24*60*60*1000);  
  }
  
  /**
   * ��ȡ��������ڵ��ַ���
   * @return
   */
  public static String getToday(){
    Calendar cld=Calendar.getInstance();
    java.util.Date date=new Date();
    cld.setTime(date);
    int intMon=cld.get(Calendar.MONTH)+1;
    int intDay=cld.get(Calendar.DAY_OF_MONTH);
    String mons=String.valueOf(intMon);
    String days=String.valueOf(intDay);
    if(intMon<10)
      mons="0"+String.valueOf(intMon);
    if(intDay<10)
      days="0"+String.valueOf(intDay);
    return String.valueOf(cld.get(Calendar.YEAR))+"-"+mons+"-"+days;
  }

	/**
	 * ��ȡ��ǰ�·�
	 * @return �����ַ��� ��ʽ����λ��
	 */
	public static String getCurrentMonth(){
		String strmonth = null;
	    Calendar cld = Calendar.getInstance();
	    java.util.Date date = new Date();
	    cld.setTime(date);
	    int intMon=cld.get(Calendar.MONTH) + 1;
		if(intMon<10)
			strmonth = "0" + String.valueOf(intMon);
		else
			strmonth = String.valueOf(intMon);
		date = null;
		return strmonth;
	}
  
//  public static String getCurrMonth()
//  {
//    Calendar cld=Calendar.getInstance();
//    java.util.Date date=new Date();
//    cld.setTime(date);
//    int intMon=cld.get(Calendar.MONTH)+1;
//
//    return String.valueOf(intMon).toString();
//  }

  /**
   * ��ȡ��������ڵ��ַ���
   */
  public static String getYestoday(){
    Calendar cld = Calendar.getInstance();
    java.util.Date date = new Date();
    cld.setTime(date);
    cld.add(Calendar.DATE,-1);
    int intMon = cld.get(Calendar.MONTH)+1;
    int intDay = cld.get(Calendar.DAY_OF_MONTH);
    String mons = String.valueOf(intMon);
    String days = String.valueOf(intDay);
    if(intMon < 10)
    	mons="0" + String.valueOf(intMon);
    if(intDay < 10)
    	days = "0" + String.valueOf(intDay);
    return String.valueOf(cld.get(Calendar.YEAR)) + "-" + mons + "-" + days;
  }

  /**
   * �˺�����������Ա���Ĺ�������������ʹ���ں���ְ�ڸ��·ݵĹ�����
   * ���루��ְ���ڣ�-1���ɵø��¹�������  ʱ����2002-12-14Ϊ׼
   * ���루��˾ʱ�䣬1���ɵĸ��¹�������
   */
  public static int getWorkDay(String date , int sign){
    int month=0;
    int week=0;
    int workDay=0;
    Calendar rightNow = Calendar.getInstance();

    DateHandler dateOver=new DateHandler();
    dateOver.setDate(date);

    rightNow.set(rightNow.YEAR,dateOver.getYear());
    rightNow.set(rightNow.MONTH,dateOver.getMonth()-1);
    rightNow.set(rightNow.DATE,dateOver.getDay());

    month = rightNow.get(rightNow.MONTH);

    while(rightNow.get(rightNow.MONTH)==month){
        week=rightNow.get(Calendar.DAY_OF_WEEK);
        if (week==1||week==7){
        }else{
            workDay++;
            System.out.println(rightNow.get(rightNow.DATE));
        }
        rightNow.add(rightNow.DATE,sign);
        }
    return workDay;
  }

  public static void main(String args[]){
	  System.out.println(DateHandler.isSeason("2002-03-02"));
//    String cc ="100.123.342";
//    System.out.println(cc.indexOf(".",3));
//
//    StringTokenizer st=new StringTokenizer(cc,".");
//
//    if (st.countTokens()!=2) {
//
//    String state = st.nextToken();
//    String event = st.nextToken();
//    System.out.println(""+event);
	  String strDate = DateHandler.getDateFromNow(0,"yyyy-MM-dd HH:mm:ss");
      System.out.println("date:" + strDate);
      System.out.println("15:" + strDate.substring(0,16));
      
      Date firstDate = new Date(2006,11,14,18,3,0);
      Date nextDate = new Date(2006,11,15,18,2,0);
      System.out.println("date's num: " + (int)(nextDate.getTime()-firstDate.getTime())/(24*60*60*1000));
//    }
  //System.out.println(DateHandler.getWorkDay("2002-11-14",-1));
  }
}
