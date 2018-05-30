package com.gootrip.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.filechooser.FileFilter;

/**
 * �����з�װһЩ���õ��ļ�������
 * ���з������Ǿ�̬����������Ҫ���ɴ����ʵ����
 * Ϊ�������ɴ����ʵ�������췽��������Ϊprivate���͵ġ�
 * @since  1.0
 */

public class FileUtil {
  /**
   * ˽�й��췽������ֹ���ʵ��������Ϊ�����಻��Ҫʵ������
   */
  private FileUtil() {

  }

  /**
   * �޸��ļ���������ʱ�䡣
   * ����ļ��������򴴽����ļ���
   * <b>Ŀǰ�����������Ϊ��ʽ�����ȶ�����Ҫ�Ƿ�����Щ��Ϣ�������Щ��Ϣ����Ƿ������ڿ����С�</b>
   * @param file ��Ҫ�޸�������ʱ����ļ���
   * @since  1.0
   */
  public static void touch(File file) {
    long currentTime = System.currentTimeMillis();
    if (!file.exists()) {
      System.err.println("file not found:" + file.getName());
      System.err.println("Create a new file:" + file.getName());
      try {
        if (file.createNewFile()) {
          System.out.println("Succeeded!");
        }
        else {
          System.err.println("Create file failed!");
        }
      }
      catch (IOException e) {
        System.err.println("Create file failed!");
        e.printStackTrace();
      }
    }
    boolean result = file.setLastModified(currentTime);
    if (!result) {
      System.err.println("touch failed: " + file.getName());
    }
  }

  /**
   * �޸��ļ���������ʱ�䡣
   * ����ļ��������򴴽����ļ���
   * <b>Ŀǰ�����������Ϊ��ʽ�����ȶ�����Ҫ�Ƿ�����Щ��Ϣ�������Щ��Ϣ����Ƿ������ڿ����С�</b>
   * @param fileName ��Ҫ�޸�������ʱ����ļ����ļ�����
   * @since  1.0
   */
  public static void touch(String fileName) {
    File file = new File(fileName);
    touch(file);
  }

  /**
   * �޸��ļ���������ʱ�䡣
   * ����ļ��������򴴽����ļ���
   * <b>Ŀǰ�����������Ϊ��ʽ�����ȶ�����Ҫ�Ƿ�����Щ��Ϣ�������Щ��Ϣ����Ƿ������ڿ����С�</b>
   * @param files ��Ҫ�޸�������ʱ����ļ����顣
   * @since  1.0
   */
  public static void touch(File[] files) {
    for (int i = 0; i < files.length; i++) {
      touch(files[i]);
    }
  }

  /**
   * �޸��ļ���������ʱ�䡣
   * ����ļ��������򴴽����ļ���
   * <b>Ŀǰ�����������Ϊ��ʽ�����ȶ�����Ҫ�Ƿ�����Щ��Ϣ�������Щ��Ϣ����Ƿ������ڿ����С�</b>
   * @param fileNames ��Ҫ�޸�������ʱ����ļ������顣
   * @since  1.0
   */
  public static void touch(String[] fileNames) {
    File[] files = new File[fileNames.length];
    for (int i = 0; i < fileNames.length; i++) {
      files[i] = new File(fileNames[i]);
    }
    touch(files);
  }

  /**
   * �ж�ָ�����ļ��Ƿ���ڡ�
   * @param fileName Ҫ�жϵ��ļ����ļ���
   * @return ����ʱ����true�����򷵻�false��
   * @since  1.0
   */
  public static boolean isFileExist(String fileName) {
    return new File(fileName).isFile();
  }

