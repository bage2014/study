package com.bage.study.best.practice.trial.io;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class FileWriterService {

    public static void main(String[] args) {
        new FileWriterService().append("", "");
        new FileWriterService().read("");
    }

    public int append(String fileName, String content) {
        Writer writer = null;
        BufferedWriter bw = null;
        try {
            /*
             * 完整写入文件
             */
            writer = new FileWriter("." + File.separator + fileName);
            bw = new BufferedWriter(writer);
            // 注意这里调用了toString方法
            bw.write(content.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // 注意这两个关闭的顺序
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {

                }
            }
        }
        return 1;
    }

    public int read(String fileName) {
        Reader reader = null;

        // 这里我们用到了字符操作的BufferedReader类
        BufferedReader bufferedReader = null;
        try {
            // 声明一个可变长的stringBuffer对象
            StringBuffer sb = new StringBuffer("");

            /*
             * 读取完整文件
             */
            reader = new FileReader("." + File.separator + fileName);

            // 这里我们用到了字符操作的BufferedReader类
            bufferedReader = new BufferedReader(reader);
            String string = null;
            // 按行读取，结束的判断是是否为null，按字节或者字符读取时结束的标志是-1
            while ((string = bufferedReader.readLine()) != null) {
                // 这里我们用到了StringBuffer的append方法，这个比string的“+”要高效
                sb.append(string + "/n");
                System.out.println(string);
            }
            // 注意这两个关闭的顺序
            bufferedReader.close();
            reader.close();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {

                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
        return 1;
    }

}
