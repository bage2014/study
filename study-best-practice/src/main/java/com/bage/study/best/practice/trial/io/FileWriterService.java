package com.bage.study.best.practice.trial.io;

import cn.hutool.core.util.JdkUtil;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class FileWriterService {

    public static void main(String[] args) {
        new FileWriterService().append("");

        JdkUtil.IS_JDK8
    }

    public int append(String content) {
        try {
            // 声明一个可变长的stringBuffer对象
            StringBuffer sb = new StringBuffer("");

            /*
             * 读取完整文件
             */
            Reader reader = new FileReader("." + File.separator + "hello1.txt");

            // 这里我们用到了字符操作的BufferedReader类
            BufferedReader bufferedReader = new BufferedReader(reader);
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

            /*
             * 完整写入文件
             */
            Writer writer = new FileWriter("." + File.separator + "hello2.txt");
            BufferedWriter bw = new BufferedWriter(writer);
            // 注意这里调用了toString方法
            bw.write(sb.toString());
            // 注意这两个关闭的顺序
            bw.close();
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

}