  /**
   * ����ָ����Ŀ¼��
   * ���ָ����Ŀ¼�ĸ�Ŀ¼�������򴴽���Ŀ¼����������Ҫ�ĸ�Ŀ¼��
   * <b>ע�⣺���ܻ��ڷ���false��ʱ�򴴽����ָ�Ŀ¼��</b>
   * @param file Ҫ������Ŀ¼
   * @return ��ȫ�����ɹ�ʱ����true�����򷵻�false��
   * @since  1.0
   */
  public static boolean makeDirectory(File file) {
    File parent = file.getParentFile();
    if (parent != null) {
      return parent.mkdirs();
    }
    return false;
  }

  /**
   * ����ָ����Ŀ¼��
   * ���ָ����Ŀ¼�ĸ�Ŀ¼�������򴴽���Ŀ¼����������Ҫ�ĸ�Ŀ¼��
   * <b>ע�⣺���ܻ��ڷ���false��ʱ�򴴽����ָ�Ŀ¼��</b>
   * @param fileName Ҫ������Ŀ¼��Ŀ¼��
   * @return ��ȫ�����ɹ�ʱ����true�����򷵻�false��
   * @since  1.0
   */
  public static boolean makeDirectory(String fileName) {
    File file = new File(fileName);
    return makeDirectory(file);
  }

  /**
   * ���ָ��Ŀ¼�е��ļ���
   * ���������������ɾ�����е��ļ�������ֻҪ��һ���ļ�û�б�ɾ�����᷵��false��
   * ������������������ɾ����������ɾ����Ŀ¼�������ݡ�
   * @param directory Ҫ��յ�Ŀ¼
   * @return Ŀ¼�µ������ļ������ɹ�ɾ��ʱ����true�����򷵻�false.
   * @since  1.0
   */
  public static boolean emptyDirectory(File directory) {
    boolean result = true;
    File[] entries = directory.listFiles();
    for (int i = 0; i < entries.length; i++) {
      if (!entries[i].delete()) {
        result = false;
      }
    }
    return result;
  }

  /**
   * ���ָ��Ŀ¼�е��ļ���
   * ���������������ɾ�����е��ļ�������ֻҪ��һ���ļ�û�б�ɾ�����᷵��false��
   * ������������������ɾ����������ɾ����Ŀ¼�������ݡ�
   * @param directoryName Ҫ��յ�Ŀ¼��Ŀ¼��
   * @return Ŀ¼�µ������ļ������ɹ�ɾ��ʱ����true�����򷵻�false��
   * @since  1.0
   */
  public static boolean emptyDirectory(String directoryName) {
    File dir = new File(directoryName);
    return emptyDirectory(dir);
  }

  /**
   * ɾ��ָ��Ŀ¼�����е��������ݡ�
   * @param dirName Ҫɾ����Ŀ¼��Ŀ¼��
   * @return ɾ���ɹ�ʱ����true�����򷵻�false��
   * @since  1.0
   */
  public static boolean deleteDirectory(String dirName) {
    return deleteDirectory(new File(dirName));
  }

  /**
   * ɾ��ָ��Ŀ¼�����е��������ݡ�
   * @param dir Ҫɾ����Ŀ¼
   * @return ɾ���ɹ�ʱ����true�����򷵻�false��
   * @since  1.0
   */
  public static boolean deleteDirectory(File dir) {
    if ( (dir == null) || !dir.isDirectory()) {
      throw new IllegalArgumentException("Argument " + dir +
                                         " is not a directory. ");
    }

    File[] entries = dir.listFiles();
    int sz = entries.length;

    for (int i = 0; i < sz; i++) {
      if (entries[i].isDirectory()) {
        if (!deleteDirectory(entries[i])) {
          return false;
        }
      }
      else {
        if (!entries[i].delete()) {
          return false;
        }
      }
    }

    if (!dir.delete()) {
      return false;
    }
    return true;
  }

  /**
   * �г�Ŀ¼�е��������ݣ���������Ŀ¼�е����ݡ�
   * @param fileName Ҫ�г���Ŀ¼��Ŀ¼��
   * @return Ŀ¼���ݵ��ļ����顣
   * @since  1.0
   */
  /*public static File[] listAll(String fileName) {
    return listAll(new File(fileName));
  }*/

