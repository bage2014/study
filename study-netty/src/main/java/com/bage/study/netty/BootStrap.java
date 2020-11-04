package com.bage.study.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class BootStrap {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",8090));

        //如果channel设置为阻塞的，则read会阻塞直到有数据返回，否则则会立刻返回，不管有没有数据
        socketChannel.configureBlocking(true);

        ByteBuffer buffer = ByteBuffer.wrap("hello world".getBytes());

        socketChannel.write(buffer);

        buffer.clear();

        long start = System.currentTimeMillis();


        socketChannel.read(buffer);

//        while (socketChannel.read(buffer)<=0);

        System.out.println("receive cost time:"+(System.currentTimeMillis()-start)+"ms");

        buffer.flip();



        while (buffer.hasRemaining()){
            System.out.print((char)buffer.get());
        }

        System.out.println();

        socketChannel.close();
    }
}
