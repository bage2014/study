package com.bage.study.java.io.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
    public static void main(String[] args) throws IOException {
        String FILE_PATH = "fromFile.txt";
        File file = new File(FILE_PATH);
        if(!file.exists()){
            file.createNewFile();
        }

        // 获取管道
        RandomAccessFile raf = new RandomAccessFile(FILE_PATH, "rw");
        FileChannel rafChannel = raf.getChannel();

        // 准备数据
        String data = "新数据，时间： " + System.currentTimeMillis();
        System.out.println("原数据：\n" + "   " + data);
        ByteBuffer buffer = ByteBuffer.allocate(128);
        buffer.clear();
        buffer.put(data.getBytes());
        buffer.flip();

        // 写入数据
        rafChannel.write(buffer);

        rafChannel.close();
        raf.close();

        // 重新打开管道
        raf = new RandomAccessFile(FILE_PATH, "rw");
        rafChannel = raf.getChannel();

        // 读取刚刚写入的数据
        buffer.clear();
        rafChannel.read(buffer);

        // 打印读取出的数据
        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        System.out.println("读取到的数据：\n" + "   " + new String(bytes));

        rafChannel.close();
        raf.close();

        // 删除文件
        file.deleteOnExit();
    }

}