  /**
   * �г�Ŀ¼�е��������ݣ���������Ŀ¼�е����ݡ�
   * @param file Ҫ�г���Ŀ¼
   * @return Ŀ¼���ݵ��ļ����顣
   * @since  1.0
   */
  /*public static File[] listAll(File file) {
    ArrayList list = new ArrayList();
    File[] files;
    if (!file.exists() || file.isFile()) {
      return null;
    }
    list(list, file, new AllFileFilter());
    list.remove(file);
    files = new File[list.size()];
    list.toArray(files);
    return files;
  }*/

  /**
   * �г�Ŀ¼�е��������ݣ���������Ŀ¼�е����ݡ�
   * @param file Ҫ�г���Ŀ¼
   * @param filter ������
   * @return Ŀ¼���ݵ��ļ����顣
   * @since  1.0
   */
  public static File[] listAll(File file,
                               javax.swing.filechooser.FileFilter filter) {
    ArrayList list = new ArrayList();
    File[] files;
    if (!file.exists() || file.isFile()) {
      return null;
    }
    list(list, file, filter);
    files = new File[list.size()];
    list.toArray(files);
    return files;
  }

  /**
   * ��Ŀ¼�е�������ӵ��б�
   * @param list �ļ��б�
   * @param filter ������
   * @param file Ŀ¼
   */
  private static void list(ArrayList list, File file,
                           javax.swing.filechooser.FileFilter filter) {
    if (filter.accept(file)) {
      list.add(file);
      if (file.isFile()) {
        return;
      }
    }
    if (file.isDirectory()) {
      File files[] = file.listFiles();
      for (int i = 0; i < files.length; i++) {
        list(list, files[i], filter);
      }
    }

  }

  /**
   * �����ļ���URL��ַ��
   * @param file �ļ�
   * @return �ļ���Ӧ�ĵ�URL��ַ
   * @throws MalformedURLException
   * @since  1.0
   * @deprecated ��ʵ�ֵ�ʱ��û��ע�⵽File�౾���һ��toURL�������ļ�·��ת��ΪURL��
   *             ��ʹ��File.toURL������
   */
  public static URL getURL(File file) throws MalformedURLException {
    String fileURL = "file:/" + file.getAbsolutePath();
    URL url = new URL(fileURL);
    return url;
  }

  /**
   * ���ļ�·���õ��ļ�����
   * @param filePath �ļ���·�������������·��Ҳ�����Ǿ���·��
   * @return ��Ӧ���ļ���
   * @since  1.0
   */
  public static String getFileName(String filePath) {
    File file = new File(filePath);
    return file.getName();
  }

  /**
   * ���ļ����õ��ļ�����·����
   * @param fileName �ļ���
   * @return ��Ӧ���ļ�·��
   * @since  1.0
   */
  public static String getFilePath(String fileName) {
    File file = new File(fileName);
    return file.getAbsolutePath();
  }

  /**
   * ��DOS/Windows��ʽ��·��ת��ΪUNIX/Linux��ʽ��·����
   * ��ʵ���ǽ�·���е�"\"ȫ����Ϊ"/"����Ϊ��ĳЩ���������ת��Ϊ���ַ�ʽ�ȽϷ��㣬
   * ĳ�г̶���˵"/"��"\"���ʺ���Ϊ·���ָ���������DOS/WindowsҲ��������·���ָ�����
   * @param filePath ת��ǰ��·��
   * @return ת�����·��
   * @since  1.0
   */
  public static String toUNIXpath(String filePath) {
    return filePath.replace('\\', '/');
  }

