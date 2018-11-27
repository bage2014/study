package com.gootrip.util;

import java.util.*;
import java.io.*;

public class PropsUtil {

    //�����ļ�һ��ŵ�
    ////��db.properties����ϵͳ����java.home�£�һ����jdkĿ¼�µ� jreĿ¼�

    //private static final String javaHome = System.getProperty("java.home");
    //private static final String FS = System.getProperty("file.separator");

    //private static final String propsFileName = javaHome + FS + "destinations.properties";

    /**
      * ���캯��
      * �ҵ�����Դ�������������Դ��������
      */
     public PropsUtil() {

     }

    public String getPropsFilePath()
    {
        String filePath = this.getClass().getResource("/").getPath().toString();
        filePath = filePath.substring(0, filePath.indexOf("classes")-1) + "/destinations.properties";
        return filePath;
    }

    public InputStream getPropsIS()
    {
        InputStream ins = this.getClass().getResourceAsStream("/destinations.properties");
        return ins;
    }
    /**
     * ��ȡ�����ļ��е�����ֵ
     * @param attr
     * @return
     */
    public String readSingleProps(String attr){
        String retValue = "";
        Properties props = new Properties();
        try {
            /*if (!FileUtil.isFileExist(getPropsFilePath())) {
                return "";
            }
            FileInputStream fi = new FileInputStream(getPropsFilePath());*/
            InputStream fi = getPropsIS();
            props.load(fi);
            fi.close();

            retValue = props.getProperty(attr);
        } catch (Exception e) {
            return "";
        }
        return retValue;
    }
    /**
     * ��ȡ�����ļ��е�����ֵ
     * @return
     */
    public HashMap readAllProps(){
        HashMap h = new HashMap();
        Properties props = new Properties();

        try {
            /*if (!FileUtil.isFileExist(getPropsFilePath()))
                return new HashMap();
            FileInputStream fi = new FileInputStream(getPropsFilePath());*/
            InputStream fi = getPropsIS();
            props.load(fi);
            fi.close();
            Enumeration er = props.propertyNames();
            while (er.hasMoreElements()) {
                String paramName = (String) er.nextElement();
                h.put(paramName, props.getProperty(paramName));
            }
        } catch (Exception e) {
            return new HashMap();
        }
        return h;
    }
}
