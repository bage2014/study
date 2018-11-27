package com.bage.study.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
            try {
                Socket socket = new Socket("localhost", 8080);
                // 发送给服务器的数据
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF("hello");
                // 接收服务器的返回数据
                DataInputStream in = new DataInputStream(socket.getInputStream());
                System.out.println("接收服务器的返回数据:" + in.readUTF());
                Thread.sleep(10000);
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
	}
}