  /**
   * ���ļ����õ�UNIX�����ļ�����·����
   * @param fileName �ļ���
   * @return ��Ӧ��UNIX�����ļ�·��
   * @since  1.0
   * @see #toUNIXpath(String filePath) toUNIXpath
   */
  public static String getUNIXfilePath(String fileName) {
    File file = new File(fileName);
    return toUNIXpath(file.getAbsolutePath());
  }

  /**
   * �õ��ļ������͡�
   * ʵ���Ͼ��ǵõ��ļ��������һ����.������Ĳ��֡�
   * @param fileName �ļ���
   * @return �ļ����е����Ͳ���
   * @since  1.0
   */
  public static String getTypePart(String fileName) {
    int point = fileName.lastIndexOf('.');
    int length = fileName.length();
    if (point == -1 || point == length - 1) {
      return "";
    }
    else {
      return fileName.substring(point + 1, length);
    }
  }

  /**
   * �õ��ļ������͡�
   * ʵ���Ͼ��ǵõ��ļ��������һ����.������Ĳ��֡�
   * @param file �ļ�
   * @return �ļ����е����Ͳ���
   * @since  1.0
   */
  public static String getFileType(File file) {
    return getTypePart(file.getName());
  }

  /**
   * �õ��ļ������ֲ��֡�
   * ʵ���Ͼ���·���е����һ��·���ָ�����Ĳ��֡�
   * @param fileName �ļ���
   * @return �ļ����е����ֲ���
   * @since  1.0
   */
  public static String getNamePart(String fileName) {
    int point = getPathLsatIndex(fileName);
    int length = fileName.length();
    if (point == -1) {
      return fileName;
    }
    else if (point == length - 1) {
      int secondPoint = getPathLsatIndex(fileName, point - 1);
      if (secondPoint == -1) {
        if (length == 1) {
          return fileName;
        }
        else {
          return fileName.substring(0, point);
        }
      }
      else {
        return fileName.substring(secondPoint + 1, point);
      }
    }
    else {
      return fileName.substring(point + 1);
    }
  }

  /**
   * �õ��ļ����еĸ�·�����֡�
   * ������·���ָ�������Ч��
   * ������ʱ����""��
   * ����ļ�������·���ָ�����β���򲻿��Ǹ÷ָ���������"/path/"����""��
   * @param fileName �ļ���
   * @return ��·���������ڻ����Ѿ��Ǹ�Ŀ¼ʱ����""
   * @since  1.0
   */
  public static String getPathPart(String fileName) {
    int point = getPathLsatIndex(fileName);
    int length = fileName.length();
    if (point == -1) {
      return "";
    }
    else if (point == length - 1) {
      int secondPoint = getPathLsatIndex(fileName, point - 1);
      if (secondPoint == -1) {
        return "";
      }
      else {
        return fileName.substring(0, secondPoint);
      }
    }
    else {
      return fileName.substring(0, point);
    }
  }

  /**
   * �õ�·���ָ������ļ�·�����״γ��ֵ�λ�á�
   * ����DOS����UNIX���ķָ��������ԡ�
   * @param fileName �ļ�·��
   * @return ·���ָ�����·�����״γ��ֵ�λ�ã�û�г���ʱ����-1��
   * @since  1.0
   */
  public static int getPathIndex(String fileName) {
    int point = fileName.indexOf('/');
    if (point == -1) {
      point = fileName.indexOf('\\');
    }
    return point;
  }

  /**
   * �õ�·���ָ������ļ�·����ָ��λ�ú��״γ��ֵ�λ�á�
   * ����DOS����UNIX���ķָ��������ԡ�
   * @param fileName �ļ�·��
   * @param fromIndex ��ʼ���ҵ�λ��
   * @return ·���ָ�����·����ָ��λ�ú��״γ��ֵ�λ�ã�û�г���ʱ����-1��
   * @since  1.0
   */
  public static int getPathIndex(String fileName, int fromIndex) {
    int point = fileName.indexOf('/', fromIndex);
    if (point == -1) {
      point = fileName.indexOf('\\', fromIndex);
    }
    return point;
  }

