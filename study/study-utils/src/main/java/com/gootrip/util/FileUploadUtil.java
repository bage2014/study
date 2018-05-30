package com.gootrip.util;

import java.io.File;
import java.util.*;
import org.apache.commons.fileupload.*;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;
import java.io.IOException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import java.util.regex.Matcher;


/**
 * @author 
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class FileUploadUtil {

    //���ϴ��ļ���������ʱ�趨����ʱ�ļ�λ�ã�ע���Ǿ���·��
    private String tempPath = null;

    //�ļ��ϴ�Ŀ��Ŀ¼��ע���Ǿ���·��
    private String dstPath = null;

    //���ļ����ƣ�������ʱĬ��Ϊԭ�ļ���
    private String newFileName = null;
    //��ȡ���ϴ�����
    private HttpServletRequest fileuploadReq = null;

    //�������ֻ�������ڴ��д洢������,��λ:�ֽڣ����������Ҫ����̫��
    private int sizeThreshold = 4096;

    //���������û��ϴ��ļ���С,��λ:�ֽ�
    //��10M
    private long sizeMax = 10485760;

    //ͼƬ�ļ����
    private int picSeqNo = 1;

    private boolean isSmallPic = false;

    public FileUploadUtil(){
    }

    public FileUploadUtil(String tempPath, String destinationPath){
        this.tempPath  = tempPath;
        this.dstPath = destinationPath;
    }

    public FileUploadUtil(String tempPath, String destinationPath, HttpServletRequest fileuploadRequest){
        this.tempPath   = tempPath;
        this.dstPath = destinationPath;
        this.fileuploadReq = fileuploadRequest;
    }

    /** �ļ�����
     * @return true ���� success; false ���� fail.
     */
    public boolean Upload(){
        DiskFileItemFactory factory = new DiskFileItemFactory();

        try {

            //���û���ϴ�Ŀ��Ŀ¼���򴴽���
            FileUtil.makeDirectory(dstPath+"/ddd");
            /*if (!FileUtil.makeDirectory(dstPath+"/ddd")) {
                throw new IOException("Create destination Directory Error.");
            }*/
            //���û����ʱĿ¼���򴴽���
            FileUtil.makeDirectory(tempPath+"/ddd");
            /*if (!FileUtil.makeDirectory(tempPath+"/ddd")) {
                throw new IOException("Create Temp Directory Error.");
            }*/

            //�ϴ���ĿֻҪ�㹻С����Ӧ�ñ������ڴ��
            //�ϴ����ĿӦ�ñ�д��Ӳ�̵���ʱ�ļ��ϡ�
            //�ǳ�����ϴ�����Ӧ�ñ��⡣
            //������Ŀ���ڴ�����ռ�Ŀռ䣬���������ϴ����󣬲����趨��ʱ�ļ���λ�á�

            //�������ֻ�������ڴ��д洢������,��λ:�ֽ�
            factory.setSizeThreshold(sizeThreshold);
            // the location for saving data that is larger than getSizeThreshold()
            factory.setRepository(new File(tempPath));

            ServletFileUpload upload = new ServletFileUpload(factory);
            //���������û��ϴ��ļ���С,��λ:�ֽ�
            upload.setSizeMax(sizeMax);

            List fileItems = upload.parseRequest(fileuploadReq);
            // assume we know there are two files. The first file is a small
            // text file, the second is unknown and is written to a file on
            // the server
            Iterator iter = fileItems.iterator();

            //  ����ƥ�䣬����·��ȡ�ļ���
            String regExp = ".+\\\\(.+)$";

            //  ���˵����ļ�����
            String[] errorType = {".exe", ".com", ".cgi", ".asp", ".php", ".jsp"};
            Pattern p = Pattern.compile(regExp);
            while (iter.hasNext()) {
                System.out.println("++00++====="+newFileName);
                FileItem item = (FileItem) iter.next();
                //�������������ļ�������б���Ϣ
                if (!item.isFormField()) {
                    String name = item.getName();
                    System.out.println("++++====="+name);
                    long size = item.getSize();
                    //�ж���ļ���ʱ��ֻ�ϴ����ļ���
                    if ((name == null || name.equals("")) && size == 0)
                        continue;
                    Matcher m = p.matcher(name);
                    boolean result = m.find();
                    if (result) {
                        for (int temp = 0; temp < errorType.length; temp++) {
                            if (m.group(1).endsWith(errorType[temp])) {
                                throw new IOException(name + ": Wrong File Type");
                            }
                        }
                        String ext = "."+FileUtil.getTypePart(name);
                        try {
                            //�����ϴ����ļ���ָ����Ŀ¼
                            //���������ϴ��ļ������ݿ�ʱ�����������д
                            //û��ָ�����ļ���ʱ��ԭ�ļ���������
                            if (newFileName == null || newFileName.trim().equals(""))
                            {
                                item.write(new File(dstPath +"/"+ m.group(1)));
                            }
                            else
                            {
                                String uploadfilename = "";
                                if (isSmallPic)
                                {
                                    uploadfilename = dstPath +"/"+ newFileName+"_"+picSeqNo+"_small"+ext;
                                }
                                else
                                {
                                    uploadfilename = dstPath +"/"+ newFileName+"_"+picSeqNo+ext;
                                }
                                //��������δ���ɵ�Ŀ¼
                                System.out.println("++++====="+uploadfilename);
                                FileUtil.makeDirectory(uploadfilename);
                                //item.write(new File(dstPath +"/"+ newFileName));
                                item.write(new File(uploadfilename));
                            }
                            picSeqNo++;
                            //out.print(name + "&nbsp;&nbsp;" + size + "<br>");
                        } catch (Exception e) {
                            //out.println(e);
                            throw new IOException(e.getMessage());
                        }
                    } else {
                        throw new IOException("fail to upload");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } catch (FileUploadException e) {
            System.out.println(e);
        }
        return true;
    }

    /**��·���л�ȡ�����ļ���
     * @author
     *
     * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
     * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
     */
    public String GetFileName(String filepath)
    {
        String returnstr = "*.*";
        int length       = filepath.trim().length();

        filepath = filepath.replace('\\', '/');
        if(length >0)
        {
            int i = filepath.lastIndexOf("/");
            if (i >= 0)
            {
                filepath  = filepath.substring(i + 1);
                returnstr = filepath;
            }
        }
        return returnstr;
    }
    /**
     * ������ʱ����Ŀ¼
     */
    public void setTmpPath(String tmppath)
    {
        this.tempPath = tmppath;
    }
    /**
     * ����Ŀ��Ŀ¼
     */
    public void setDstPath(String dstpath) {
        this.dstPath = dstpath;
    }
    /**
     * ��������ϴ��ļ��ֽ�����������ʱĬ��10M
     */
    public void setFileMaxSize(long maxsize) {
        this.sizeMax = maxsize;
    }
    /**
     * ����Http ���������ͨ�������������ȡ�ļ���Ϣ
     */
    public void setHttpReq(HttpServletRequest httpreq) {
        this.fileuploadReq = httpreq;
    }
    /**
     * ����Http ���������ͨ�������������ȡ�ļ���Ϣ
     */
    public void setNewFileName(String filename) {
        this.newFileName = filename;
    }

    /**
     * ���ô��ϴ��ļ��Ƿ�������ͼ�ļ������������Ҫ��������ͼ����
     */
    public void setIsSmalPic(boolean isSmallPic) {
        this.isSmallPic = isSmallPic;
    }

    /**
     * ����Http ���������ͨ�������������ȡ�ļ���Ϣ
     */
    public void setPicSeqNo(int seqNo) {
        this.picSeqNo = seqNo;
    }


}