  /**
   * �õ�·���ָ������ļ�·���������ֵ�λ�á�
   * ����DOS����UNIX���ķָ��������ԡ�
   * @param fileName �ļ�·��
   * @return ·���ָ�����·���������ֵ�λ�ã�û�г���ʱ����-1��
   * @since  1.0
   */
  public static int getPathLsatIndex(String fileName) {
    int point = fileName.lastIndexOf('/');
    if (point == -1) {
      point = fileName.lastIndexOf('\\');
    }
    return point;
  }

  /**
   * �õ�·���ָ������ļ�·����ָ��λ��ǰ�����ֵ�λ�á�
   * ����DOS����UNIX���ķָ��������ԡ�
   * @param fileName �ļ�·��
   * @param fromIndex ��ʼ���ҵ�λ��
   * @return ·���ָ�����·����ָ��λ��ǰ�����ֵ�λ�ã�û�г���ʱ����-1��
   * @since  1.0
   */
  public static int getPathLsatIndex(String fileName, int fromIndex) {
    int point = fileName.lastIndexOf('/', fromIndex);
    if (point == -1) {
      point = fileName.lastIndexOf('\\', fromIndex);
    }
    return point;
  }

  /**
   * ���ļ����е����Ͳ���ȥ����
   * @param filename �ļ���
   * @return ȥ�����Ͳ��ֵĽ��
   * @since  1.0
   */
  public static String trimType(String filename) {
    int index = filename.lastIndexOf(".");
    if (index != -1) {
      return filename.substring(0, index);
    }
    else {
      return filename;
    }
  }
  /**
   * �õ����·����
   * �ļ�������Ŀ¼�����ӽڵ�ʱ�����ļ�����
   * @param pathName Ŀ¼��
   * @param fileName �ļ���
   * @return �õ��ļ��������Ŀ¼�������·����Ŀ¼�²����ڸ��ļ�ʱ�����ļ���
   * @since  1.0
   */
  public static String getSubpath(String pathName,String fileName) {
    int index = fileName.indexOf(pathName);
    if (index != -1) {
      return fileName.substring(index + pathName.length() + 1);
    }
    else {
      return fileName;
    }
  }

  /**
   * ������Ŀ¼�Ĵ�����
   * ��ָ֤����·�����ã����ָ����·�������ڣ���ô������·��������Ϊ�༶·��
   * @param path
   * @return ���ֵ
   * @since  1.0
   */
   public static final boolean pathValidate(String path)
   {
     //String path="d:/web/www/sub";
     //System.out.println(path);
     //path = getUNIXfilePath(path);

     //path = ereg_replace("^\\/+", "", path);
     //path = ereg_replace("\\/+$", "", path);
     String[] arraypath = path.split("/");
     String tmppath = "";
     for (int i = 0; i < arraypath.length; i++)
     {
       tmppath += "/" + arraypath[i];
       File d = new File(tmppath.substring(1));
       if (!d.exists()) { //���SubĿ¼�Ƿ����
           System.out.println(tmppath.substring(1));
         if (!d.mkdir())
         {
           return false;
         }
       }
     }
     return true;
   }

   /**
    * ��ȡ�ļ�������
    * ��ȡָ���ļ�������
    * @param path ΪҪ��ȡ�ļ��ľ���·��
    * @return ���ж�ȡ�ļ�������ݡ�
    * @since  1.0
    */
   public static final String getFileContent(String path) throws IOException
   {
     String filecontent = "";
     try {
       File f = new File(path);
       if (f.exists()) {
         FileReader fr = new FileReader(path);
         BufferedReader br = new BufferedReader(fr); //����BufferedReader���󣬲�ʵ����Ϊbr
         String line = br.readLine(); //���ļ���ȡһ���ַ���
         //�ж϶�ȡ�����ַ����Ƿ�Ϊ��
         while (line != null) {
           filecontent += line + "\n";
           line = br.readLine(); //���ļ��м�����ȡһ������
         }
         br.close(); //�ر�BufferedReader����
         fr.close(); //�ر��ļ�
       }

     }
     catch (IOException e) {
       throw e;
     }
     return filecontent;
   }

   /**
    * �������������ļ�
    * @param pathҪ�����ļ��ľ���·����
    * @param �ļ������ݡ�
    * @return ���ֵ
    * @since  1.0
    */
   public static final boolean genModuleTpl(String path, String modulecontent)  throws IOException
   {

     path = getUNIXfilePath(path);
     String[] patharray = path.split("\\/");
     String modulepath = "";
     for (int i = 0; i < patharray.length - 1; i++) {
       modulepath += "/" + patharray[i];
     }
     File d = new File(modulepath.substring(1));
     if (!d.exists()) {
       if (!pathValidate(modulepath.substring(1))) {
         return false;
       }
     }
     try {
       FileWriter fw = new FileWriter(path); //����FileWriter���󣬲�ʵ����fw
       //���ַ���д���ļ�
       fw.write(modulecontent);
       fw.close();
     }
     catch (IOException e) {
       throw e;
     }
     return true;
   }

   /**
    * ��ȡͼƬ�ļ�����չ��������ϵͳר�ã�
    * @param picname ΪͼƬ���Ƽ���ǰ���·����������չ��
    * @return ͼƬ����չ��
    * @since  1.0
    */
   public static final String getPicExtendName(String pic_path)
   {
     pic_path = getUNIXfilePath(pic_path);
     String pic_extend = "";
     if (isFileExist(pic_path + ".gif"))
     {
       pic_extend = ".gif";
     }
     if (isFileExist(pic_path + ".jpeg"))
     {
       pic_extend = ".jpeg";
     }
     if (isFileExist(pic_path + ".jpg"))
     {
       pic_extend = ".jpg";
     }
     if (isFileExist(pic_path + ".png"))
     {
       pic_extend = ".png";
     }
     return pic_extend; //����ͼƬ��չ��
   }
   //�����ļ�
   public static final boolean CopyFile(File in, File out) throws Exception {
       try {
           FileInputStream fis = new FileInputStream(in);
           FileOutputStream fos = new FileOutputStream(out);
           byte[] buf = new byte[1024];
           int i = 0;
           while ((i = fis.read(buf)) != -1) {
               fos.write(buf, 0, i);
           }
           fis.close();
           fos.close();
           return true;
       } catch (IOException ie) {
           ie.printStackTrace();
           return false;
       }
   }
   //�����ļ�
   public static final boolean CopyFile(String infile, String outfile) throws Exception {
       try {
           File in = new File(infile);
           File out = new File(outfile);
           return CopyFile(in, out);
       } catch (IOException ie) {
           ie.printStackTrace();
           return false;
       }

   }
   /**
    * ����ͼƬ����
    * @param id
    * @param dtime
    * @return
    */
   public static final int countPics(String id,String dtime,String extensions){
	   int counts = 0; 
	   
	   MyFileFilter mfilter = new MyFileFilter(extensions.split(","));
       PropsUtil pu = new PropsUtil();
       String PICROOT = pu.readSingleProps("DestinationsPICROOT").trim();
       String path = PICROOT + "/"+dtime.substring(0, 10) + "/";
       File lfile = new File(path);
       String filename;
       if(lfile.isDirectory()){
    	   File[] files = lfile.listFiles(mfilter);
    	   for(int i=0;i<files.length;i++){
    		   filename = files[i].getName();
    		   if((filename.indexOf(id + "_")==0)&&(filename.indexOf("_small")>-1))
    			   counts ++;
    	   }
    	   files = null;
       }
       filename = null;
       lfile = null;
       pu = null;
       mfilter = null;
    	   
	   return counts;
   }

}
